package com.example.astrolingo.apdapter.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.Service.AnswerTestMananger;
import com.example.astrolingo.Service.AudioTestManager;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.api.TestApi;
import com.example.astrolingo.domain.test.nav_answer;
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
import com.example.astrolingo.function.NumberManager;
import com.example.astrolingo.function.StringManager;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class TestDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<testDetail_page> itemList;
    private ViewPager2 viewPager;

    public TestDetailAdapter(List<testDetail_page> itemList, ViewPager2 viewPager) {
        this.itemList = itemList;
        this.viewPager = viewPager;
    }

    // ---------------------------------- Edit layout ----------------------------------
    public static class StartPartViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitlePart, txtContentPart;
        ConstraintLayout button_login;

        public StartPartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitlePart = itemView.findViewById(R.id.title_part);
            txtContentPart = itemView.findViewById(R.id.content_part);
            button_login = itemView.findViewById(R.id.button_login);
        }
    }

    public static class ListeningViewHolder extends RecyclerView.ViewHolder {
        ListView lv_question;
        ConstraintLayout box_audio;

        ImageView audio_reply5, audio_pause, audio_forward5;
        TextView audio_starttime, audio_endtime;
        SeekBar audio_seekbar;

        LinearLayout navQuestion;
        View navQuestion1, navQuestion2, navQuestion3, navQuestion4, navQuestion5;
        View navAnswer1, navAnswer2, navAnswer3, navAnswer4;

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

            // nav Answer, Question
            navQuestion = itemView.findViewById(R.id.navQuestion);
            navQuestion1 = itemView.findViewById(R.id.navQuestion1);
            navQuestion2 = itemView.findViewById(R.id.navQuestion2);
            navQuestion3 = itemView.findViewById(R.id.navQuestion3);
            navQuestion4 = itemView.findViewById(R.id.navQuestion4);
            navQuestion5 = itemView.findViewById(R.id.navQuestion5);

            navAnswer1 = itemView.findViewById(R.id.navAnswer1);
            navAnswer2 = itemView.findViewById(R.id.navAnswer2);
            navAnswer3 = itemView.findViewById(R.id.navAnswer3);
            navAnswer4 = itemView.findViewById(R.id.navAnswer4);
        }
    }

    // ---------------------------------------------------------------------

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.page_test_detail_startpart, parent, false);

            return new StartPartViewHolder(view);
        }
        else {
            view = inflater.inflate(R.layout.page_test_detail_listening, parent, false);

            return new ListeningViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        testDetail_page item = itemList.get(position);
        return item.getType();
    }

    // Audio
    public Handler handler = new Handler();
    public Runnable updateSeekBar= null;

    // Lưu số câu hỏi trong 1 group_question và số đáp án, câu trả lời trong 1 câu hỏi
    HashMap<String, ArrayList<Integer>> map_groupQuestion = new HashMap<>();     // key: group_question_id, value: danh sách các mã câu hỏi
    // HashMap<Integer, nav_answer> map_answer = new HashMap<>();     // key: mã câu hỏi, value: nav_answer(count_ans, currentChoose)

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        testDetail_page item = itemList.get(position);

        if (holder instanceof StartPartViewHolder) {
            StartPartViewHolder viewHolder = (StartPartViewHolder) holder;

            viewHolder.txtTitlePart.setText(String.valueOf(item.getTitle()));
            viewHolder.txtContentPart.setText(String.valueOf(item.getContent()));

            viewHolder.button_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewPager != null && position + 1 < itemList.size()) {
                        viewPager.setCurrentItem(position + 1, true); // true = animate
                    }
                }
            });

        } else if (holder instanceof ListeningViewHolder) {
            ListeningViewHolder viewHolder = (ListeningViewHolder) holder;

            viewHolder.audio_pause.setImageResource(R.drawable.icon_asset_play_fill);

            // init navAnswer, mavQuestion
            initNavAnswer(viewHolder);

            List<question_test> list_QuestionTest = new ArrayList<>();

            // add image (if exist)
            ArrayList<String> listImageUrl = item.getListImageUrl();
            for(int i = 0; i < listImageUrl.size(); i++) {
                list_QuestionTest.add(new question_test(listImageUrl.get(i)));
            }

            // add question and set adapter into listView
            addQuestion(list_QuestionTest, item, viewHolder.lv_question.getContext(), viewHolder, position);

            if(item.getType() == 1) {
                // is Listening Page

                String urlAudio = item.getAudioUrl();

                // init
                if(!AudioTestManager.map_audio.containsKey(position)) {
                    MediaPlayer curMediaPlayer = new MediaPlayer();

                    try {
                        curMediaPlayer.setDataSource(urlAudio);
                        curMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        viewHolder.audio_seekbar.setVisibility(View.INVISIBLE);
                        curMediaPlayer.prepareAsync();

                        viewHolder.audio_seekbar.setProgress(0);
                        viewHolder.audio_starttime.setText(NumberManager.numberToTime_minute(0));

                        // media Được tair thành công
                        curMediaPlayer.setOnPreparedListener(mp -> {
                            viewHolder.audio_seekbar.setVisibility(View.VISIBLE);
                            viewHolder.audio_endtime.setText(NumberManager.numberToTime_minute(mp.getDuration()));

                            Objects.requireNonNull(AudioTestManager.map_audio.get(position)).setPlaying(false);

                            viewHolder.audio_seekbar.setMax(mp.getDuration());
                            startUpdatingSeekBar(viewHolder, position); // Hàm riêng bên dưới
                        });

                        curMediaPlayer.setOnCompletionListener(mp -> {
                            handler.removeCallbacks(updateSeekBar);
                            viewHolder.audio_pause.setImageResource(R.drawable.icon_asset_play_fill);
                            Objects.requireNonNull(AudioTestManager.map_audio.get(position)).setPlaying(false);
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    AudioState audioState = new AudioState(curMediaPlayer,position);
                    audioState.setAudioPause(viewHolder.audio_pause);
                    audioState.setAudioEndtime(viewHolder.audio_endtime);
                    AudioTestManager.map_audio.put(position, audioState);

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
                            if (Objects.requireNonNull(AudioTestManager.map_audio.get(position)).getMediaPlayer() != null && fromUser) {
                                Objects.requireNonNull(AudioTestManager.map_audio.get(position)).getMediaPlayer().seekTo(progress);
                            }
                        }
                        @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                        @Override public void onStopTrackingTouch(SeekBar seekBar) {}
                    });

                    // set relay or forward button
                    viewHolder.audio_reply5.setOnClickListener(v -> {
                        MediaPlayer mediaPlayer = Objects.requireNonNull(AudioTestManager.map_audio.get(position)).getMediaPlayer();
                        if (mediaPlayer != null) {
                            int currentPosition = mediaPlayer.getCurrentPosition();
                            int newPosition = Math.max(currentPosition - 5000, 0); // Giảm 5 giây, không nhỏ hơn 0
                            mediaPlayer.seekTo(newPosition);
                            viewHolder.audio_seekbar.setProgress(newPosition);
                            viewHolder.audio_starttime.setText(NumberManager.numberToTime_minute(newPosition));
                        }
                    });

                    // set relay or forward button
                    viewHolder.audio_forward5.setOnClickListener(v -> {
                        MediaPlayer mediaPlayer = Objects.requireNonNull(AudioTestManager.map_audio.get(position)).getMediaPlayer();
                        if (mediaPlayer != null) {
                            int currentPosition = mediaPlayer.getCurrentPosition();
                            int newPosition = Math.min(currentPosition + 5000, mediaPlayer.getDuration()); // Giảm 5 giây, không nhỏ hơn 0
                            mediaPlayer.seekTo(newPosition);
                            viewHolder.audio_seekbar.setProgress(newPosition);
                            viewHolder.audio_starttime.setText(NumberManager.numberToTime_minute(newPosition));
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
                AudioState audioState = AudioTestManager.map_audio.get(position);

                if (audioState != null && audioState.getMediaPlayer() != null && audioState.isPlaying()) {
                    int currentPosition = audioState.getMediaPlayer().getCurrentPosition();

                    viewHolder.audio_seekbar.setProgress(currentPosition);
                    viewHolder.audio_starttime.setText(NumberManager.numberToTime_minute(currentPosition));
                    handler.postDelayed(this, 500);
                } else {
                    // Không phát nữa => dừng cập nhật seekbar
                    handler.removeCallbacks(this);
                }
            }
        };
        handler.post(updateSeekBar);
    }

    private void playAudio(String urlAudio, ListeningViewHolder viewHolder, int positionAdapter, ImageView audio_pause) {
        if (Objects.requireNonNull(AudioTestManager.map_audio.get(positionAdapter)).isPlaying()) {
            // Đang phát → pause
            Objects.requireNonNull(AudioTestManager.map_audio.get(positionAdapter)).getMediaPlayer().pause();
            handler.removeCallbacks(updateSeekBar);
            Objects.requireNonNull(AudioTestManager.map_audio.get(positionAdapter)).setPlaying(false);
            audio_pause.setImageResource(R.drawable.icon_asset_play_fill);
        } else {
            // Đang pause → resume
            Objects.requireNonNull(AudioTestManager.map_audio.get(positionAdapter)).getMediaPlayer().start();
            Objects.requireNonNull(AudioTestManager.map_audio.get(positionAdapter)).setPlaying(true);
            startUpdatingSeekBar(viewHolder, positionAdapter);
            audio_pause.setImageResource(R.drawable.icon_asset_pause_fill);
        }
    }

    private void addQuestion(List<question_test> list_QuestionTest, testDetail_page item, Context context, ListeningViewHolder viewHolder, int position) {
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
                        ArrayList<Integer> list_question = new ArrayList<>();

                        for(int i = 0; i < arrayObject.length(); i++) {
                            int count_ans = 0;

                            JSONObject object = arrayObject.getJSONObject(i);

                            int question_id = StringManager.extractLastNumber(object.getString("question_id"));
                            int part_id = StringManager.extractNumberFromSecondPart(object.getString("group_question_id"));

                            String questionText, ans1, ans2, ans3, ans4;
                            int correctNumber;

                            if(!object.isNull("question_text"))
                                questionText = question_id + ". " + object.getString("question_text");
                            else
                                questionText = "null";

                            if(!object.isNull("ans_1")) {
                                ans1 = object.getString("ans_1");
                                count_ans++;
                            }
                            else
                                ans1 = "null";

                            if(!object.isNull("ans_2")) {
                                ans2 = object.getString("ans_2");
                                count_ans++;
                            } else
                                ans2 = "null";

                            if(!object.isNull("ans_3")) {
                                ans3 = object.getString("ans_3");
                                count_ans++;
                            } else
                                ans3 = "null";

                            if(!object.isNull("ans_4")) {
                                ans4 = object.getString("ans_4");
                                count_ans++;
                            } else
                                ans4 = "null";

                            if(!object.isNull("correct_answer")) {
                                correctNumber = object.getInt("correct_answer");
                            } else
                                correctNumber = 1;

                            list_QuestionTest.add(new question_test(questionText, ans1, ans2, ans3, ans4, correctNumber));

                            // add to list_question to add to HashMap map_groupQuestion
                            list_question.add(question_id);

                            // add to HashMap map_answer
                            if(AnswerTestMananger.list_answer.size() < question_id) {
                                nav_answer navAnswer = new nav_answer(0, count_ans);
                                navAnswer.setCorrectAnswer(correctNumber);
                                navAnswer.setInfo(part_id, question_id);
                                navAnswer.setGroup_question_id(object.getString("group_question_id"));
                                navAnswer.setQuestion_id_text(object.getString("question_id"));

                                AnswerTestMananger.list_answer.add(navAnswer);
                            }

                        }

                        String group_question_id = item.getGroupQuestionId();

                        if(!map_groupQuestion.containsKey(group_question_id))
                            map_groupQuestion.put(group_question_id, list_question);

                        editNavAnswer(group_question_id, viewHolder, position);

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

    private void editNavAnswer(String group_question_id, ListeningViewHolder viewHolder, int position) {
        if(!map_groupQuestion.containsKey(group_question_id))
            return;

        ArrayList<Integer> list_question = map_groupQuestion.get(group_question_id);

        View[] views = new View[] {
                viewHolder.navQuestion1,
                viewHolder.navQuestion2,
                viewHolder.navQuestion3,
                viewHolder.navQuestion4,
                viewHolder.navQuestion5
        };

        if(list_question.size() <= 1) {
            viewHolder.navQuestion.setVisibility(View.GONE);
        }
        else {
            viewHolder.navQuestion.setVisibility(View.VISIBLE);

            Context context = viewHolder.itemView.getContext();

            for(int i = 0; i < list_question.size(); i++) {
                views[i].setVisibility(View.VISIBLE);

                TextView navQuestion_text = views[i].findViewById(R.id.navQuestion_text);
                int lastNumber = StringManager.extractLastNumber(list_question.get(i).toString());
                navQuestion_text.setText(context.getString(R.string.Sentence) + " " + lastNumber);

                // set listener lick question i
                int finalI = i;
                views[i].setOnClickListener(v -> {
                    // reset hightlight question
                    for (View view : views) {
                        view.findViewById(R.id.navQuestion_highlight).setVisibility(View.GONE);
                    }

                    chooseQuestion(list_question.get(finalI), viewHolder, position);
                    v.findViewById(R.id.navQuestion_highlight).setVisibility(View.VISIBLE);
                });
            }

            for(int i = list_question.size(); i < 5; i++)
                views[i].setVisibility(View.GONE);
        }

        // reset hightlight question
        for (View view : views) {
            view.findViewById(R.id.navQuestion_highlight).setVisibility(View.GONE);
        }

        // choose question1
        chooseQuestion(list_question.get(0), viewHolder, position);
        viewHolder.navQuestion1.findViewById(R.id.navQuestion_highlight).setVisibility(View.VISIBLE);
    }

    private void chooseQuestion(int question_id, ListeningViewHolder viewHolder, int position) {
//        if(!AnswerTestMananger.map_answer.containsKey(question_id))
        if(AnswerTestMananger.list_answer.size()< question_id)
                return;

//        nav_answer navAnswer = AnswerTestMananger.map_answer.get(question_id);
        nav_answer navAnswer = AnswerTestMananger.getAnswer(question_id - 1);

        View[] views = new View[] {
            viewHolder.navAnswer1,
            viewHolder.navAnswer2,
            viewHolder.navAnswer3,
            viewHolder.navAnswer4
        };

        for(int i = 0; i < Objects.requireNonNull(navAnswer).getCountAns(); i++) {
            views[i].setVisibility(View.VISIBLE);

            // set listener lick answer i
            int finalI = i;
            views[i].setOnClickListener(v-> {
                navAnswer.setCurrentChoose(finalI + 1);

                // Reset tất cả đáp án về màu gốc
                for (View view : views) {
                    resetAnswer(view, viewHolder);
                }

                highlightAnswer(v, viewHolder);

//                if (viewPager != null && position + 1 < itemList.size()) {
//                    viewPager.setCurrentItem(position + 1, true); // true = animate
//                }

            });
        }

        // Reset tất cả đáp án về màu gốc
        for (View view : views) {
            resetAnswer(view, viewHolder);
        }

        int currentChoose = navAnswer.getCurrentChoose();
        if(currentChoose >= 1 && currentChoose <= 4)
            highlightAnswer(views[currentChoose - 1], viewHolder);

        for(int i = navAnswer.getCountAns(); i < 4; i++)
            views[i].setVisibility(View.GONE);
    }

    private void resetAnswer(View view, RecyclerView.ViewHolder viewHolder) {
        ShapeableImageView avatarImageView = view.findViewById(R.id.avatarImageView);
        avatarImageView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.white));
        avatarImageView.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.dark_grey)));

        TextView navAnswer_text = view.findViewById(R.id.navAnswer_text);
        navAnswer_text.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.dark_grey));
    }

    private void highlightAnswer(View view, RecyclerView.ViewHolder viewHolder) {
        ShapeableImageView avatarImageView = view.findViewById(R.id.avatarImageView);
        avatarImageView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.chooseAnswer));
        avatarImageView.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.chooseAnswer)));

        TextView navAnswer_text = view.findViewById(R.id.navAnswer_text);
        navAnswer_text.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.white));
    }

    @SuppressLint("SetTextI18n")
    private void initNavAnswer(ListeningViewHolder viewHolder) {
        View[] navQuestions = new View[] {
                viewHolder.navQuestion1,
                viewHolder.navQuestion2,
                viewHolder.navQuestion3,
                viewHolder.navQuestion4,
                viewHolder.navQuestion5
        };

        int index;

        for (int i = 0; i < navQuestions.length; i++) {
            TextView navQuestion_text = navQuestions[i].findViewById(R.id.navQuestion_text);
            View navQuestion_highlight = navQuestions[i].findViewById(R.id.navQuestion_highlight);

            Context context = viewHolder.itemView.getContext();

            navQuestion_text.setText(context.getString(R.string.Sentence) + " " + (i + 1));

            navQuestion_highlight.setVisibility(View.GONE);
            navQuestions[i].setVisibility(View.GONE);
        }

        viewHolder.navQuestion.setVisibility(View.GONE);

        View[] navAnswers = new View[] {
                viewHolder.navAnswer1,
                viewHolder.navAnswer2,
                viewHolder.navAnswer3,
                viewHolder.navAnswer4
        };

        String[] answers = new String[] {
                "A",
                "B",
                "C",
                "D"
        };

        for (int i = 0; i < navAnswers.length; i++) {
            TextView navAnswer_text = navAnswers[i].findViewById(R.id.navAnswer_text);

            navAnswer_text.setText(answers[i]);

            navAnswers[i].setVisibility(View.GONE);
        }
    }

    public void release() {
        if (handler != null && updateSeekBar != null) {
            handler.removeCallbacks(updateSeekBar);
        }
    }

    //    --------------------- update app
    // Memory Leak từ MediaPlayer và Handler
    // MediaPlayer cần được release() trong onViewRecycled() hoặc khi không dùng nữa.
//    @Override
//    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
//        super.onViewRecycled(holder);
//        if (holder instanceof ListeningViewHolder) {
//            int position = holder.getAdapterPosition();
//            if (map_audio.containsKey(position)) {
//                MediaPlayer mp = map_audio.get(position).getMediaPlayer();
//                if (mp != null) {
//                    mp.stop();
//                    mp.release();
//                }
//                map_audio.remove(position);
//            }
//            handler.removeCallbacks(updateSeekBar);
//        }
//    }
}

