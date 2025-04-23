package com.example.astrolingo.activity.loginRegister;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.astrolingo.R;

import com.example.astrolingo.activity.ai.*;
import com.example.astrolingo.activity.course.*;
import com.example.astrolingo.activity.home.*;
import com.example.astrolingo.activity.setting.*;
import com.example.astrolingo.activity.test.*;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class loginRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }
}
