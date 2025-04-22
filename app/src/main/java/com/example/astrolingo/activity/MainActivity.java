package com.example.astrolingo.activity;
// thêm vào
//

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

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.format_bottomNav);
        frame = findViewById(R.id.format_frameLayout);

        // navigate bottom nav
        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navHome) {
                    loadFragment(new HomeFragment(), false);
                    return true;
                } else if (id == R.id.navTest) {
                    loadFragment(new TestFragment(), false);
                    return true;
                } else if (id == R.id.navCourse) {
                    loadFragment(new CourseFragment(), false);
                    return true;
                } else if (id == R.id.navAI) {
                    loadFragment(new AiFragment(), false);
                    return true;
                } else if (id == R.id.navSetting) {
                    loadFragment(new SettingFragment(), false);
                    return true;
                }

                return false;
            }
        });

        // mặc định load trang home Page
        loadFragment(new HomeFragment(), true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });
    }


    private void loadFragment(Fragment fragment, boolean isInit) {
        if(isInit) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.format_frameLayout, fragment)
                    .commit();

            return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.format_frameLayout, fragment)
                .commit();

    }
}