package com.example.astrolingo.activity.home.learn_vocab;

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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.activity.home.history.historyWordTranslatedMainActivity;
import com.example.astrolingo.apdapter.home.history.history_word_adapter;
import com.example.astrolingo.apdapter.home.learn_word.learn_word_adapter;
import com.example.astrolingo.api.VocabularyApi;
import com.example.astrolingo.domain.home.history.history_word;
import com.example.astrolingo.domain.home.learn_word.vocabulary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class learnVocabMainActivity extends AppCompatActivity {

    private SharedPreferenceClass sharedPreClass;
    private ClipboardManager clipboard;

    private View header;
    private ImageView backIcon;
    private TextView header_title;

    private TextView topic_input, level_input;
    private ConstraintLayout box_level_input, box_topic_input;

    private ListView listview_learnWord;
    private ArrayList<vocabulary> List_words = new ArrayList<>();
    private learn_word_adapter learnWordAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_word_learn);

        // init
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        sharedPreClass = new SharedPreferenceClass(this);

        header = findViewById(R.id.header);

        backIcon = header.findViewById(R.id.backIcon);
        header_title = header.findViewById(R.id.header_title);

        box_level_input = findViewById(R.id.box_level_input);
        topic_input = findViewById(R.id.topic_input);
        box_topic_input = findViewById(R.id.box_topic_input);
        level_input = findViewById(R.id.level_input);

        listview_learnWord = findViewById(R.id.listview_learnWord);


        // init
        header_title.setText(getString(R.string.learnVocabulary_header));

        // get ListHistoryWord
        getListWords();

    }

    private void getListWords() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", sharedPreClass.getValue_string("user_id"));

        VocabularyApi.getListWords(
            params,
            this,
            sharedPreClass.getValue_string("token"),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Xử lý khi thành công
                    try {
                        JSONArray jsonArray = response.getJSONArray("vocabularies");

                        for(int i = 0; i < jsonArray.length(); i ++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String word = jsonObject.optString("word", "");
                            String type = jsonObject.optString("type", "");
                            String meaning_vietnamese = jsonObject.optString("meaning_vietnamese", "");
                            String meaning_english = jsonObject.optString("meaning_english", "");
                            String example_vietnamese = jsonObject.optString("example_vietnamese", "");
                            String example_english = jsonObject.optString("example_english", "");
                            String image_url = jsonObject.optString("image_url", "");
                            String audio_url = jsonObject.optString("audio_url", "");
                            String pronunciation = jsonObject.optString("pronunciation", "");

                            int topic_id = jsonObject.has("topic_id") ? jsonObject.getInt("topic_id") : 1;
                            int level_id = jsonObject.has("level_id") ? jsonObject.getInt("level_id") : 1;

                            vocabulary vob = new vocabulary(word, type, meaning_vietnamese, meaning_english, example_vietnamese, example_english, topic_id, level_id);
                            vob.setPronunciation(pronunciation);
                            vob.setImageUrl(image_url);
                            vob.setAudioUrl(audio_url);

                            List_words.add(vob);
                        }

                        learnWordAdapter = new learn_word_adapter(learnVocabMainActivity.this, List_words, clipboard);

                        listview_learnWord.setAdapter(learnWordAdapter);

                    } catch (JSONException e) {
                        Toast.makeText(learnVocabMainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Xử lý khi có lỗi
                    Toast.makeText(learnVocabMainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        );

    }
}
