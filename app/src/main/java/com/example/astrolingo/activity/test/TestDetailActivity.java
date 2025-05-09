package com.example.astrolingo.activity.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.astrolingo.R;

public class TestDetailActivity extends AppCompatActivity {
    private TextView testTitle, testTime, testQuestions;
    private ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_test_prepare_start_test);

        testTitle = findViewById(R.id.testTitle);
        testTime = findViewById(R.id.testTime);
        testQuestions = findViewById(R.id.testQuestions);
        backIcon = findViewById(R.id.backIcon);

        PageTestMainItem test = (PageTestMainItem) getIntent().getSerializableExtra("testItem");

        if(test != null){
            testTitle.setText(test.getTitle().replace("\n", " "));
            testTime.setText("Thời gian: " + test.getDurationMinutesOfTest() + " phút");
            testQuestions.setText("Số câu hỏi: " + test.getNumberOfQuestions());
        }

        backIcon.setOnClickListener(v ->{
            finish();
        });

    }
}
