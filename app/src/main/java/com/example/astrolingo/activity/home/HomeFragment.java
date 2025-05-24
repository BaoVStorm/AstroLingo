package com.example.astrolingo.activity.home;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.activity.test.TestSubmitActivity;
import com.example.astrolingo.api.UserApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.astrolingo.activity.RetryWrongAnswerActivity;


public class HomeFragment extends Fragment {
    SharedPreferenceClass sharedPreClass;

    TextView leaderboard_seeMore, headerText;

    LinearLayout first_place_Section, second_place_Section, third_place_Section, fourth_place_Section, fifth_place_Section, sixth_place_Section;
    CardView vocab_search;

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

        // Khởi tạo SharedPreferenceClass
        sharedPreClass = new SharedPreferenceClass(view.getContext());

        vocab_search = view.findViewById(R.id.icon_vocab_search_CardView);
        vocab_search.setOnClickListener(v-> {
           startActivity(new Intent(getActivity(), TestSubmitActivity.class));
        });

        // Get 5 user section
        first_place_Section = view.findViewById(R.id.first_place_Section);
        second_place_Section = view.findViewById(R.id.second_place_Section);
        third_place_Section = view.findViewById(R.id.third_place_Section);
        fourth_place_Section = view.findViewById(R.id.fourth_place_Section);
        fifth_place_Section = view.findViewById(R.id.fifth_place_Section);
        sixth_place_Section = view.findViewById(R.id.sixth_place_Section);


        setUpLeaderBoard(view);

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

    private void setUpLeaderBoard(View view) {
        first_place_Section.setVisibility(View.GONE);
        second_place_Section.setVisibility(View.GONE);
        third_place_Section.setVisibility(View.GONE);
        fourth_place_Section.setVisibility(View.GONE);
        fifth_place_Section.setVisibility(View.GONE);
        sixth_place_Section.setVisibility(View.GONE);

        UserApi.getTopScore(
            view.getContext(),
            sharedPreClass.getValue_string("token"),
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    // Xử lý khi thành công
                    // Log.d("API_SUCCESS", response.toString());
                    try {

                        View[] sections = {
                                first_place_Section, second_place_Section, third_place_Section,
                                fourth_place_Section, fifth_place_Section, sixth_place_Section
                        };

                        int[] nameIds = {
                                R.id.top1_name, R.id.top2_name, R.id.top3_name,
                                R.id.top4_name, R.id.top5_name, R.id.top6_name
                        };

                        int[] scoreIds = {
                                R.id.top1_score, R.id.top2_score, R.id.top3_score,
                                R.id.top4_score, R.id.top5_score, R.id.top6_score
                        };

                        int[] iconIds = {
                                R.id.top1_icon, R.id.top2_icon, R.id.top3_icon,
                                R.id.icon_top4, R.id.icon_top5, R.id.icon_top6
                        };

                        for (int nameId : nameIds) {
                            view.findViewById(nameId).setVisibility(View.GONE);
                        }

                        for (int i = 0; i < jsonArray.length() && i < 6; i++) {
                            sections[i].setVisibility(View.VISIBLE);

                            if (jsonArray.isNull(i))
                                continue;

                            JSONObject user = jsonArray.getJSONObject(i);

                            TextView nameView = view.findViewById(nameIds[i]);
                            TextView scoreView = view.findViewById(scoreIds[i]);
                            ImageView iconView = view.findViewById(iconIds[i]);

                            nameView.setVisibility(View.VISIBLE);
                            nameView.setText(user.getString("full_name"));
                            scoreView.setText(String.valueOf(user.getInt("score")));

                            if (!user.isNull("photo_url")) {
                                iconView.setImageURI(Uri.parse(user.getString("photo_url")));
                            }
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Xử lý khi có lỗi
                    Log.e("API_ERROR", error.toString());
                    Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        );



    }

    public String getType() {
        return "home";
    }

}