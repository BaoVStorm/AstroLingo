package com.example.astrolingo.activity.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.astrolingo.R;
import com.example.astrolingo.activity.RetryWrongAnswerActivity;


public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        CardView cardview = view.findViewById(R.id.icon_wrong_answer_CardView);
        cardview.setOnClickListener(v ->{
           Intent intent = new Intent(getContext(), RetryWrongAnswerActivity.class);
           startActivity(intent);
        });

        return view;
    }
}