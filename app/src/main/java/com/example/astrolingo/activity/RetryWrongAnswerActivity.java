package com.example.astrolingo.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.astrolingo.R;

public class RetryWrongAnswerActivity extends AppCompatActivity{
    private ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_retry_wrong_answer);

        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(v ->{
            Log.d("RetryWrongAnswerActivity", "Back clicked - finishing activity");
            finish();
        });
    }
}

