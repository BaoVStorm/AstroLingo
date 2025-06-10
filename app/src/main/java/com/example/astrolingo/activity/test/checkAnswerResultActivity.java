package com.example.astrolingo.activity.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.AnswerTestMananger;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.apdapter.test.QuestionAdapter;
import com.example.astrolingo.apdapter.test.TestDetailAdapter;
import com.example.astrolingo.api.TestApi;
import com.example.astrolingo.domain.test.nav_answer;
import com.example.astrolingo.domain.test.question_test;
import com.example.astrolingo.function.NumberManager;
import com.example.astrolingo.function.StringManager;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class checkAnswerResultActivity extends AppCompatActivity {

    // input
    private String group_question_id;
    private int part_id, cur_question_id, test_id, correct_number;
    private int selected_number;

    //
    SharedPreferenceClass sharedClass;

    View viewHolder;

    ListView lv_question;
    ConstraintLayout box_audio;

    ImageView audio_reply5, audio_pause, audio_forward5;
    TextView audio_starttime, audio_endtime;
    SeekBar audio_seekbar;

    LinearLayout navQuestion;
    View navQuestion1, navQuestion2, navQuestion3, navQuestion4, navQuestion5;
    View navAnswer1, navAnswer2, navAnswer3, navAnswer4;

    private ImageView backIcon;

    // audio
    public Handler handler = new Handler();
    public Runnable updateSeekBar= null;
    MediaPlayer curMediaPlayer;
    private boolean isPlaying = false;
    private int position = 0;


    //
    List<question_test> list_QuestionTest = new ArrayList<>();

    HashMap<String, ArrayList<Integer>> map_groupQuestion = new HashMap<>();
    HashMap<Number, nav_answer> map_answer = new HashMap<>();

    //
    String urlAudio = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_test_check_answer_result);

        sharedClass = new SharedPreferenceClass(this);

        // đầu vào
        cur_question_id = getIntent().getIntExtra("question_id", -1);
        group_question_id = getIntent().getStringExtra("group_question_id");
        part_id = getIntent().getIntExtra("part_id", -1);
        selected_number = getIntent().getIntExtra("selected_number", -1);
        correct_number = getIntent().getIntExtra("correct_number", -1);
        // test_id = getIntent().getIntExtra("test_id", -1);
        // selected_number = getIntent().getIntExtra("selected_number", -1);

        // ---
        viewHolder = findViewById(R.id.ListeningViewHolder);

        lv_question = viewHolder.findViewById(R.id.lv_question);
        audio_seekbar = viewHolder.findViewById(R.id.audio_seekbar);
        audio_starttime = viewHolder.findViewById(R.id.audio_starttime);
        audio_endtime = viewHolder.findViewById(R.id.audio_endtime);
        audio_reply5 = viewHolder.findViewById(R.id.audio_reply5);
        audio_forward5 = viewHolder.findViewById(R.id.audio_forward5);
        box_audio = viewHolder.findViewById(R.id.box_audio);

        audio_pause = viewHolder.findViewById(R.id.audio_pause);
        audio_pause.setImageResource(R.drawable.icon_asset_play_fill);

        backIcon = findViewById(R.id.backIcon);

        // init navAnswer, mavQuestion
        initNavAnswer(viewHolder);

        // add image (if exist)
        getListImageUrl();

        backIcon.setOnClickListener(v ->{
            finish();
        });
    }

    private void getListImageUrl() {

        /*

        {
            "_id": "68206059a87bed1c0357e730",
            "part_id": 1,
            "group_question_id": "test1_part1_1",
            "url_image1": "https://estudyme.hoc102.com/legacy-data/kslearning/images/418922160-1620725865601-pic1.png",
            "url_audio": "https://storage.googleapis.com/estudyme/dev/2022/06/27/30449101.mp3",
            "test_id": 1
        }
        * */

        TestApi.getGroupQuestionDetail(
                group_question_id,
                this,
                sharedClass.getValue_string("token"),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Xử lý khi thành công

                        try {

                            // add list imageurl
                            ArrayList<String> listImageUrl = new ArrayList<>();

                            if(!jsonObject.isNull("url_image1"))
                                listImageUrl.add(jsonObject.getString("url_image1"));

                            for(int index = 1; index <= 5; index++) {
                                String urlImageText = "url_image" + (index + 1);

                                if(!jsonObject.isNull(urlImageText))
                                    listImageUrl.add(jsonObject.getString(urlImageText));
                                else
                                    break;
                            }

                            for(int i = 0; i < listImageUrl.size(); i++) {
                                list_QuestionTest.add(new question_test(listImageUrl.get(i)));
                            }

                            urlAudio = jsonObject.optString("url_audio", "");

                            // add question and set adapter into listView
                            addQuestion();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý khi có lỗi
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }


    private void addQuestion() {
        // list_QuestionTest.add(new question_test("What does the woman want to find?", "(A) Some money", "(B) A file", "(C) An office key", "(D) A check", 2));


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
                group_question_id,
                this,
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
//                                if(AnswerTestMananger.list_answer.size() < question_id) {
                                    nav_answer navAnswer = new nav_answer(0, count_ans);
                                    navAnswer.setCorrectAnswer(correctNumber);
                                    navAnswer.setInfo(part_id, question_id);
                                    navAnswer.setGroup_question_id(object.getString("group_question_id"));
                                    navAnswer.setQuestion_id_text(object.getString("question_id"));
//
                                    map_answer.put(question_id, navAnswer);

//                                    AnswerTestMananger.list_answer.add(navAnswer);
//                                }
                            }

                            if(!map_groupQuestion.containsKey(group_question_id))
                                map_groupQuestion.put(group_question_id, list_question);

                            QuestionAdapter adapter = new QuestionAdapter(lv_question.getContext(), list_QuestionTest);
                            lv_question.setAdapter(adapter);

                            editNavAnswer();

                            finalInitValue();

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


    private void finalInitValue() {
        if(part_id <= 4) {
            // is Listening Page

            // init
            curMediaPlayer = new MediaPlayer();
            position = 0;

            try {
                curMediaPlayer.setDataSource(urlAudio);
                curMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                audio_seekbar.setVisibility(View.INVISIBLE);
                curMediaPlayer.prepareAsync();

                audio_seekbar.setProgress(0);
                audio_starttime.setText(NumberManager.numberToTime_minute(0));

                curMediaPlayer.setOnPreparedListener(mp -> {
                    audio_seekbar.setVisibility(View.VISIBLE);
                    audio_endtime.setText(NumberManager.numberToTime_minute(mp.getDuration()));

                    isPlaying = false;

                    audio_seekbar.setMax(mp.getDuration());
                    startUpdatingSeekBar(); // Hàm riêng bên dưới
                });

                curMediaPlayer.setOnCompletionListener(mp -> {
                    handler.removeCallbacks(updateSeekBar);
                    audio_pause.setImageResource(R.drawable.icon_asset_play_fill);
                    isPlaying = false;
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

//            AudioState audioState = new AudioState(curMediaPlayer,position);
//            audioState.setAudioPause(audio_pause);
//            audioState.setAudioEndtime(audio_endtime);
//            AudioTestManager.map_audio.put(position, audioState);

            // edit seekbar and listener
            // viewHolder.audio_seekbar

            if(!urlAudio.isEmpty()) {
                // set pause button
                audio_pause.setImageResource(R.drawable.icon_asset_play_fill);
                audio_pause.setOnClickListener(v -> {
                    playAudio();
                });

                // Handle when user moves SeekBar
                audio_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (curMediaPlayer != null && fromUser) {
                            curMediaPlayer.seekTo(progress);
                        }
                    }
                    @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override public void onStopTrackingTouch(SeekBar seekBar) {}
                });

                // set relay or forward button
                audio_reply5.setOnClickListener(v -> {
                    if (curMediaPlayer != null) {
                        int currentPosition = curMediaPlayer.getCurrentPosition();
                        int newPosition = Math.max(currentPosition - 5000, 0); // Giảm 5 giây, không nhỏ hơn 0
                        curMediaPlayer.seekTo(newPosition);
                        audio_seekbar.setProgress(newPosition);
                        audio_starttime.setText(NumberManager.numberToTime_minute(newPosition));
                    }
                });

                // set relay or forward button
                audio_forward5.setOnClickListener(v -> {
                    if (curMediaPlayer != null) {
                        int currentPosition = curMediaPlayer.getCurrentPosition();
                        int newPosition = Math.min(currentPosition + 5000, curMediaPlayer.getDuration()); // Giảm 5 giây, không nhỏ hơn 0
                        curMediaPlayer.seekTo(newPosition);
                        audio_seekbar.setProgress(newPosition);
                        audio_starttime.setText(NumberManager.numberToTime_minute(newPosition));
                    }
                });

            }
        }
        else {
            // is Reading Page
            box_audio.setVisibility(View.GONE);
        }
    }

    private void startUpdatingSeekBar() {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (curMediaPlayer != null && isPlaying) {
                    int currentPosition = curMediaPlayer.getCurrentPosition();

                    audio_seekbar.setProgress(currentPosition);
                    audio_starttime.setText(NumberManager.numberToTime_minute(currentPosition));
                    handler.postDelayed(this, 500);
                } else {
                    // Không phát nữa => dừng cập nhật seekbar
                    handler.removeCallbacks(this);
                }
            }
        };
        handler.post(updateSeekBar);
    }

    private void playAudio() {
        if (isPlaying) {
            // Đang phát → pause
            curMediaPlayer.pause();
            handler.removeCallbacks(updateSeekBar);
            isPlaying = false;
            audio_pause.setImageResource(R.drawable.icon_asset_play_fill);
        } else {
            // Đang pause → resume
            curMediaPlayer.start();
            isPlaying = true;
            startUpdatingSeekBar();
            audio_pause.setImageResource(R.drawable.icon_asset_pause_fill);
        }
    }

    private void editNavAnswer() {
        if(!map_groupQuestion.containsKey(group_question_id))
            return;

        ArrayList<Integer> list_question = map_groupQuestion.get(group_question_id);

        View[] views = new View[] {
                navQuestion1,
                navQuestion2,
                navQuestion3,
                navQuestion4,
                navQuestion5
        };

        int index = 0;

        if(list_question.size() <= 1) {
            navQuestion.setVisibility(View.GONE);
        }
        else {
            navQuestion.setVisibility(View.VISIBLE);

            for(int i = 0; i < list_question.size(); i++) {

                TextView navQuestion_text = views[i].findViewById(R.id.navQuestion_text);
                int lastNumber = StringManager.extractLastNumber(list_question.get(i).toString());
                navQuestion_text.setText(this.getString(R.string.Sentence) + " " + lastNumber);

                // set listener lick question i
//                int finalI = i;
//                views[i].setOnClickListener(v -> {
//                    // reset hightlight question
//                    for (View view : views) {
//                        view.findViewById(R.id.navQuestion_highlight).setVisibility(View.GONE);
//                    }
//
//                    chooseQuestion(list_question.get(finalI));
//                    v.findViewById(R.id.navQuestion_highlight).setVisibility(View.VISIBLE);
//                });

                views[i].setVisibility(View.GONE);

                if(cur_question_id == lastNumber) {
                    index = i;
                    views[i].setVisibility(View.VISIBLE);
                }
            }

            for(int i = list_question.size(); i < 5; i++) {
                views[i].setVisibility(View.GONE);

            }

        }

        // choose question1
        chooseQuestion(list_question.get(index));
        // navQuestion1.findViewById(R.id.navQuestion_highlight).setVisibility(View.VISIBLE);
    }

    private void resetAnswer(ShapeableImageView avatarImageView,TextView navAnswer_text) {
        avatarImageView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        avatarImageView.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_grey)));

        navAnswer_text.setTextColor(ContextCompat.getColor(this, R.color.dark_grey));
    }

    private void highlightAnswer(ShapeableImageView avatarImageView,TextView navAnswer_text) {
        avatarImageView.setBackgroundColor(ContextCompat.getColor(this, R.color.chooseAnswer));
        avatarImageView.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.chooseAnswer)));

        navAnswer_text.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private void highLightAnswerCorrect(ShapeableImageView avatarImageView, TextView navAnswer_text) {
        avatarImageView.setBackgroundColor(ContextCompat.getColor(this, R.color.correctAnswer));
        avatarImageView.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.correctAnswer_Border)));

        navAnswer_text.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private void highLightAnswer(ShapeableImageView avatarImageView, TextView navAnswer_text) {
        avatarImageView.setBackgroundColor(ContextCompat.getColor(this, R.color.wrongAnswer));
        avatarImageView.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.wrongAnswer_Border)));

        navAnswer_text.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private void chooseQuestion(int question_id) {

        nav_answer navAnswer = map_answer.get(question_id);

        View[] views = new View[] {
                navAnswer1,
                navAnswer2,
                navAnswer3,
                navAnswer4
        };

        for(int i = 0; i < Objects.requireNonNull(navAnswer).getCountAns(); i++) {
            views[i].setVisibility(View.VISIBLE);
        }

        // Reset tất cả đáp án về màu gốc
        for (View view : views) {
            resetAnswer(view.findViewById(R.id.avatarImageView), view.findViewById(R.id.navAnswer_text));
        }

        int currentChoose = selected_number;
        if(currentChoose >= 1 && currentChoose <= 4)
            highLightAnswer(views[currentChoose - 1].findViewById(R.id.avatarImageView), views[currentChoose - 1].findViewById(R.id.navAnswer_text));

        highLightAnswerCorrect(views[correct_number - 1].findViewById(R.id.avatarImageView), views[correct_number - 1].findViewById(R.id.navAnswer_text));

        for(int i = navAnswer.getCountAns(); i < 4; i++)
            views[i].setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    private void initNavAnswer(View viewHolder) {
        navAnswer1 = viewHolder.findViewById(R.id.navAnswer1);
        navAnswer2 = viewHolder.findViewById(R.id.navAnswer2);
        navAnswer3 = viewHolder.findViewById(R.id.navAnswer3);
        navAnswer4 = viewHolder.findViewById(R.id.navAnswer4);

        navQuestion = viewHolder.findViewById(R.id.navQuestion);

        navQuestion1 = viewHolder.findViewById(R.id.navQuestion1);
        navQuestion2 = viewHolder.findViewById(R.id.navQuestion2);
        navQuestion3 = viewHolder.findViewById(R.id.navQuestion3);
        navQuestion4 = viewHolder.findViewById(R.id.navQuestion4);
        navQuestion5 = viewHolder.findViewById(R.id.navQuestion5);

        // function

        View[] navQuestions = new View[] {
                navQuestion1,
                navQuestion2,
                navQuestion3,
                navQuestion4,
                navQuestion5
        };

        for (int i = 0; i < navQuestions.length; i++) {
            TextView navQuestion_text = navQuestions[i].findViewById(R.id.navQuestion_text);
            View navQuestion_highlight = navQuestions[i].findViewById(R.id.navQuestion_highlight);

            navQuestion_text.setText(this.getString(R.string.Sentence) + " " + (i + 1));

            navQuestion_highlight.setVisibility(View.VISIBLE);
            navQuestions[i].setVisibility(View.GONE);
        }

        navQuestion.setVisibility(View.GONE);

        View[] navAnswers = new View[] {
                navAnswer1,
                navAnswer2,
                navAnswer3,
                navAnswer4
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 1. Giải phóng MediaPlayer nếu đang dùng
        if (curMediaPlayer != null) {
            if (curMediaPlayer.isPlaying()) {
                curMediaPlayer.stop();
            }
            curMediaPlayer.release(); // giải phóng
            curMediaPlayer = null;
        }

        // 2. Dừng và xóa callback Handler
        if (handler != null && updateSeekBar != null) {
            handler.removeCallbacks(updateSeekBar);
            updateSeekBar = null;
        }

        // 3. Xóa List nếu cần
        if (list_QuestionTest != null) {
            list_QuestionTest.clear();
            list_QuestionTest = null;
        }

        // 4. Xóa map
        if (map_groupQuestion != null) {
            map_groupQuestion.clear();
            map_groupQuestion = null;
        }

        if (map_answer != null) {
            map_answer.clear();
            map_answer = null;
        }

        // 5. Gỡ tham chiếu các view lớn (optional nếu bạn gặp memory leak)
        lv_question = null;
        navQuestion = null;
        box_audio = null;

        audio_reply5 = null;
        audio_pause = null;
        audio_forward5 = null;
        audio_seekbar = null;
        audio_starttime = null;
        audio_endtime = null;

        navQuestion1 = navQuestion2 = navQuestion3 = navQuestion4 = navQuestion5 = null;
        navAnswer1 = navAnswer2 = navAnswer3 = navAnswer4 = null;

        sharedClass = null;
        viewHolder = null;
    }


}
