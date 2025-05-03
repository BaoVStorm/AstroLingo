package com.example.astrolingo.activity.setting;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.astrolingo.R;

public class SettingFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout page_setting vào fragment
        View view = inflater.inflate(R.layout.page_setting, container, false);

        // Gán sự kiện click cho nút "Chỉnh sửa hồ sơ"
        LinearLayout btnChinhSuaHoSo = view.findViewById(R.id.btnChinhSuaHoSo);
        btnChinhSuaHoSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đổi màu nền của nút
                btnChinhSuaHoSo.setBackgroundColor(Color.parseColor("#9F7EFF"));


                // Mở activity ChinhSuaHoSoActivity
                Intent intent = new Intent(getActivity(), ChinhSuaHoSoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
