package com.example.astrolingo.activity.home.retry_wrong_answer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;

public class RetryWrongAnswerMainActivity extends AppCompatActivity{
    private SharedPreferenceClass sharedPreClass;
    private ImageView backIcon;
    private TextView header_title;
    private View header;

    private CardView icon_picture_description_CardView;
    private CardView icon_question_and_answer_CardView;
    private CardView icon_conversation_CardView;
    private CardView icon_short_talk_CardView;
    private CardView icon_fill_the_blank_CardView;
    private CardView icon_fill_the_paragraph_CardView;
    private CardView icon_single_paragraph_CardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_retry_wrong_answer);

        // init
        sharedPreClass = new SharedPreferenceClass(this);

        header = findViewById(R.id.header);

        backIcon = header.findViewById(R.id.backIcon);
        header_title = header.findViewById(R.id.header_title);
        header_title.setText(getString(R.string.rewatch_wrong_answer));

        //init cardview
        icon_picture_description_CardView = findViewById(R.id.icon_picture_description_CardView);
        icon_picture_description_CardView.setOnClickListener(v -> {
            Intent intent = new Intent(RetryWrongAnswerMainActivity.this, RetryWrongAnswerDetailActivity.class);
            intent.putExtra("part", 1);
            startActivity(intent);
        });

        icon_question_and_answer_CardView = findViewById(R.id.icon_question_and_answer_CardView);
        icon_question_and_answer_CardView.setOnClickListener(v -> {
            Intent intent = new Intent(RetryWrongAnswerMainActivity.this, RetryWrongAnswerDetailActivity.class);
            intent.putExtra("part", 2);
            startActivity(intent);
        });

        icon_conversation_CardView = findViewById(R.id.icon_conversation_CardView);
        icon_conversation_CardView.setOnClickListener(v -> {
            Intent intent = new Intent(RetryWrongAnswerMainActivity.this, RetryWrongAnswerDetailActivity.class);
            intent.putExtra("part", 3);
            startActivity(intent);
        });

        icon_short_talk_CardView = findViewById(R.id.icon_short_talk_CardView);
        icon_short_talk_CardView.setOnClickListener(v -> {
            Intent intent = new Intent(RetryWrongAnswerMainActivity.this, RetryWrongAnswerDetailActivity.class);
            intent.putExtra("part", 4);
            startActivity(intent);
        });

        icon_fill_the_blank_CardView = findViewById(R.id.icon_fill_the_blank_CardView);
        icon_fill_the_blank_CardView.setOnClickListener(v -> {
            Intent intent = new Intent(RetryWrongAnswerMainActivity.this, RetryWrongAnswerDetailActivity.class);
            intent.putExtra("part", 5);
            startActivity(intent);
        });

        icon_fill_the_paragraph_CardView = findViewById(R.id.icon_fill_the_paragraph_CardView);
        icon_fill_the_paragraph_CardView.setOnClickListener(v -> {
            Intent intent = new Intent(RetryWrongAnswerMainActivity.this, RetryWrongAnswerDetailActivity.class);
            intent.putExtra("part", 6);
            startActivity(intent);
        });

        icon_single_paragraph_CardView = findViewById(R.id.icon_single_paragraph_CardView);
        icon_single_paragraph_CardView.setOnClickListener(v -> {
            Intent intent = new Intent(RetryWrongAnswerMainActivity.this, RetryWrongAnswerDetailActivity.class);
            intent.putExtra("part", 7);
            startActivity(intent);
        });

        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(v ->{
            Log.d("RetryWrongAnswerActivity", "Back clicked - finishing activity");
            finish();
        });
    }
}

