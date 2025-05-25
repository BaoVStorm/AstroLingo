package com.example.astrolingo.apdapter.home.learn_word;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import com.example.astrolingo.domain.home.learn_word.vocabulary;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class learn_word_adapter extends ArrayAdapter<vocabulary> {

    private final List<vocabulary> originalList;   // Danh sách đầy đủ
    private List<vocabulary> displayList;          // Danh sách đang hiển thị
    private ClipboardManager clipboard;
    public learn_word_adapter(Context context, List<vocabulary> words, ClipboardManager clipboard) {
        super(context, 0, words);
        this.originalList = new ArrayList<>(words);
        this.displayList = new ArrayList<>(words);

        this.clipboard = clipboard;
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

        return convertView;
    }

//    public void filterEnglish() {
//        displayList.clear();
//        for (history_word item : originalList) {
//            if (item.isTranslateEnglish()) {
//                displayList.add(item);
//            }
//        }
//        notifyDataSetChanged();
//    }
//
//    public void filterAll() {
//        displayList.clear();
//        displayList.addAll(originalList);
//        notifyDataSetChanged();
//    }
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