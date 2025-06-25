package com.example.astrolingo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.astrolingo.R;

import java.util.Objects;

public class SuccessActivity extends AppCompatActivity {

    TextView header_text, enter_textview;
    ProgressBar progressBar;
    ConstraintLayout button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.successful);

        enter_textview = findViewById(R.id.enter_textview);
        header_text = findViewById(R.id.header_text);
        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.button);

        // init
        if(Objects.equals(getIntent().getStringExtra("text"), "password")) {
            enter_textview.setText(getString(R.string.resetpassword_success));
            header_text.setText(getString(R.string.forgotPasswordPage));
        }


        progressBar.setVisibility(View.GONE);

        button.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            startActivity(new Intent(SuccessActivity.this, LoginActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
