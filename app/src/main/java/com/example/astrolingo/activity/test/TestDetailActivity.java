package com.example.astrolingo.activity.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.AnswerTestMananger;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.api.TestApi;
import com.example.astrolingo.domain.test.nav_answer;
import com.example.astrolingo.function.StringManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestDetailActivity extends AppCompatActivity {
    private SharedPreferenceClass sharedPreClass;
    private TextView testTitle, testTime, testQuestions;
    private ImageView backIcon;

    ConstraintLayout button_forgotPassword;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_test_prepare_start_test);

        sharedPreClass = new SharedPreferenceClass(this);

        testTitle = findViewById(R.id.testTitle);
        testTime = findViewById(R.id.testTime);
        testQuestions = findViewById(R.id.testQuestions);
        backIcon = findViewById(R.id.backIcon);
        button_forgotPassword = findViewById(R.id.button_forgotPassword);

        String testString = getIntent().getStringExtra("testObject");

        try {
            JSONObject testObject = new JSONObject(testString);

            int test_id = testObject.getInt("test_id");

            testTitle.setText(testObject.getString("title"));
            testTime.setText("Thời gian: " + testObject.getInt("test_time") + " phút");
            testQuestions.setText("Số câu hỏi: " + testObject.getInt("question_number"));

            button_forgotPassword.setOnClickListener(v ->{
                getAndAddQuestionOfTest(test_id);

                Intent intent = new Intent(TestDetailActivity.this, TestDetailMainActivity.class);
                intent.putExtra("testObject", testObject.toString());

                startActivity(intent);
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

        backIcon.setOnClickListener(v ->{
            finish();
        });
    }

    private void getAndAddQuestionOfTest(int test_id) {

//        [
//            {
//                "_id": "68206440a87bed1c0357e798",
//                "question_id": "test1_1",
//                "group_question_id": "test1_part1_1",
//                "question_text": "Look at the picture marked number 1 in your test book",
//                "correct_answer": 4,
//                "ans_1": "A",
//                "ans_2": "B",
//                "ans_3": "C",
//                "ans_4": "D"
//            }
//        ]
        AnswerTestMananger.releaseAll();
        AnswerTestMananger.list_answer.add(new nav_answer(0, 0));

        TestApi.getListQuestionByTestId(test_id,
            this,
            sharedPreClass.getValue_string("token"),
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    // Xử lý khi thành công
                    try {

                        for(int i = 0; i < jsonArray.length(); i++) {
                            if(jsonArray.isNull(i)) {
                                throw new JSONException("Null value");
                            }

                            JSONObject object = jsonArray.getJSONObject(i);

                            int question_id = StringManager.extractLastNumber(object.getString("question_id"));
                            String part_id = StringManager.extractSecondPart(object.getString("group_question_id"));
                            int count_ans = 0;
                            int correct_ans = 1;

                            if(!object.isNull("ans_1"))
                                count_ans++;

                            if(!object.isNull("ans_2"))
                                count_ans++;

                            if(!object.isNull("ans_3"))
                                count_ans++;

                            if(!object.isNull("ans_4"))
                                count_ans++;

                            if(!object.isNull("correct_answer"))
                                correct_ans = object.getInt("correct_answer");

                            nav_answer navAnswer = new nav_answer(0, count_ans);
                            navAnswer.setCorrectAnswer(correct_ans);
                            navAnswer.setInfo(part_id, question_id);
//                            AnswerTestMananger.map_answer.put(question_id, navAnswer);
                            AnswerTestMananger.list_answer.add(navAnswer);
                        }

                    } catch (JSONException e) {
                        Log.e("error", e.toString());
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


//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        finish();
//    }
}
