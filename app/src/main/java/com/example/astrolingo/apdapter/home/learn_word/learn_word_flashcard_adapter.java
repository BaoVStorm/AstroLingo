package com.example.astrolingo.apdapter.home.learn_word;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.astrolingo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.astrolingo.apdapter.test.TestDetailAdapter;
import com.example.astrolingo.domain.home.learn_word.vocabulary;


public class learn_word_flashcard_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;
    private final List<vocabulary> vocabularyList;
    private ViewPager2 viewPager;

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        View cardFront, cardBack;
        TextView front_word_text, second_word, front_pronunciation_text, front_fullPage, second_fullPage;
        TextView second_word_meaning_english, second_word_meaning_vietnamese;
        TextView second_example_meaning_english, second_example_meaning_vietnamese;
        ImageView imageQuestion;
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
        }
    }

    public learn_word_flashcard_adapter(Context context, List<vocabulary> vocabularyList, ViewPager2 viewPager) {
        this.context = context;
        this.vocabularyList = vocabularyList;
        this.viewPager = viewPager;
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
            } else {
                frontAnim.setTarget(viewHolder.cardBack);
                backAnim.setTarget(viewHolder.cardFront);
                frontAnim.start();
                backAnim.start();
            }
            viewHolder.isFront = !viewHolder.isFront;
        });

        // init value
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


    }

    @Override
    public int getItemCount() {
        return vocabularyList.size();
    }

/*

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_word_learn_flashcard_adapter);

        // init
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        sharedPreClass = new SharedPreferenceClass(this);


        View card_front = findViewById(R.id.font_card);
        View card_back = findViewById(R.id.second_card);

        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        card_front.setCameraDistance(8000 * scale);
        card_back.setCameraDistance(8000 * scale);

        AnimatorSet frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.front_card_animator);
        AnimatorSet backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.back_card_animator);

        card_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard(card_front, card_back, frontAnim, backAnim);
            }
        });

        card_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard(card_front, card_back, frontAnim, backAnim);
            }
        });


//        backIcon.setOnClickListener(v->{
//            dialog_level.dismiss();
//            bottomDialog_topic.dismiss();
//            finish();
//        });

    }

    private void flipCard(View card_front, View card_back, AnimatorSet frontAnim, AnimatorSet backAnim) {
        if (isFront) {
            frontAnim.setTarget(card_front);
            backAnim.setTarget(card_back);
            frontAnim.start();
            backAnim.start();
            isFront = false;
        } else {
            frontAnim.setTarget(card_back);
            backAnim.setTarget(card_front);
            frontAnim.start();
            backAnim.start();
            isFront = true;
        }
    }
*/


}

