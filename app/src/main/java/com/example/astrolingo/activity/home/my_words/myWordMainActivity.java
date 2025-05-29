package com.example.astrolingo.activity.home.my_words;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.activity.home.history.historyWordTranslatedMainActivity;
import com.example.astrolingo.apdapter.home.history.history_word_adapter;
import com.example.astrolingo.apdapter.home.my_words.myWordAdapter;
import com.example.astrolingo.api.UserLookupHistoryApi;
import com.example.astrolingo.api.UserStarApi;
import com.example.astrolingo.domain.home.history.history_word;
import com.example.astrolingo.domain.home.learn_word.vocabulary;
import com.example.astrolingo.domain.home.my_word.myWords;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class myWordMainActivity extends AppCompatActivity {
    private SharedPreferenceClass sharedPreClass;
    private ArrayList<myWords> list_MyWords = new ArrayList<>();
    private View header;
    private ListView listview_marked_word;
    private ImageView backIcon;
    private TextView filter_vocabulary, filter_translate, filter_created, header_title;
    private CardView box_addWord;
    private myWordAdapter myWordAdapter;
    private ClipboardManager clipboard;
    private ConstraintLayout box_createWord;

    private Dialog dialog_createWord;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_word_main);

        // init
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        sharedPreClass = new SharedPreferenceClass(this);

        header = findViewById(R.id.header);

        backIcon = header.findViewById(R.id.backIcon);
        header_title = header.findViewById(R.id.header_title);

        filter_vocabulary = findViewById(R.id.filter_vocabulary);
        filter_translate = findViewById(R.id.filter_translate);
        filter_created = findViewById(R.id.filter_created);

        listview_marked_word = findViewById(R.id.listview_marked_word);

        box_addWord = findViewById(R.id.box_addWord);

        box_createWord = findViewById(R.id.box_createWord);

        // init
        initDialog_createWord();

        header_title.setText(getString(R.string.myWord_header));

        // set Listener filter
        setListenerFilter();

        // backIcon
        backIcon.setOnClickListener(v ->{
            list_MyWords.clear();
            finish();
        });

        box_createWord.setVisibility(View.GONE);

        // get ListHistoryWord
        getListUserStarWord();

        // default filter all
        unHighLightAllFilter();
        highLightFilter(filter_vocabulary);

        // set created Word
        box_addWord.setOnClickListener(v -> {
//            createMyWord();
            dialog_createWord.show();
        });
    }

    private void initDialog_createWord() {
        dialog_createWord = new Dialog(this);
        dialog_createWord.setContentView(R.layout.act_my_word_dialog_create_word);
        dialog_createWord.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_createWord.getWindow().setBackgroundDrawable(getDrawable(R.drawable.page_test_detail_dialog_info_bg));
        dialog_createWord.setCanceledOnTouchOutside(true);

        // edit detail dialog
        ConstraintLayout create_button = dialog_createWord.findViewById(R.id.create_button);
        EditText input_word = dialog_createWord.findViewById(R.id.input_word);
        EditText input_meaning = dialog_createWord.findViewById(R.id.input_meaning);

        // set button event
        create_button.setOnClickListener(v -> {
            dialog_createWord.dismiss();

            String word = input_word.getText().toString();
            String meaning = input_meaning.getText().toString();

            createWord(word, meaning);
        });

    }

    private void createWord(String word, String meaning) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", sharedPreClass.getValue_string("user_id"));
        params.put("type_star", "create");
        params.put("word", word);
        params.put("meaning", meaning);

        UserStarApi.addWordUserStars(
                params,
                this,
                sharedPreClass.getValue_string("token"),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý khi thành công
                        // Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject jsonObject = response.getJSONObject("userStars");

                            String id = jsonObject.getString("_id");

                            myWords myWord = new myWords();
                            myWord.setType("create");
                            myWord.setId(id);

                            boolean isTranslateEnglish = false;
                            String word = jsonObject.getString("word");
                            String meaning = jsonObject.getString("meaning");
                            String date = jsonObject.getString("starred_at_vietnam");
                            String user_lookup_id = jsonObject.getString("_id");

                            history_word historyWord = new history_word(isTranslateEnglish, word, meaning, date);
                            historyWord.setUserLookupId(user_lookup_id);

                            myWord.setWord(historyWord);

                            list_MyWords.add(myWord);
                            myWordAdapter.notifyDataSetChanged();
                            myWordAdapter.filterCreated();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý khi có lỗi
                        Toast.makeText(myWordMainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    private void setListenerFilter() {
        filter_vocabulary.setOnClickListener(v -> {
            myWordAdapter.filterVocabulary();

            box_createWord.setVisibility(View.GONE);

            unHighLightAllFilter();
            highLightFilter(filter_vocabulary);
        });

        filter_translate.setOnClickListener(v -> {
            myWordAdapter.filterTranslate();

            box_createWord.setVisibility(View.GONE);

            unHighLightAllFilter();
            highLightFilter(filter_translate);
        });

        filter_created.setOnClickListener(v -> {
            myWordAdapter.filterCreated();

            box_createWord.setVisibility(View.VISIBLE);

            unHighLightAllFilter();
            highLightFilter(filter_created);
        });
    }
    private void unHighLightFilter(TextView filter) {
        filter.setBackground(null);
        filter.setTextColor(this.getColor(R.color.dark_grey));
    }
    private void unHighLightAllFilter() {
        unHighLightFilter(filter_vocabulary);
        unHighLightFilter(filter_translate);
        unHighLightFilter(filter_created);
    }
    private void highLightFilter(TextView filter) {
        filter.setBackgroundResource(R.drawable.page_test_detail_dialog_filter_part_bg);
        filter.setTextColor(this.getColor(R.color.main_color_purple_dark));
    }

    private void getListUserStarWord() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", sharedPreClass.getValue_string("user_id"));

        UserStarApi.getWordUserStars(
                params,
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý khi thành công
                        try {
                            JSONArray jsonArray = response.getJSONArray("userStars");

                            for(int i = 0; i < jsonArray.length(); i ++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String type_star = jsonObject.getString("type_star");
                                String id = jsonObject.getString("_id");

                                myWords myWord = new myWords();
                                myWord.setType(type_star);
                                myWord.setId(id);

                                if(type_star.equals("vocabulary")) {

                                    String word = jsonObject.optString("word", "");
                                    String type = jsonObject.optString("type", "");
                                    String meaning_vietnamese = jsonObject.optString("meaning_vietnamese", "");
                                    String meaning_english = jsonObject.optString("meaning_english", "");
                                    String example_vietnamese = jsonObject.optString("example_vietnamese", "");
                                    String example_english = jsonObject.optString("example_english", "");
                                    String image_url = jsonObject.optString("image_url", "");
                                    String audio_url = jsonObject.optString("audio_url", "");
                                    String pronunciation = jsonObject.optString("pronunciation", "");
                                    boolean isStar = jsonObject.optBoolean("isStar", false);
                                    String vocab_id = jsonObject.getString("_id");

                                    int topic_id = jsonObject.has("topic_id") ? jsonObject.getInt("topic_id") : 1;
                                    int level_id = jsonObject.has("level_id") ? jsonObject.getInt("level_id") : 1;

                                    vocabulary vob = new vocabulary(word, type, meaning_vietnamese, meaning_english, example_vietnamese, example_english, topic_id, level_id);
                                    vob.setPronunciation(pronunciation);
                                    vob.setImageUrl(image_url);
                                    vob.setAudioUrl(audio_url);
                                    vob.setIsStar(isStar);
                                    vob.setVocabId(vocab_id);

                                    myWord.setVocabulary(vob);
                                }
                                else {
                                    boolean isTranslateEnglish = jsonObject.optBoolean("isTranslateEnglish", false);
                                    String word = jsonObject.getString("word");
                                    String meaning = jsonObject.getString("meaning");
                                    String date = jsonObject.getString("starred_at_vietnam");
                                    String user_lookup_id = jsonObject.getString("_id");

                                    history_word historyWord = new history_word(isTranslateEnglish, word, meaning, date);
                                    historyWord.setUserLookupId(user_lookup_id);

                                    myWord.setWord(historyWord);
                                }

                                list_MyWords.add(myWord);

                            }

                            myWordAdapter = new myWordAdapter(myWordMainActivity.this, list_MyWords, clipboard);
                            myWordAdapter.setUserId(sharedPreClass.getValue_string("user_id"), sharedPreClass.getValue_string("token"));
                            myWordAdapter.filterVocabulary();

                            listview_marked_word.setAdapter(myWordAdapter);

                        } catch (JSONException e) {
                            Toast.makeText(myWordMainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý khi có lỗi
                        Toast.makeText(myWordMainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }
}
