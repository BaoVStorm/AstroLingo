package com.example.astrolingo.activity.home.retry_wrong_answer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.activity.home.history_test.historyTestMainActivity;
import com.example.astrolingo.apdapter.home.history_test.history_test_adapter;
import com.example.astrolingo.apdapter.home.retry_wrong_answer.retryWrongAnswerAdapter;
import com.example.astrolingo.api.CertificateApi;
import com.example.astrolingo.api.UserAnswerApi;
import com.example.astrolingo.domain.home.history_test.history_test;
import com.example.astrolingo.domain.home.retry_wrong_answer.wrongAnswer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RetryWrongAnswerDetailActivity extends AppCompatActivity{
    private SharedPreferenceClass sharedPreClass;
    private ImageView backIcon;
    private TextView header_title;
    private View header;
    private int part;
    private ListView lv_testResults;
    private ArrayList<wrongAnswer> List_WrongAnswer = new ArrayList<>();

    private retryWrongAnswerAdapter retryWrongAnswerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_retry_wrong_answer_detail);

        part = getIntent().getIntExtra("part", 0);

        // init
        sharedPreClass = new SharedPreferenceClass(this);

        header = findViewById(R.id.header);

        backIcon = header.findViewById(R.id.backIcon);
        header_title = header.findViewById(R.id.header_title);

        String headerTitle = "Wrong anwser | Part " + part;

        //init value
        header_title.setText(headerTitle);

        lv_testResults = findViewById(R.id.lv_testResults);

        // get User Wrong Answer
        getUserWrongAnswer();

        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(v ->{
            finish();
        });
    }

    private void getUserWrongAnswer() {


//        {
//            "msg": "get user answers wrong successfully",
//                "user_answers": [
//                        {
//                            "_id": "68370dbe739cc33508a2cbd2",
//                                "user_id": "6826953fdd0f324c408858d9",
//                                "selected_answer": 3,
//                                "is_wrong": false,
//                                "answered_at": "2025-05-28T13:21:02.226Z",
//                                "question_number": 2,
//                                "question_id": "test1_2",
//                                "group_question_id": "test1_part1_2",
//                                "part_id": 1,
//                                "test_id": 1,
//                                "__v": 0,
//                                "starred_at_vietnam": "28/05/2025 | 23:12:40"
//                        }
//                ]
//        }
        List_WrongAnswer.clear();

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", sharedPreClass.getValue_string("user_id"));
        params.put("part_id", part + "");

        UserAnswerApi.getWrongAnswers(
                params,
                this,
                sharedPreClass.getValue_string("token"),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý khi thành công
                        try {
                            JSONArray jsonArray = response.getJSONArray("user_answers");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject item = jsonArray.getJSONObject(i);

                                int selected_answer = item.getInt("selected_answer");
                                int correct_answer = item.getInt("correct_answer");
                                boolean is_wrong = item.getBoolean("is_wrong");
                                String answered_at = item.getString("starred_at_vietnam");
                                int test_id = item.getInt("test_id");
                                int part_id = item.getInt("part_id");
                                String group_question_id = item.getString("group_question_id");
                                String question_id = item.getString("question_id");
                                int question_number = item.getInt("question_number");
                                String test_title = item.getString("test_title");

                                wrongAnswer answer = new wrongAnswer(
                                        selected_answer,
                                        is_wrong,
                                        answered_at,
                                        test_id,
                                        part_id,
                                        group_question_id,
                                        question_id,
                                        question_number
                                );

                                answer.setCorrect_answer(correct_answer);
                                answer.setTest_title(test_title);

                                List_WrongAnswer.add(answer);
                            }

//                            historyTestAdapter = new history_test_adapter(historyTestMainActivity.this, historyTests);
//                            // historyTestAdapter.setUserId(sharedPreClass.getValue_string("user_id"), sharedPreClass.getValue_string("token"));
//                            lv_testResults.setAdapter(historyTestAdapter);


                            // Cập nhật adapter nếu có
                            retryWrongAnswerAdapter = new retryWrongAnswerAdapter(RetryWrongAnswerDetailActivity.this, List_WrongAnswer);
                            lv_testResults.setAdapter(retryWrongAnswerAdapter);

                        } catch (JSONException e) {
                            Toast.makeText(RetryWrongAnswerDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý khi có lỗi
                        Toast.makeText(RetryWrongAnswerDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}

