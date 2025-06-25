package com.example.astrolingo.activity.home.learn_vocab;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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
import com.example.astrolingo.apdapter.CustomStringAdapter;
import com.example.astrolingo.apdapter.home.history.history_word_adapter;
import com.example.astrolingo.apdapter.home.learn_word.learn_word_adapter;
import com.example.astrolingo.api.VocabularyApi;
import com.example.astrolingo.domain.home.history.history_word;
import com.example.astrolingo.domain.home.learn_word.vocabulary;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
    private ArrayList<String> List_topics = new ArrayList<>();
    private ArrayList<String> List_levels = new ArrayList<>();
    private learn_word_adapter learnWordAdapter;

    private Dialog dialog_level;
    private BottomSheetDialog bottomDialog_topic;

    private TextView flashcard;

    private int curTopic = 0, curLevel = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_word_learn);

        curTopic = 0;
        curLevel = 0;

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

        flashcard = findViewById(R.id.flashcard);

        // init
        header_title.setText(getString(R.string.learnVocabulary_header));

        initDialog();

        //
        getListVocabTopics();
        getListVocabLevels();

        // set event

        flashcard.setOnClickListener(v->{
            Intent intent = new Intent(learnVocabMainActivity.this, learnVocab_FlashCardActivity.class);
            intent.putExtra("list_words", learnWordAdapter.getDisplayList());
            startActivity(intent);
        });

        backIcon.setOnClickListener(v->{
            dialog_level.dismiss();
            bottomDialog_topic.dismiss();
            finish();
        });

        getListWords();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getListWords();
    }

    private void initDialog() {
        bottomDialog_topic = new BottomSheetDialog(this);
        bottomDialog_topic.setContentView(R.layout.act_word_learn_dialog_topic);
        // bottomDialog_filter.getWindow().setBackgroundDrawable(getDrawable(R.drawable.page_test_detail_dialog_info_bg));
        bottomDialog_topic.setCanceledOnTouchOutside(true);

        // ngăn lướt xuống dialog
        bottomDialog_topic.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED); // Mở full
                behavior.setSkipCollapsed(true); // Bỏ qua trạng thái collapsed
                behavior.setDraggable(false); // Ngăn không cho vuốt xuống để đóng
            }
        });

        box_topic_input.setOnClickListener(v->{
            bottomDialog_topic.show();
        });


        // -------------------------
        dialog_level = new Dialog(this);
        dialog_level.setContentView(R.layout.act_word_learn_dialog_level);
        dialog_level.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_level.getWindow().setBackgroundDrawable(getDrawable(R.drawable.page_test_detail_dialog_info_bg));
        dialog_level.setCanceledOnTouchOutside(true);

        box_level_input.setOnClickListener(v->{
            dialog_level.show();
        });

    }

    private void getListVocabTopics() {
        List_topics.clear();

        VocabularyApi.getListVocabTopics(
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý khi thành công
                        try {
                            JSONArray jsonArray = response.getJSONArray("vocabTopics");

                            for(int i = 0; i < jsonArray.length(); i ++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String topic = jsonObject.optString("topic_name", "");

                                List_topics.add(topic);
                            }

                            //
                            ListView lisetview_topic = bottomDialog_topic.findViewById(R.id.lv);
                            CustomStringAdapter adapter = new CustomStringAdapter(learnVocabMainActivity.this, List_topics, 0, true);
                            lisetview_topic.setAdapter(adapter);
                            setEventListenerForListView_topic(lisetview_topic, adapter);

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

    private void setEventListenerForListView_topic(ListView lisetview_topic, CustomStringAdapter adapter) {
        lisetview_topic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy phần tử được nhấn
                // Object item = parent.getItemAtPosition(position);

//                Toast.makeText(learnVocabMainActivity.this, "Bạn nhấn topic tứ " + position, Toast.LENGTH_SHORT).show();
                learnWordAdapter.filterTopic(position);
                chooseTopic(position);

                adapter.setCurPosition(position);
                curTopic = position;
            }
        });
        chooseTopic(curTopic);
    }
    private void chooseTopic(int position) {
        String topic = List_topics.get(position);
        topic_input.setText(topic);
        bottomDialog_topic.dismiss();
    }

    private void getListVocabLevels() {
        List_levels.clear();
        List_levels.add("Tất cả");

        VocabularyApi.getListVocabLevels(
            this,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Xử lý khi thành công
                    try {
                        JSONArray jsonArray = response.getJSONArray("vocabLevels");

                        for(int i = 0; i < jsonArray.length(); i ++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String topic = jsonObject.optString("level_name", "");

                            List_levels.add(topic);
                        }

                        //
                        ListView lisetview_level = dialog_level.findViewById(R.id.lv);
                        CustomStringAdapter adapter = new CustomStringAdapter(learnVocabMainActivity.this, List_levels, 0,false);
                        lisetview_level.setAdapter(adapter);
                        setEventListenerForListView_Level(lisetview_level, adapter);

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

    private void setEventListenerForListView_Level(ListView lisetview_level, CustomStringAdapter adapter) {
        lisetview_level.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy phần tử được nhấn
                // Object item = parent.getItemAtPosition(position);

//                Toast.makeText(learnVocabMainActivity.this, "Bạn nhấn level tứ " + position, Toast.LENGTH_SHORT).show();
                learnWordAdapter.filterLevel(position);
                chooseLevel(position);

                adapter.setCurPosition(position);
                curLevel = position;
            }
        });
        chooseLevel(curLevel);
    }
    private void chooseLevel(int position) {
        String level = List_levels.get(position);
        level_input.setText(level);
        dialog_level.dismiss();
    }

    private void getListWords() {
        List_words.clear();

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

                            List_words.add(vob);
                        }

                        learnWordAdapter = new learn_word_adapter(learnVocabMainActivity.this, List_words, clipboard);
                        learnWordAdapter.filterLevelAndTopic(curLevel, curTopic);
                        learnWordAdapter.setUserId(sharedPreClass.getValue_string("user_id"), sharedPreClass.getValue_string("token"));
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
