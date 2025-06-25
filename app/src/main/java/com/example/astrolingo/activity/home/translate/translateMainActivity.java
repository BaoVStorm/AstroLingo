package com.example.astrolingo.activity.home.translate;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.AnswerTestMananger;
import com.example.astrolingo.Service.KeyboardUtil;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.Service.UtilService;
import com.example.astrolingo.activity.home.history.historyWordTranslatedMainActivity;
import com.example.astrolingo.activity.home.my_words.myWordMainActivity;
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
    private TextView header_title, button_search, card_origin, card_translate;
    private EditText translate_text;
    private ImageView backIcon, icon_switch, icon_paste, icon_copy;
    private View header;
    private String textTranslate;
    private LinearLayout box_interact2;
    private boolean isTranslateEnglish;
    ClipboardManager clipboard;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_translate_main);

        textTranslate = "";
        isTranslateEnglish = true;

        sharedPreClass = new SharedPreferenceClass(this);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

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

        box_interact2 = findViewById(R.id.box_interact2);
        icon_paste = findViewById(R.id.icon_paste);
        icon_copy = findViewById(R.id.icon_copy);

        // init value
        header_title.setText(this.getString(R.string.translate_header));

        // event
        button_search.setOnClickListener(v ->{
            textTranslate = translate_text.getText().toString();

            if(!textTranslate.isEmpty())
                translateText();
        });

        icon_switch.setOnClickListener(v ->{
            switchLanguage();
        });

        word_history_button.setOnClickListener(v ->{
            Intent intent = new Intent(this, historyWordTranslatedMainActivity.class);
            startActivity(intent);
        });

        word_mark_button.setOnClickListener(v ->{
            Intent intent = new Intent(this, myWordMainActivity.class);
            startActivity(intent);
        });

        icon_copy.setOnClickListener(v ->{
            String text = translate_text.getText().toString();

            if(!text.isEmpty())
                copyToClipboard(text);
        });

        icon_paste.setOnClickListener(v ->{
            pasteToEditText(translate_text);
        });

        backIcon.setOnClickListener(v ->{
            finish();
        });

        // sự kiện với keyboard
        View rootView = findViewById(android.R.id.content);

        KeyboardUtil.setKeyboardVisibilityListener(rootView, isVisible -> {
            if (isVisible) {
                // Keyboard is visible
                box_interact2.setVisibility(View.GONE);
            } else {
                // Keyboard is hidden
                box_interact2.setVisibility(View.VISIBLE);
            }
        });
    }

    private void translateText() {
//        textTranslate
//        isTranslateEnglish
        Intent intent = new Intent(translateMainActivity.this, translateDetailActivity.class);
        intent.putExtra("textTranslate", textTranslate);
        intent.putExtra("isTranslateEnglish", isTranslateEnglish);
        startActivity(intent);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        UtilService UtilService = new UtilService();

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                int[] screenCoords = new int[2];
                view.getLocationOnScreen(screenCoords);
                float x = ev.getRawX() + view.getLeft() - screenCoords[0];
                float y = ev.getRawY() + view.getTop() - screenCoords[1];

                if (x < view.getLeft() || x > view.getRight() ||
                        y < view.getTop() || y > view.getBottom()) {
                    UtilService.hideKeyboard(view, this);
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void copyToClipboard(String text) {
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Đã lưu vào bộ nhớ tạm", Toast.LENGTH_SHORT).show();
    }

    public void pasteToEditText(EditText editText) {
        if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            String pasteData = item.getText().toString();
            editText.setText(pasteData); // hoặc append nếu muốn nối
        }
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        finish();
//    }

}
