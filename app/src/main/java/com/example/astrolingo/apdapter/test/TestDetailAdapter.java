package com.example.astrolingo.apdapter.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.api.TestApi;
import com.example.astrolingo.domain.test.question_test;
import com.example.astrolingo.domain.test.testDetail_page;
import com.example.astrolingo.domain.test.AudioState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.example.astrolingo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<testDetail_page> itemList;

    public TestDetailAdapter(List<testDetail_page> itemList) {
        this.itemList = itemList;
    }



    // ---------------------------------- Edit layout ----------------------------------
    public static class StartPartViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitlePart, txtContentPart;

        public StartPartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitlePart = itemView.findViewById(R.id.title_part);
            txtContentPart = itemView.findViewById(R.id.content_part);
        }
    }

    public static class ListeningViewHolder extends RecyclerView.ViewHolder {
        ListView lv_question;
        ConstraintLayout box_audio;

        ImageView audio_reply5, audio_pause, audio_forward5;
        TextView audio_starttime, audio_endtime;
        SeekBar audio_seekbar;

        public ListeningViewHolder(@NonNull View itemView) {
            super(itemView);

            lv_question = itemView.findViewById(R.id.lv_question);
            box_audio = itemView.findViewById(R.id.box_audio);

            audio_reply5 = itemView.findViewById(R.id.audio_reply5);
            audio_pause = itemView.findViewById(R.id.audio_pause);
            audio_forward5 = itemView.findViewById(R.id.audio_forward5);

            audio_starttime = itemView.findViewById(R.id.audio_starttime);
            audio_endtime = itemView.findViewById(R.id.audio_endtime);

            audio_seekbar = itemView.findViewById(R.id.audio_seekbar);
        }
    }

//    public static class ReadingViewHolder extends RecyclerView.ViewHolder {
//        ListView lv_question;
//        ImageView audio_reply5, audio_pause, audio_forward5;
//        TextView audio_starttime, audio_endtime;
//        SeekBar audio_seekbar;
//
//        public ReadingViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            lv_question = itemView.findViewById(R.id.lv_question);
//            audio_reply5 = itemView.findViewById(R.id.audio_reply5);
//            audio_pause = itemView.findViewById(R.id.audio_pause);
//            audio_forward5 = itemView.findViewById(R.id.audio_forward5);
//
//            audio_starttime = itemView.findViewById(R.id.audio_starttime);
//            audio_endtime = itemView.findViewById(R.id.audio_endtime);
//
//            audio_seekbar = itemView.findViewById(R.id.audio_seekbar);
//        }
//    }


    // ---------------------------------- ----------------------------------

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.page_test_detail_startpart, parent, false);

            return new StartPartViewHolder(view);

        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.page_test_detail_listening, parent, false);

            return new ListeningViewHolder(view);

        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.page_test_detail_listening, parent, false);

            return new ListeningViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        testDetail_page item = itemList.get(position);
        return item.getType();
    }


    // Audio
    public MediaPlayer curMediaPlayer = null;
    public Map<Integer, AudioState> map_audio = new HashMap<>();
    public Handler handler = new Handler();
    public Runnable updateSeekBar= null;
//    public boolean isPlaying = false;
    // ---

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        testDetail_page item = itemList.get(position);

        if (holder instanceof StartPartViewHolder) {
            StartPartViewHolder viewHolder = (StartPartViewHolder) holder;

            viewHolder.txtTitlePart.setText(String.valueOf(item.getTitle()));
            viewHolder.txtContentPart.setText(String.valueOf(item.getContent()));

        } else if (holder instanceof ListeningViewHolder) {
            Log.e("position", String.valueOf(position) + String.valueOf(item.getTitle()));

            ListeningViewHolder viewHolder = (ListeningViewHolder) holder;

            List<question_test> list_QuestionTest = new ArrayList<>();

            // add image (if exist)
            ArrayList<String> listImageUrl = item.getListImageUrl();
            for(int i = 0; i < listImageUrl.size(); i++) {
                list_QuestionTest.add(new question_test(listImageUrl.get(i)));
            }

            // add question and set adapter into listView
            addQuestion(list_QuestionTest, item, viewHolder.lv_question.getContext(), viewHolder);

            if(item.getType() == 1) {
                // is Listening Page

                String urlAudio = item.getAudioUrl();

                // init
                if(!map_audio.containsKey(position)) {
                    curMediaPlayer = new MediaPlayer();

                    try {
                        curMediaPlayer.setDataSource(urlAudio);
                        curMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        viewHolder.audio_seekbar.setVisibility(View.INVISIBLE);
                        curMediaPlayer.prepareAsync();

                        viewHolder.audio_seekbar.setProgress(0);

                        curMediaPlayer.setOnPreparedListener(mp -> {
                            viewHolder.audio_seekbar.setVisibility(View.VISIBLE);

                            Objects.requireNonNull(map_audio.get(position)).setPlaying(false);

                            viewHolder.audio_seekbar.setMax(mp.getDuration());
                            startUpdatingSeekBar(viewHolder, position); // Hàm riêng bên dưới
                        });

                        curMediaPlayer.setOnCompletionListener(mp -> {
                            handler.removeCallbacks(updateSeekBar);
                            viewHolder.audio_pause.setImageResource(R.drawable.icon_asset_play_fill);
                            Objects.requireNonNull(map_audio.get(position)).setPlaying(false);
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    map_audio.put(position, new AudioState(curMediaPlayer,position));
                }

                // edit seekbar and listener
                // viewHolder.audio_seekbar

                if(!urlAudio.isEmpty()) {
                    // set pause button
                    viewHolder.audio_pause.setImageResource(R.drawable.icon_asset_play_fill);
                    viewHolder.audio_pause.setOnClickListener(v -> {
                        playAudio(urlAudio, viewHolder, position, viewHolder.audio_pause);
                    });

                    // Handle when user moves SeekBar
                    viewHolder.audio_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (Objects.requireNonNull(map_audio.get(position)).getMediaPlayer() != null && fromUser) {
                                Objects.requireNonNull(map_audio.get(position)).getMediaPlayer().seekTo(progress);
                            }
                        }
                        @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                        @Override public void onStopTrackingTouch(SeekBar seekBar) {}
                    });

                    // set relay or forward button
                    viewHolder.audio_reply5.setOnClickListener(v -> {
                        MediaPlayer mediaPlayer = Objects.requireNonNull(map_audio.get(position)).getMediaPlayer();
                        if (mediaPlayer != null) {
                            int currentPosition = mediaPlayer.getCurrentPosition();
                            int newPosition = Math.max(currentPosition - 5000, 0); // Giảm 5 giây, không nhỏ hơn 0
                            mediaPlayer.seekTo(newPosition);
                            viewHolder.audio_seekbar.setProgress(newPosition);
                        }
                    });

                    // set relay or forward button
                    viewHolder.audio_forward5.setOnClickListener(v -> {
                        MediaPlayer mediaPlayer = Objects.requireNonNull(map_audio.get(position)).getMediaPlayer();
                        if (mediaPlayer != null) {
                            int currentPosition = mediaPlayer.getCurrentPosition();
                            int newPosition = Math.min(currentPosition + 5000, mediaPlayer.getDuration()); // Giảm 5 giây, không nhỏ hơn 0
                            mediaPlayer.seekTo(newPosition);
                            viewHolder.audio_seekbar.setProgress(newPosition);
                        }
                    });

                }
            }
            else {
                // is Reading Page
                viewHolder.box_audio.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ------------------------------------ function ------------------------------------
    private void startUpdatingSeekBar(ListeningViewHolder viewHolder, int position) {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (Objects.requireNonNull(map_audio.get(position)).getMediaPlayer() != null && Objects.requireNonNull(map_audio.get(position)).isPlaying()) {
                    viewHolder.audio_seekbar.setProgress(Objects.requireNonNull(map_audio.get(position)).getMediaPlayer().getCurrentPosition());
                    handler.postDelayed(this, 500);
                }
            }
        };
        handler.post(updateSeekBar);
    }

    private void playAudio(String urlAudio, ListeningViewHolder viewHolder, int positionAdapter, ImageView audio_pause) {
//        if (!map_audio.containsKey(positionAdapter)) {
//            //
////            bug here
//
//        } else {
//
//        }

        if (Objects.requireNonNull(map_audio.get(positionAdapter)).isPlaying()) {
            // Đang phát → pause
            Objects.requireNonNull(map_audio.get(positionAdapter)).getMediaPlayer().pause();
            handler.removeCallbacks(updateSeekBar);
            Objects.requireNonNull(map_audio.get(positionAdapter)).setPlaying(false);
            audio_pause.setImageResource(R.drawable.icon_asset_play_fill);
        } else {
            // Đang pause → resume
            Objects.requireNonNull(map_audio.get(positionAdapter)).getMediaPlayer().start();
            Objects.requireNonNull(map_audio.get(positionAdapter)).setPlaying(true);
            startUpdatingSeekBar(viewHolder, positionAdapter);
            audio_pause.setImageResource(R.drawable.icon_asset_pause_fill);
        }
    }

    private void addQuestion(List<question_test> list_QuestionTest, testDetail_page item, Context context, ListeningViewHolder viewHolder) {
        // list_QuestionTest.add(new question_test("What does the woman want to find?", "(A) Some money", "(B) A file", "(C) An office key", "(D) A check", 2));

        SharedPreferenceClass sharedClass = new SharedPreferenceClass(context);

        /*
            [
                {
                    "_id": "68206440a87bed1c0357e7b7",
                    "question_id": "test1_32",
                    "group_question_id": "test1_part3_1",
                    "question_text": "What does the woman want to find?",
                    "correct_answer": 2,
                    "ans_1": "(A) Some money",
                    "ans_2": "(B) A file",
                    "ans_3": "(C) An office key",
                    "ans_4": "(D) A check"
                }
            ]
         */

        TestApi.getListQuestion(
            item.getGroupQuestionId(),
            context,
            sharedClass.getValue_string("token"),
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray arrayObject) {
                    // Xử lý khi thành công
                    // Log.d("API_SUCCESS", response.toString());

                    try {
                        for(int i = 0; i < arrayObject.length(); i++) {
                            JSONObject object = arrayObject.getJSONObject(i);

                            String questionText, ans1, ans2, ans3, ans4;
                            int correctNumber;

                            if(!object.isNull("question_text"))
                                questionText = object.getString("question_text");
                            else
                                questionText = "null";

                            if(!object.isNull("ans_1"))
                                ans1 = object.getString("ans_1");
                            else
                                ans1 = "null";

                            if(!object.isNull("ans_2"))
                                ans2 = object.getString("ans_2");
                            else
                                ans2 = "null";

                            if(!object.isNull("ans_3"))
                                ans3 = object.getString("ans_3");
                            else
                                ans3 = "null";

                            if(!object.isNull("ans_4"))
                                ans4 = object.getString("ans_4");
                            else
                                ans4 = "null";

                            if(!object.isNull("correct_answer"))
                                correctNumber = object.getInt("correct_answer");
                            else
                                correctNumber = 1;

                            list_QuestionTest.add(new question_test(questionText, ans1, ans2, ans3, ans4, correctNumber));
                        }

                        QuestionAdapter adapter = new QuestionAdapter(viewHolder.lv_question.getContext(), list_QuestionTest);
                        viewHolder.lv_question.setAdapter(adapter);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Xử lý khi có lỗi
                    Log.e("API_ERROR", error.toString());
                }
            }
        );

    }
}

