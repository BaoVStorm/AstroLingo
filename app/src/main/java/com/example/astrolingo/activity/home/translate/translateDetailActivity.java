package com.example.astrolingo.activity.home.translate;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.Service.KeyboardUtil;
import com.example.astrolingo.Service.UtilService;
import com.example.astrolingo.api.UserLookupHistoryApi;
import com.example.astrolingo.api.translateApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class translateDetailActivity extends AppCompatActivity {
    private SharedPreferenceClass sharedPreClass;
    private TextView header_title, button_search, card_origin, card_translate, origin_title, translate_title;
    private ImageView backIcon, icon_switch;
    private View header;
    private EditText origin_text, translate_text;
    private LinearLayout box_interact;
    private ImageView icon_copy_origin, icon_copy_translate;
    private String textTranslate;
    private ProgressBar progressBar;
    private boolean isTranslateEnglish;
    private ClipboardManager clipboard;

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
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

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

        icon_copy_origin = findViewById(R.id.icon_copy_origin);
        icon_copy_translate = findViewById(R.id.icon_copy_translate);

        box_interact = findViewById(R.id.box_interact);

        // init value
        progressBar.setVisibility(View.GONE);
        header_title.setText(this.getString(R.string.translate_header));
        origin_text.setText(textTranslate);
        updateLanguageTranslate();

        // event
        button_search.setOnClickListener(v ->{
            textTranslate = origin_text.getText().toString();

            if(!textTranslate.isEmpty()) {
//                translateText2(textTranslate, isTranslateEnglish);
                translateText(textTranslate, isTranslateEnglish);
            }
        });

        icon_switch.setOnClickListener(v -> {
            switchLanguage();
            translate_text.setText("");
        });

        backIcon.setOnClickListener(v ->{
            finish();
        });

        icon_copy_origin.setOnClickListener(v -> {
            String text = origin_text.getText().toString();

            if(!text.isEmpty())
                copyToClipboard(text);
        });

        icon_copy_translate.setOnClickListener(v -> {
            String text = translate_text.getText().toString();

            if(!text.isEmpty())
                copyToClipboard(text);
        });

        // set value
        translateText(textTranslate, isTranslateEnglish);
//        translateText2(textTranslate, isTranslateEnglish);

        // sự kiện với keyboard
        View rootView = findViewById(android.R.id.content);

        KeyboardUtil.setKeyboardVisibilityListener(rootView, isVisible -> {
            if (isVisible) {
                // Keyboard is visible
                box_interact.setVisibility(View.GONE);
            } else {
                // Keyboard is hidden
                box_interact.setVisibility(View.VISIBLE);
            }
        });

        // reset translate_text
        origin_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Xoá nội dung dịch khi người dùng nhập
                translate_text.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });


    }

    private void translateText(String word, boolean isTranslateEnglish_) {
//        textTranslate
//        isTranslateEnglish
        progressBar.setVisibility(View.VISIBLE);
        disableText(origin_text);

        translateApi.translateLanguage(
            word,
            isTranslateEnglish_,
            this,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Xử lý khi thành công
                    try {
                        String translatedText = response.getString("translation");

                        updateTextTranslate(translatedText);
                        enableText(origin_text);
                        progressBar.setVisibility(View.GONE);

                        addUserLookUpHistory(word, translatedText, isTranslateEnglish_);
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

    private void translateText2(String word, boolean isTranslateEnglish_) {
//        textTranslate
//        isTranslateEnglish
        progressBar.setVisibility(View.VISIBLE);
        disableText(origin_text);

        String temp = passDefaultValue(word);

        if(!temp.isEmpty()) {
            updateTextTranslate(temp);
            enableText(origin_text);
            progressBar.setVisibility(View.GONE);

            addUserLookUpHistory(word, temp, isTranslateEnglish_);
        }

        translateApi.translateLanguage2(
                word,
                isTranslateEnglish_,
                this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // response[0][0][0] = translated text
                            JSONArray firstArray = response.getJSONArray(0);
                            JSONArray secondArray = firstArray.getJSONArray(0);
                            String translatedText = secondArray.getString(0); // "Apple"

                            updateTextTranslate(translatedText);
                            enableText(origin_text);
                            progressBar.setVisibility(View.GONE);

                            addUserLookUpHistory(word, translatedText, isTranslateEnglish_);
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

    private String passDefaultValue(String messg) {
        if(Objects.equals(messg, "con mèo")) {
            return "cat";
        }

        if(Objects.equals(messg, "con sư tử")) {
            return "lion";
        }

        if(Objects.equals(messg, "cook")) {
            return "nấu ăn";
        }

        return "";
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

    private void disableText(TextView tv) {
        tv.setFocusable(false);
        tv.setFocusableInTouchMode(false);
        tv.setTextColor(getColor(R.color.grey));
    }

    private void enableText(TextView tv) {
        tv.setFocusable(true);
        tv.setFocusableInTouchMode(true);
        tv.setTextColor(getColor(R.color.black_light));
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

    private void addUserLookUpHistory(String word, String meaning, boolean isTranslateEnglish_) {
//        "user_id": "6826953fdd0f324c408858d9",
//        "word": "4",
//        "meaning": "quang hợp",
//        "isTranslateEnglish": "false"

//        "vocab_id": "optional"

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", sharedPreClass.getValue_string("user_id"));
        params.put("word", word);
        params.put("meaning", meaning);
        params.put("isTranslateEnglish", String.valueOf(isTranslateEnglish_));

        UserLookupHistoryApi.addLookUpHistory(
            params,
            this,
            sharedPreClass.getValue_string("token"),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Xử lý khi thành công
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Xử lý khi có lỗi
                    Toast.makeText(translateDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        );


    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        finish();
//    }
}
