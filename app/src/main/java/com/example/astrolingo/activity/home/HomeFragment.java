package com.example.astrolingo.activity.home;

import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.api.UserApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment {
    SharedPreferenceClass sharedPreClass;

    TextView leaderboard_seeMore, headerText;

    LinearLayout first_place_Section, second_place_Section, third_place_Section, fourth_place_Section, fifth_place_Section, sixth_place_Section;

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
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Xử lý khi thành công
                    // Log.d("API_SUCCESS", response.toString());
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        if (!jsonArray.isNull(0)) {
                            // top 1
                            first_place_Section.setVisibility(View.VISIBLE);

                        }
                        else
                            return;

                        if (!jsonArray.isNull(1)) {
                            // top 2
                            second_place_Section.setVisibility(View.VISIBLE);

                        }
                        else
                            return;

                        if (!jsonArray.isNull(2)) {
                            // top 3
                            third_place_Section.setVisibility(View.VISIBLE);

                        }
                        else
                            return;

                        if (!jsonArray.isNull(3)) {
                            // top 4
                            fourth_place_Section.setVisibility(View.VISIBLE);

                        }
                        else
                            return;

                        if (!jsonArray.isNull(4)) {
                            // top 5
                            fifth_place_Section.setVisibility(View.VISIBLE);

                        }
                        else
                            return;

                        if (!jsonArray.isNull(5)) {
                            // top 6
                            sixth_place_Section.setVisibility(View.VISIBLE);

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
}