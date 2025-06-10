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
import com.bumptech.glide.Glide;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.activity.MainActivity;
import com.example.astrolingo.activity.ai.AiFragment;
import com.example.astrolingo.activity.home.game_hangman.GameActivity;
import com.example.astrolingo.activity.home.game_hangman.StartGame;
import com.example.astrolingo.activity.home.game_quiz.QuizGameActivity;
import com.example.astrolingo.activity.home.game_quiz.StartGameWordAttack;
import com.example.astrolingo.activity.home.history.historyWordTranslatedMainActivity;
import com.example.astrolingo.activity.home.history_test.historyTestMainActivity;
import com.example.astrolingo.activity.home.learn_vocab.learnVocabMainActivity;
import com.example.astrolingo.activity.home.my_words.myWordMainActivity;
import com.example.astrolingo.activity.home.translate.translateMainActivity;
import com.example.astrolingo.api.UserApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.astrolingo.activity.home.retry_wrong_answer.RetryWrongAnswerMainActivity;


public class HomeFragment extends Fragment {
    SharedPreferenceClass sharedPreClass;

    TextView leaderboard_seeMore, headerText;

    LinearLayout first_place_Section, second_place_Section, third_place_Section, fourth_place_Section, fifth_place_Section, sixth_place_Section;
    CardView vocab_search, icon_lookuped_word_CardView, icon_learn_vocab_CardView, icon_word_of_you_CardView, icon_test_history_CardView, icon_chatbot_CardView;

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
           startActivity(new Intent(getActivity(), translateMainActivity.class));
        });

        icon_lookuped_word_CardView = view.findViewById(R.id.icon_lookuped_word_CardView);
        icon_lookuped_word_CardView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), historyWordTranslatedMainActivity.class);
            startActivity(intent);
        });

        icon_learn_vocab_CardView = view.findViewById(R.id.icon_learn_vocab_CardView);
        icon_learn_vocab_CardView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), learnVocabMainActivity.class);
            startActivity(intent);
        });

        icon_word_of_you_CardView = view.findViewById(R.id.icon_word_of_you_CardView);
        icon_word_of_you_CardView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), myWordMainActivity.class);
            startActivity(intent);
        });

        icon_test_history_CardView = view.findViewById(R.id.icon_test_history_CardView);
        icon_test_history_CardView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), historyTestMainActivity.class);
            startActivity(intent);
        });

        icon_chatbot_CardView = view.findViewById(R.id.icon_chatbot_CardView);
        icon_chatbot_CardView.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateTo(
                        new AiFragment(),
                        R.string.AiFragment,
                        R.id.navAI,
                        false
                );
            }
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
            Intent intent = new Intent(getContext(), RetryWrongAnswerMainActivity.class);
            startActivity(intent);
        });



        // game_hangman
        CardView hangmanCard = view.findViewById(R.id.icon_hangman_CardView);
        hangmanCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StartGame.class);
            startActivity(intent);
        });

        //icon_word_attack_CardView
        CardView quizgameCard = view.findViewById(R.id.icon_word_attack_CardView);
        quizgameCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StartGameWordAttack.class);
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

                                // iconView.setImageURI(Uri.parse(user.getString("photo_url")));

                                Glide.with(view.getContext())
                                        .load(user.getString("photo_url"))
                                        .placeholder(R.drawable.icon_ava2) // hình mặc định nếu chưa có ảnh
                                        .error(R.drawable.icon_ava2)         // hình nếu lỗi tải
                                        .into(iconView);
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