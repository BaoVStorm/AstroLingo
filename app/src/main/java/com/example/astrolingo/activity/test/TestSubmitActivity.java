package com.example.astrolingo.activity.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.AnswerTestMananger;
import com.example.astrolingo.Service.AudioTestManager;
import com.example.astrolingo.Service.DateUtils;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.apdapter.test.FilterQuestionAdapter;
import com.example.astrolingo.apdapter.test.ResultQuestionAdapter;
import com.example.astrolingo.api.CertificateApi;
import com.example.astrolingo.api.ScoreAPI;
import com.example.astrolingo.api.UserApi;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TestSubmitActivity extends AppCompatActivity {
    private SharedPreferenceClass sharedPreClass;
    private JSONObject userObject;
    private TextView header_title, name_text, gender_text, country_text, DOB_text;
    private TextView test_date_title, test_date_content, valid_date_title, valid_date_content, name_test;
    private TextView listening_score, reading_score, total_score, pass_score;
    private ImageView backIcon, imgAvatar;
    private JSONObject testObject;
    private int numberQuestion, numberCorrect, numberWrong, numberCorrect_Listening, numberCorrect_Reading;
    private String currentDate, validDate, user_id, title_test;
    private int test_id, total_score_test;
    private BottomSheetDialog bottomDialog_filter;
    ConstraintLayout button_gotIt, button_result;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_test_submit);

        String testString = getIntent().getStringExtra("testObject");

        sharedPreClass = new SharedPreferenceClass(this);

        backIcon = findViewById(R.id.backIcon);
        header_title = findViewById(R.id.header_title);
        name_text = findViewById(R.id.name_text);
        gender_text = findViewById(R.id.gender_title);
        country_text = findViewById(R.id.country_text);
        DOB_text = findViewById(R.id.DOB_text);
        imgAvatar = findViewById(R.id.imgAvatar);

        name_test = findViewById(R.id.name_test);
        test_date_content = findViewById(R.id.test_date_content);
        valid_date_content = findViewById(R.id.valid_date_content);

        listening_score = findViewById(R.id.listening_score);
        reading_score = findViewById(R.id.reading_score);
        total_score = findViewById(R.id.total_score);
        pass_score = findViewById(R.id.pass_score);

        button_gotIt = findViewById(R.id.button_gotIt);
        button_result = findViewById(R.id.button_result);

        // Dialog
        bottomDialog_filter = new BottomSheetDialog(this);
        bottomDialog_filter.setContentView(R.layout.page_test_detail_dialog_filter);
        bottomDialog_filter.setCanceledOnTouchOutside(true);

        // init value
        header_title.setText("Test: Exam results");

        try {
            testObject = new JSONObject(testString);

            test_id = testObject.getInt("test_id");
            title_test = testObject.getString("title");

            name_test.setText(testObject.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
//            finish();
//            AnswerTestMananger.releaseAll();
            exitActivity();
        }

        // score test
        scoreTest();

        // edit user info
        getUserInfo();

        // get Date
        currentDate = DateUtils.getCurrentDate();
        validDate = DateUtils.calculateDate(2, 0, 0); // +2 năm

        test_date_content.setText(currentDate);
        valid_date_content.setText(validDate);

        // set event button
        button_gotIt.setOnClickListener(v -> {
            exitActivity();
        });

        button_result.setOnClickListener(v -> {
            bottomDialog_filter.show();
        });

        // ngăn lướt xuống dialog
        bottomDialog_filter.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED); // Mở full
                behavior.setSkipCollapsed(true); // Bỏ qua trạng thái collapsed
                behavior.setDraggable(false); // Ngăn không cho vuốt xuống để đóng
            }
        });

        editBottomDialogFilter();

        // back icon
        backIcon.setOnClickListener(v -> {
            exitActivity();
        });
    }

    private void editBottomDialogFilter() {
        ListView questionListView = bottomDialog_filter.findViewById(R.id.lv_question);

        ResultQuestionAdapter adapter = new ResultQuestionAdapter(this, AnswerTestMananger.list_answer);
        adapter.setBottomDialog_filter(bottomDialog_filter);
        questionListView.setAdapter(adapter);

        ConstraintLayout filter_listening = bottomDialog_filter.findViewById(R.id.filter_listening);
        ConstraintLayout filter_reading = bottomDialog_filter.findViewById(R.id.filter_reading);
        ConstraintLayout filter_all = bottomDialog_filter.findViewById(R.id.filter_all);

        View group_listening_highlight = bottomDialog_filter.findViewById(R.id.group_listening_highlight);
        View group_reading_highlight = bottomDialog_filter.findViewById(R.id.group_reading_highlight);
        View group_all_highlight = bottomDialog_filter.findViewById(R.id.group_all_highlight);

        LinearLayout box_type = bottomDialog_filter.findViewById(R.id.box_type);
        assert box_type != null;

        // init view
        box_type.setVisibility(View.GONE);
        group_listening_highlight.setVisibility(View.GONE);
        group_reading_highlight.setVisibility(View.GONE);
        group_all_highlight.setVisibility(View.VISIBLE);

        // Event type
        assert filter_listening != null;
        filter_listening.setOnClickListener(v -> {
            adapter.filterListening();
            questionListView.post(() -> questionListView.smoothScrollToPosition(0));

            group_listening_highlight.setVisibility(View.VISIBLE);
            group_reading_highlight.setVisibility(View.GONE);
            group_all_highlight.setVisibility(View.GONE);
        });

        assert filter_reading != null;
        filter_reading.setOnClickListener(v -> {
            adapter.filterReading();
            questionListView.post(() -> questionListView.smoothScrollToPosition(0));

            group_listening_highlight.setVisibility(View.GONE);
            group_reading_highlight.setVisibility(View.VISIBLE);
            group_all_highlight.setVisibility(View.GONE);
        });

        assert filter_all != null;
        filter_all.setOnClickListener(v -> {
            adapter.resetFilter();
            questionListView.post(() -> questionListView.smoothScrollToPosition(0));

            group_listening_highlight.setVisibility(View.GONE);
            group_reading_highlight.setVisibility(View.GONE);
            group_all_highlight.setVisibility(View.VISIBLE);
        });
    }

    private void saveTestScore() {
        // save certification of user
        HashMap<String, String> params = new HashMap<>();
        params.put("test_id", String.valueOf(test_id));
        params.put("user_id", user_id);
        params.put("certificate_name", title_test);
        params.put("reading_score", String.valueOf(numberCorrect_Reading));
        params.put("listening_score", String.valueOf(numberCorrect_Listening));
        params.put("total_score", String.valueOf(total_score_test));
        params.put("correct_count", String.valueOf(numberCorrect));
        params.put("wrong_count", String.valueOf(numberWrong));

        CertificateApi.addCertificate(
            params,
            this,
            sharedPreClass.getValue_string("token"),
            new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject response) {
                    // Xử lý khi thành công
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Xử lý khi có lỗi
                    Log.e("API_ERROR", error.toString());
                    Toast.makeText(TestSubmitActivity.this, String.valueOf(test_id) + " - " + user_id, Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    private void updateUserScore() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("test_id", String.valueOf(test_id));
        params.put("max_score", String.valueOf(total_score_test));

        ScoreAPI.updateScore(
                params,
                this,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        // Xử lý khi thành công
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý khi có lỗi
                        Log.e("API_ERROR", error.toString());
                        Toast.makeText(TestSubmitActivity.this, String.valueOf(test_id) + " - " + user_id, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void getUserInfo() {
        UserApi.getUserSchema(
            this,
            sharedPreClass.getValue_string("token"),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Xử lý khi thành công
                    try {
                        userObject = response.getJSONObject("user");
                        user_id = response.getString("user_id");

                        updateViewUser();
                        saveTestScore();
                        addUserAnswer();

                        updateUserScore();
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
                    Toast.makeText(TestSubmitActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        );


    }

    private void updateViewUser() {
        try {
            if(!userObject.isNull("photo_url")) {
//                avatarImageView.setImageURI(Uri.parse(userObject.getString("photo_url")));

                Glide.with(this.getApplicationContext())
                        .load(userObject.getString("photo_url"))
                        .placeholder(R.drawable.icon_ava2) // hình mặc định nếu chưa có ảnh
                        .error(R.drawable.icon_ava2)         // hình nếu lỗi tải
                        .into(imgAvatar);
            }

            name_text.setText(userObject.getString("full_name"));

            if(!userObject.isNull("gender"))
                gender_text.setText(userObject.getString("gender"));

            if(!userObject.isNull("date_of_birth")) {
                String date = DateUtils.formatDate(userObject.getString("date_of_birth"));
                DOB_text.setText(date);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void scoreTest() {
        AnswerTestMananger.calScore();
        numberQuestion = AnswerTestMananger.getNumberOfQuestion();
        numberWrong = AnswerTestMananger.getNumberOfWrong();

        numberCorrect = AnswerTestMananger.getNumberOfCorrect();
        numberCorrect_Listening = AnswerTestMananger.getNumberOfCorrect_Listening();
        numberCorrect_Reading = AnswerTestMananger.getNumberOfCorrect_Reading();

        // edit score view
        listening_score.setText(numberCorrect_Listening + "/" + (numberQuestion / 2));
        reading_score.setText(numberCorrect_Reading + "/" + (numberQuestion / 2));
        total_score.setText(numberCorrect + "/" + numberQuestion);

        total_score_test = AnswerTestMananger.getScore();
        pass_score.setText(total_score_test + "");

    }

    private void addUserAnswer() {
        // user_id
        // test_id

        //selected_answer
        //is_wrong

        // --

        //question_number
        //question_id
        //group_question_id
        //part_id

        try {
            HashMap<String, Object> params = new HashMap<>();

            params.put("user_id", user_id);
            params.put("test_id", test_id);

            JSONArray answersArray = AnswerTestMananger.getLisJSONArray();

            params.put("answers", answersArray);

            CertificateApi.addUserAnswers(
                params,
                this,
                sharedPreClass.getValue_string("token"),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý khi thành công
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý khi có lỗi
                        Log.e("API_ERROR", error.toString());
                        Toast.makeText(TestSubmitActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void exitActivity() {
        if(bottomDialog_filter != null)
            bottomDialog_filter.dismiss();

        AudioTestManager.releaseAll();
        AnswerTestMananger.releaseAll();

        finish();
    }


//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        finish();
//    }
}
