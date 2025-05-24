package com.example.astrolingo.activity.home;

import android.graphics.Paint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.astrolingo.R;
import com.example.astrolingo.activity.RetryWrongAnswerActivity;


public class HomeFragment extends Fragment {

    TextView leaderboard_seeMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // init leaderboard
        leaderboard_seeMore = view.findViewById(R.id.leaderboard_see_more);
        leaderboard_seeMore.setPaintFlags(leaderboard_seeMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        CardView cardview = view.findViewById(R.id.icon_wrong_answer_CardView);
        cardview.setOnClickListener(v ->{
           Intent intent = new Intent(getContext(), RetryWrongAnswerActivity.class);
           startActivity(intent);
        });

        return view;
    }
}