package com.example.astrolingo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.astrolingo.R;

//import com.chaos.view.PinView;


public class FogotPasswordActivity extends AppCompatActivity {
//    PinView pinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.forgotpassword);
    }
}

