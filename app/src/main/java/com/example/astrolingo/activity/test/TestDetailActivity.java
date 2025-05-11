package com.example.astrolingo.activity.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.astrolingo.R;

public class TestDetailActivity extends AppCompatActivity {
    private TextView testTitle, testTime, testQuestions;
    private ImageView backIcon;

    ConstraintLayout button_forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_test_prepare_start_test);

        testTitle = findViewById(R.id.testTitle);
        testTime = findViewById(R.id.testTime);
        testQuestions = findViewById(R.id.testQuestions);
        backIcon = findViewById(R.id.backIcon);
        button_forgotPassword = findViewById(R.id.button_forgotPassword);

        PageTestMainItem test = (PageTestMainItem) getIntent().getSerializableExtra("testItem");

        if(test != null){
            testTitle.setText(test.getTitle().replace("\n", " "));
            testTime.setText("Thời gian: " + test.getDurationMinutesOfTest() + " phút");
            testQuestions.setText("Số câu hỏi: " + test.getNumberOfQuestions());
        }

        button_forgotPassword.setOnClickListener(v ->{
            startActivity(new Intent(TestDetailActivity.this, TestDetailMainActivity.class));

//            finish();
        });

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
