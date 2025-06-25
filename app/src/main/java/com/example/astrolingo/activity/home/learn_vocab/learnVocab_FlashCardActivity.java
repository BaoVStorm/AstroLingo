package com.example.astrolingo.activity.home.learn_vocab;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.apdapter.home.learn_word.learn_word_flashcard_adapter;
import com.example.astrolingo.apdapter.test.TestDetailAdapter;
import com.example.astrolingo.domain.home.learn_word.vocabulary;

import java.util.ArrayList;

public class learnVocab_FlashCardActivity extends AppCompatActivity {

    private SharedPreferenceClass sharedPreClass;
    private ClipboardManager clipboard;

    private ImageView backIcon;
    private ViewPager2 viewPager;
    private learn_word_flashcard_adapter learnWordFlashcardAdapter;
    private ArrayList<vocabulary> list_words;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_word_learn_flashcard);

        list_words = (ArrayList<vocabulary>) getIntent().getSerializableExtra("list_words");

        Log.d("list_words", list_words.toString());

        // init
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        sharedPreClass = new SharedPreferenceClass(this);

        backIcon = findViewById(R.id.backIcon);
        viewPager = findViewById(R.id.viewPager);


        // init Value
        learnWordFlashcardAdapter = new learn_word_flashcard_adapter(this, list_words, viewPager);
        learnWordFlashcardAdapter.setUserId(sharedPreClass.getValue_string("user_id"), sharedPreClass.getValue_string("token"));
        viewPager.setAdapter(learnWordFlashcardAdapter);



        backIcon.setOnClickListener(v->{
            finish();
        });

    }

}
