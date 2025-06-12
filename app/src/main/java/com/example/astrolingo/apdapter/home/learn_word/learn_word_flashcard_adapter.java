package com.example.astrolingo.apdapter.home.learn_word;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.astrolingo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.astrolingo.Service.AudioManager;
import com.example.astrolingo.apdapter.test.TestDetailAdapter;
import com.example.astrolingo.api.UserStarApi;
import com.example.astrolingo.domain.home.learn_word.vocabulary;
import com.example.astrolingo.function.AudioListener;

import org.json.JSONObject;


public class learn_word_flashcard_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;
    private final List<vocabulary> vocabularyList;
    private ViewPager2 viewPager;
    private String user_id, token;

    public learn_word_flashcard_adapter(Context context, List<vocabulary> vocabularyList, ViewPager2 viewPager) {
        this.context = context;
        this.vocabularyList = vocabularyList;
        this.viewPager = viewPager;
    }

    public void setUserId(String user_id, String token) {
        this.user_id = user_id;
        this.token = token;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        View cardFront, cardBack;
        TextView front_word_text, second_word, front_pronunciation_text, front_fullPage, second_fullPage;
        TextView second_word_meaning_english, second_word_meaning_vietnamese;
        TextView front_currentPage, second_currentPage;
        TextView second_example_meaning_english, second_example_meaning_vietnamese;
        ImageView imageQuestion;
        ImageView front_icon_loud, second_icon_loud;
        ImageView front_icon_mark, second_icon_mark;
        boolean isFront = true;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardFront = itemView.findViewById(R.id.font_card);
            cardBack = itemView.findViewById(R.id.second_card);
            front_word_text = itemView.findViewById(R.id.front_word_text);
            front_pronunciation_text = itemView.findViewById(R.id.front_pronunciation_text);

            front_fullPage = itemView.findViewById(R.id.front_fullPage);
            second_fullPage = itemView.findViewById(R.id.second_fullPage);

            second_word = itemView.findViewById(R.id.second_word);

            second_word_meaning_english = itemView.findViewById(R.id.second_word_meaning_english);
            second_word_meaning_vietnamese = itemView.findViewById(R.id.second_word_meaning_vietnamese);

            second_example_meaning_english = itemView.findViewById(R.id.second_example_meaning_english);
            second_example_meaning_vietnamese = itemView.findViewById(R.id.second_example_meaning_vietnamese);

            imageQuestion = itemView.findViewById(R.id.imageQuestion);

            front_currentPage = itemView.findViewById(R.id.front_currentPage);
            second_currentPage = itemView.findViewById(R.id.second_currentPage);

            front_icon_loud = itemView.findViewById(R.id.front_icon_loud);
            second_icon_loud = itemView.findViewById(R.id.second_icon_loud);

            front_icon_mark = itemView.findViewById(R.id.front_icon_mark);
            second_icon_mark = itemView.findViewById(R.id.second_icon_mark);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.act_word_learn_flashcard_adapter, parent, false);
        return new CardViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        vocabulary word = vocabularyList.get(position);

        CardViewHolder viewHolder = (CardViewHolder) holder;

        // Animation setup
        float scale = context.getResources().getDisplayMetrics().density;
        viewHolder.cardFront.setCameraDistance(8000 * scale);
        viewHolder.cardBack.setCameraDistance(8000 * scale);

        AnimatorSet frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.front_card_animator);
        AnimatorSet backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.back_card_animator);

        viewHolder.itemView.setOnClickListener(v -> {
            if (viewHolder.isFront) {
                frontAnim.setTarget(viewHolder.cardFront);
                backAnim.setTarget(viewHolder.cardBack);
                frontAnim.start();
                backAnim.start();

                // fix lỗi khi load backCard nhắn vào loud thì load loud của front
                viewHolder.front_icon_loud.setClickable(false);
            } else {
                frontAnim.setTarget(viewHolder.cardBack);
                backAnim.setTarget(viewHolder.cardFront);
                frontAnim.start();
                backAnim.start();

                // fix lỗi khi load backCard nhắn vào loud thì load loud của front
                viewHolder.front_icon_loud.setClickable(true);
            }
            viewHolder.isFront = !viewHolder.isFront;
        });

        // init value
        viewHolder.front_currentPage.setText((position + 1) + "");
        viewHolder.second_currentPage.setText((position + 1) + "");

        viewHolder.front_word_text.setText(word.getWord());
        viewHolder.second_word.setText(word.getWord());

        viewHolder.front_pronunciation_text.setText(word.getPronunciation());

        viewHolder.front_fullPage.setText(vocabularyList.size() + "");
        viewHolder.second_fullPage.setText(vocabularyList.size() + "");

        String meaningEnglish = word.getMeaningEnglish();
        if(meaningEnglish.isEmpty())
            viewHolder.second_word_meaning_english.setVisibility(View.GONE);
        else {
            viewHolder.second_word_meaning_english.setVisibility(View.VISIBLE);
            viewHolder.second_word_meaning_english.setText(meaningEnglish);
        }

        String meaningVietnamese = word.getMeaningVietnamese();
        if(meaningVietnamese.isEmpty())
            viewHolder.second_word_meaning_vietnamese.setVisibility(View.GONE);
        else {
            viewHolder.second_word_meaning_vietnamese.setVisibility(View.VISIBLE);
            viewHolder.second_word_meaning_vietnamese.setText(meaningVietnamese);
        }

        String exampleEnglish = word.getExampleEnglish();
        if(exampleEnglish.isEmpty())
            viewHolder.second_example_meaning_english.setVisibility(View.GONE);
        else {
            viewHolder.second_example_meaning_english.setVisibility(View.VISIBLE);
            viewHolder.second_example_meaning_english.setText(exampleEnglish);
        }

        String exampleVietnamese = word.getExampleVietnamese();
        if(exampleVietnamese.isEmpty())
            viewHolder.second_example_meaning_vietnamese.setVisibility(View.GONE);
        else {
            viewHolder.second_example_meaning_vietnamese.setVisibility(View.VISIBLE);
            viewHolder.second_example_meaning_vietnamese.setText(exampleVietnamese);
        }

        // set image
        String imageUrl = word.getImageUrl();

        if(imageUrl.isEmpty())
            viewHolder.imageQuestion.setVisibility(View.GONE);
        else {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                viewHolder.imageQuestion.setVisibility(View.VISIBLE);

                Glide.with(context)
                    .load(imageUrl)
                    .error((Drawable) null) // nếu không muốn ảnh lỗi
                    .into(viewHolder.imageQuestion);
            } else {
                viewHolder.imageQuestion.setVisibility(View.GONE); // ẩn ImageView nếu không có ảnh
            }
        }

        // set audio
        viewHolder.front_icon_loud.setImageResource(R.drawable.icon_asset_loud1);

        viewHolder.front_icon_loud.setOnClickListener(v -> {
            if(!AudioManager.isPlaying()) {
                AudioManager.setAudio(context , word.getAudioUrl(), new AudioListener() {
                    @Override
                    public void onPrepared() {
                        viewHolder.front_icon_loud.setImageResource(R.drawable.icon_asset_loud3);
                    }

                    @Override
                    public void onCompletion() {
                        viewHolder.front_icon_loud.setImageResource(R.drawable.icon_asset_loud1);
                    }
                    @Override
                    public void onError() {
                        Log.e("AUDIO", "Error during playback");
                    }
                });
            }
        });

        viewHolder.second_icon_loud.setImageResource(R.drawable.icon_asset_loud1);

        viewHolder.second_icon_loud.setOnClickListener(v -> {
            if(!AudioManager.isPlaying()) {
                AudioManager.setAudio(context , word.getAudioUrl(), new AudioListener() {
                    @Override
                    public void onPrepared() {
                        viewHolder.second_icon_loud.setImageResource(R.drawable.icon_asset_loud3);
                    }

                    @Override
                    public void onCompletion() {
                        viewHolder.second_icon_loud.setImageResource(R.drawable.icon_asset_loud1);
                    }
                    @Override
                    public void onError() {
                        Log.e("AUDIO", "Error during playback");
                    }
                });
            }
        });

        // init star
        if(word.getIsStar()) {
            viewHolder.front_icon_mark.setImageResource(R.drawable.icon_assets_star_check);
            viewHolder.second_icon_mark.setImageResource(R.drawable.icon_assets_star_check);
        }
        else {
            viewHolder.front_icon_mark.setImageResource(R.drawable.icon_assets_star_uncheck);
            viewHolder.second_icon_mark.setImageResource(R.drawable.icon_assets_star_uncheck);
        }

        // set mark
        markStar(viewHolder.front_icon_mark, viewHolder, word);
        markStar(viewHolder.second_icon_mark, viewHolder, word);
    }

    private void markStar(ImageView icon_mark, CardViewHolder viewHolder, vocabulary word) {
        icon_mark.setOnClickListener(v-> {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", user_id);
            params.put("type_star", "vocabulary");
            params.put("vocab_id", word.getVocabId());

            if(word.getIsStar()) {
                // remove star

                UserStarApi.removeWordUserStars(
                    params,
                    context,
                    token,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Xử lý khi thành công
                            // Toast.makeText(getContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();

                            viewHolder.front_icon_mark.setImageResource(R.drawable.icon_assets_star_uncheck);
                            viewHolder.second_icon_mark.setImageResource(R.drawable.icon_assets_star_uncheck);
                            word.setIsStar(false);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý khi có lỗi
                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                );
            }
            else {
                // add star

                UserStarApi.addWordUserStars(
                    params,
                    context,
                    token,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Xử lý khi thành công
                            // Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();

                            viewHolder.front_icon_mark.setImageResource(R.drawable.icon_assets_star_check);
                            viewHolder.second_icon_mark.setImageResource(R.drawable.icon_assets_star_check);
                            word.setIsStar(true);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý khi có lỗi
                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return vocabularyList.size();
    }

}

