package com.example.astrolingo.activity.home.history;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.KeyboardUtil;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.activity.home.translate.translateDetailActivity;
import com.example.astrolingo.apdapter.home.history.history_word_adapter;
import com.example.astrolingo.api.UserLookupHistoryApi;
import com.example.astrolingo.domain.home.history.history_word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class historyWordTranslatedMainActivity extends AppCompatActivity {
    private SharedPreferenceClass sharedPreClass;
    private ArrayList<history_word> history_words = new ArrayList<>();
    private View header;
    private ListView listview_history_word;
    private ImageView backIcon;
    private TextView filter_english, filter_all, filter_vietnamese, header_title;
    private history_word_adapter historyWordAdapter;
    private ClipboardManager clipboard;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_history_wordtranslated);

        // init
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        sharedPreClass = new SharedPreferenceClass(this);

        header = findViewById(R.id.header);

        backIcon = header.findViewById(R.id.backIcon);
        header_title = header.findViewById(R.id.header_title);

        filter_english = findViewById(R.id.filter_english);
        filter_all = findViewById(R.id.filter_all);
        filter_vietnamese = findViewById(R.id.filter_vietnamese);

        listview_history_word = findViewById(R.id.listview_history_word);

        // init
        header_title.setText(getString(R.string.historySearch_header));

        // set Listener filter
        setListenerFilter();

        // backIcon
        backIcon.setOnClickListener(v ->{
            history_words.clear();
            finish();
        });

        // get ListHistoryWord
        getListHistoryWord();

        // default filter all
        unHighLightAllFilter();
        highLightFilter(filter_all);

    }

    private void setListenerFilter() {
        filter_english.setOnClickListener(v -> {
           historyWordAdapter.filterEnglish();

            unHighLightAllFilter();
            highLightFilter(filter_english);
        });

        filter_all.setOnClickListener(v -> {
            historyWordAdapter.filterAll();

            unHighLightAllFilter();
            highLightFilter(filter_all);
        });

        filter_vietnamese.setOnClickListener(v -> {
            historyWordAdapter.filterVietnamese();

            unHighLightAllFilter();
            highLightFilter(filter_vietnamese);
        });
    }
    private void unHighLightFilter(TextView filter) {
        filter.setBackground(null);
        filter.setTextColor(this.getColor(R.color.dark_grey));
    }
    private void unHighLightAllFilter() {
        unHighLightFilter(filter_english);
        unHighLightFilter(filter_all);
        unHighLightFilter(filter_vietnamese);
    }
    private void highLightFilter(TextView filter) {
        filter.setBackgroundResource(R.drawable.page_test_detail_dialog_filter_part_bg);
        filter.setTextColor(this.getColor(R.color.main_color_purple_dark));
    }

    private void getListHistoryWord() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", sharedPreClass.getValue_string("user_id"));

        UserLookupHistoryApi.getLookUpHistory(
            params,
            this,
            sharedPreClass.getValue_string("token"),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Xử lý khi thành công
                    try {
                        JSONArray jsonArray = response.getJSONArray("listUserLookupHistory");

                        for(int i = 0; i < jsonArray.length(); i ++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            boolean isTranslateEnglish = jsonObject.getBoolean("isTranslateEnglish");
                            String word = jsonObject.getString("word");
                            String meaning = jsonObject.getString("meaning");
                            String date = jsonObject.getString("lookup_at_vietnam");
                            boolean isStar = jsonObject.getBoolean("isStar");
                            String user_lookup_id = jsonObject.getString("_id");

                            history_word historyWord = new history_word(isTranslateEnglish, word, meaning, date);
                            historyWord.setIsStar(isStar);
                            historyWord.setUserLookupId(user_lookup_id);
                            history_words.add(historyWord);
                        }

                        historyWordAdapter = new history_word_adapter(historyWordTranslatedMainActivity.this, history_words, clipboard);
                        historyWordAdapter.setUserId(sharedPreClass.getValue_string("user_id"), sharedPreClass.getValue_string("token"));
                        listview_history_word.setAdapter(historyWordAdapter);

                    } catch (JSONException e) {
                        Toast.makeText(historyWordTranslatedMainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Xử lý khi có lỗi
                    Toast.makeText(historyWordTranslatedMainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        );

    }
}
