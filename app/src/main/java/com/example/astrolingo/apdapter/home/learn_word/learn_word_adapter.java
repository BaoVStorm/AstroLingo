package com.example.astrolingo.apdapter.home.learn_word;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.astrolingo.R;
import com.example.astrolingo.Service.AudioManager;
import com.example.astrolingo.domain.home.learn_word.vocabulary;
import com.example.astrolingo.function.AudioListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class learn_word_adapter extends ArrayAdapter<vocabulary> {

    private final List<vocabulary> originalList;   // Danh sách đầy đủ
    private List<vocabulary> displayList;          // Danh sách đang hiển thị
    private ClipboardManager clipboard;
    private int curTopicId = 1;
    private int curLevelId = 0;     // mặc định sẽ là tất cả
    private Context context;

    public learn_word_adapter(Context context, List<vocabulary> words, ClipboardManager clipboard) {
        super(context, 0, words);
        this.originalList = new ArrayList<>(words);
        this.displayList = new ArrayList<>(words);

        this.clipboard = clipboard;
        this.context = context;
    }

    @Override
    public int getCount() {
        return displayList.size();
    }

    @Override
    public vocabulary getItem(int position) {
        return displayList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        vocabulary words = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.act_word_learn_adapter, parent, false);
        }

        TextView word_text, pronunciation_text, type_text, meaning_text;
        word_text = convertView.findViewById(R.id.word_text);
        pronunciation_text = convertView.findViewById(R.id.pronunciation_text);
        type_text = convertView.findViewById(R.id.type_text);
        meaning_text = convertView.findViewById(R.id.meaning_text);

        assert words != null;
        word_text.setText(words.getWord());
        pronunciation_text.setText(words.getPronunciation());
        type_text.setText(words.getTypeOfWord());
        meaning_text.setText(words.getMeaningVietnamese());

        ImageView icon_loud = convertView.findViewById(R.id.icon_loud);


        icon_loud.setOnClickListener(v -> {
            if(!AudioManager.isPlaying()) {
                AudioManager.setAudio(context ,words.getAudioUrl(), new AudioListener() {
                    @Override
                    public void onPrepared() {
                        icon_loud.setImageResource(R.drawable.icon_asset_loud3);
                    }

                    @Override
                    public void onCompletion() {
                        icon_loud.setImageResource(R.drawable.icon_asset_loud);
                    }
                    @Override
                    public void onError() {
                        Log.e("AUDIO", "Error during playback");
                    }
                });
            }
        });


        return convertView;
    }

    public void filterTopic(int topic_id) {
        curTopicId = topic_id;

        updateDisplayList();
    }

    public void filterLevel(int level_id) {
        curLevelId = level_id;

        updateDisplayList();
    }

    public void filterLevelAndTopic(int level_id, int topic_id) {
        curLevelId = level_id;
        curTopicId = topic_id;

        updateDisplayList();
    }

    private void updateDisplayList() {
        displayList.clear();
        for (vocabulary item : originalList) {
            if (item.getTopicId() == curTopicId + 1 && (curLevelId == 0 || item.getLevelId() == curLevelId)) {
                displayList.add(item);
            }
        }
        notifyDataSetChanged();
    }

//    public void filterVietnamese() {
//        displayList.clear();
//        for (history_word item : originalList) {
//            if (!item.isTranslateEnglish()) {
//                displayList.add(item);
//            }
//        }
//        notifyDataSetChanged();
//    }

//    private void copyToClipboard(String text) {
//        ClipData clip = ClipData.newPlainText("label", text);
//        clipboard.setPrimaryClip(clip);
//        Toast.makeText(this, "Đã lưu vào bộ nhớ tạm", Toast.LENGTH_SHORT).show();
//    }

}