package com.example.astrolingo.activity.home.translate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.AnswerTestMananger;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.activity.test.TestDetailMainActivity;
import com.example.astrolingo.api.TestApi;
import com.example.astrolingo.domain.test.nav_answer;
import com.example.astrolingo.function.StringManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class translateMainActivity extends AppCompatActivity {
    private SharedPreferenceClass sharedPreClass;
    private CardView word_history_button, word_mark_button;
    private TextView header_title, button_search, translate_text, card_origin, card_translate;
    private ImageView backIcon, icon_switch;
    private View header;
    private String textTranslate;
    private boolean isTranslateEnglish;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_translate_main);

        textTranslate = "";
        isTranslateEnglish = true;

        sharedPreClass = new SharedPreferenceClass(this);

        header = findViewById(R.id.header);

        backIcon = header.findViewById(R.id.backIcon);
        header_title = header.findViewById(R.id.header_title);
        button_search = findViewById(R.id.button_search);
        icon_switch = findViewById(R.id.icon_switch);
        word_history_button = findViewById(R.id.word_history_button);
        word_mark_button = findViewById(R.id.word_mark_button);
        translate_text = findViewById(R.id.translate_text);
        card_origin = findViewById(R.id.card_origin);
        card_translate = findViewById(R.id.card_translate);

        // init value
        header_title.setText(this.getString(R.string.translate_header));

        // event
        button_search.setOnClickListener(v ->{
            textTranslate = translate_text.getText().toString();

            translateText();
        });

        icon_switch.setOnClickListener(v ->{

        });

        word_history_button.setOnClickListener(v ->{

        });

        word_mark_button.setOnClickListener(v ->{

        });

        backIcon.setOnClickListener(v ->{
            finish();
        });
    }

    private void translateText() {
//        textTranslate
//        isTranslateEnglish
        Intent intent = new Intent(translateMainActivity.this, translateDetailActivity.class);
        intent.putExtra("textTranslate", textTranslate);
        intent.putExtra("isTranslateEnglish", isTranslateEnglish);
    }

    private void switchLanguage() {
        if(isTranslateEnglish){
            isTranslateEnglish = false;

            card_origin.setText(this.getString(R.string.english));
            card_translate.setText(this.getString(R.string.vietnamese));
        }
        else{
            isTranslateEnglish = true;

            card_origin.setText(this.getString(R.string.vietnamese));
            card_translate.setText(this.getString(R.string.english));
        }
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        finish();
//    }
}
