package com.example.astrolingo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.astrolingo.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.loginregister_load);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(1750);
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }
}
