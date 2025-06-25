package com.example.astrolingo.activity;
// thêm vào
//

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.astrolingo.R;

import com.example.astrolingo.activity.ai.*;
import com.example.astrolingo.activity.course.*;
import com.example.astrolingo.activity.home.*;
import com.example.astrolingo.activity.setting.*;
import com.example.astrolingo.activity.test.*;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    TextView header;
    private BottomNavigationView bottomNav;
    private FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.format_bottomNav);
        frame = findViewById(R.id.format_frameLayout);
        header = findViewById(R.id.format_textView);

        // navigate bottom nav
        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navHome) {
                    header.setText(getString(R.string.homeFragment));
                    loadFragment(new HomeFragment(), false);
                    return true;
                } else if (id == R.id.navTest) {
                    header.setText(getString(R.string.testFragment));
                    loadFragment(new TestFragment(), false);
                    return true;
                } else if (id == R.id.navAI) {
                    header.setText(getString(R.string.AiFragment));
                    loadFragment(new AiFragment(), false);
                    return true;
                } else if (id == R.id.navSetting) {
                    header.setText(getString(R.string.settingFragment));
                    loadFragment(new SettingFragment(), false);
                    return true;
                }

//                else if (id == R.id.navCourse) {
//                    header.setText(getString(R.string.courseFragment));
//                    loadFragment(new CourseFragment(), false);
//                    return true;
//                }

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


//    private void loadFragment(Fragment fragment, boolean isInit) {
//        if(isInit) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.format_frameLayout, fragment)
//                    .commit();
//
//            return;
//        }
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.format_frameLayout, fragment)
//                .commit();
//
//    }
    public void navigateTo(Fragment fragment, int headerResId, int navItemId, boolean isInit) {
        header.setText(getString(headerResId));
        bottomNav.setSelectedItemId(navItemId); // cập nhật giao diện nav
        loadFragment(fragment, isInit);
    }

    public void loadFragment(Fragment fragment, boolean isInit) {
        String tag = fragment.getClass().getSimpleName();
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Ẩn tất cả fragment đang hiện
        for (Fragment f : fragmentManager.getFragments()) {
            if (f != null && f.isVisible()) {
                fragmentManager.beginTransaction().hide(f).commit();
            }
        }

        // Kiểm tra nếu fragment đã được add trước đó
        Fragment existingFragment = fragmentManager.findFragmentByTag(tag);

        if (existingFragment != null) {
            fragmentManager.beginTransaction().show(existingFragment).commit();
        } else {
            fragmentManager.beginTransaction()
                    .add(R.id.format_frameLayout, fragment, tag)
                    .commit();
        }
    }
}