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

            testTitle.setText(testObject.getString("title"));
            testTime.setText("Thời gian: " + testObject.getInt("test_time") + " phút");
            testQuestions.setText("Số câu hỏi: " + testObject.getInt("question_number"));

            button_forgotPassword.setOnClickListener(v ->{
                Intent intent = new Intent(TestDetailActivity.this, TestDetailMainActivity.class);
                intent.putExtra("testObject", testObject.toString());

                startActivity(intent);
                finish();
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

        backIcon.setOnClickListener(v ->{
            finish();
        });
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        finish();
//    }
}
