package com.example.astrolingo.activity.home.translate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.api.translateApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;

import org.json.JSONException;
import org.json.JSONObject;

public class translateDetailActivity extends AppCompatActivity {
    private SharedPreferenceClass sharedPreClass;
    private TextView header_title, button_search, card_origin, card_translate, origin_title, translate_title;
    private ImageView backIcon, icon_switch;
    private View header;
    private EditText origin_text, translate_text;
    private String textTranslate;
    private ProgressBar progressBar;
    private boolean isTranslateEnglish;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_translate_detail);

        textTranslate = "";
        isTranslateEnglish = true;

        // get Extra data
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            textTranslate = bundle.getString("textTranslate");
            isTranslateEnglish = bundle.getBoolean("isTranslateEnglish");
        }


        sharedPreClass = new SharedPreferenceClass(this);

        header = findViewById(R.id.header);

        backIcon = header.findViewById(R.id.backIcon);
        header_title = header.findViewById(R.id.header_title);
        button_search = findViewById(R.id.button_search);
        origin_text = findViewById(R.id.origin_text);
        translate_text = findViewById(R.id.translate_text);
        icon_switch = findViewById(R.id.icon_switch);

        card_origin = findViewById(R.id.card_origin);
        card_translate = findViewById(R.id.card_translate);
        origin_title = findViewById(R.id.origin_title);
        translate_title = findViewById(R.id.translate_title);

        progressBar = findViewById(R.id.progressBar);

        // init value
        progressBar.setVisibility(View.GONE);
        header_title.setText(this.getString(R.string.translate_header));
        origin_text.setText(textTranslate);
        updateLanguageTranslate();

        // event
        button_search.setOnClickListener(v ->{
            textTranslate = translate_text.getText().toString();

            translateText();
        });

        icon_switch.setOnClickListener(v -> {
            switchLanguage();
        });

        backIcon.setOnClickListener(v ->{
            finish();
        });

        // set value
        translateText();
    }

    private void translateText() {
//        textTranslate
//        isTranslateEnglish
        progressBar.setVisibility(View.VISIBLE);

        translateApi.translateLanguage(
            textTranslate,
            isTranslateEnglish,
            this,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Xử lý khi thành công
                    try {
                        String translatedText = response.getString("translation");

                        updateTextTranslate(translatedText);
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Toast.makeText(translateDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Xử lý khi có lỗi
                    Log.e("API_ERROR", error.toString());
                }
            }
        );

    }

    private void switchLanguage() {
        if(isTranslateEnglish){
            isTranslateEnglish = false;
        }
        else{
            isTranslateEnglish = true;
        }

        updateLanguageTranslate();
    }

    private void updateLanguageTranslate(){
        if(isTranslateEnglish) {
            card_origin.setText(this.getString(R.string.vietnamese));
            origin_title.setText(this.getString(R.string.vietnamese));

            card_translate.setText(this.getString(R.string.english));
            translate_title.setText(this.getString(R.string.english));
        }
        else {
            card_origin.setText(this.getString(R.string.english));
            origin_title.setText(this.getString(R.string.english));

            card_translate.setText(this.getString(R.string.vietnamese));
            translate_title.setText(this.getString(R.string.vietnamese));
        }
    }

    private void updateTextTranslate(String translatedText){
        translate_text.setText(translatedText);
    }

    private

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        finish();
//    }
}
