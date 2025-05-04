package com.example.astrolingo.activity.home;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.astrolingo.R;


public class HomeFragment extends Fragment {
    TextView leaderboard_seeMore, headerText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // init profile
        if (getActivity() != null) {
            headerText = getActivity().findViewById(R.id.format_textView);
            if (headerText != null) {
                headerText.setText(getString(R.string.homeFragment));
            }
        }

        // init leaderboard
        leaderboard_seeMore = view.findViewById(R.id.leaderboard_see_more);
        leaderboard_seeMore.setPaintFlags(leaderboard_seeMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        return view;
    }
}