package com.example.astrolingo.apdapter.home.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.astrolingo.R;
import com.example.astrolingo.domain.home.history.history_word;
import com.example.astrolingo.domain.test.question_test;

import java.util.List;
import java.util.Objects;

public class history_word_adapter extends ArrayAdapter<history_word> {
    public history_word_adapter(Context context, List<history_word> words) {
        super(context, 0, words);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        history_word words = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.act_history_wordtranslated_adapter, parent, false);
        }

        TextView translateType = convertView.findViewById(R.id.translateType);
        TextView word_text = convertView.findViewById(R.id.word_text);
        TextView meaning_text = convertView.findViewById(R.id.meaning_text);
        TextView time_text = convertView.findViewById(R.id.time_text);
        ImageView icon_mark = convertView.findViewById(R.id.icon_mark);

        translateType.setText(convertView.getResources().getString(words.getTranslateType()));
        word_text.setText(words.getWord());
        meaning_text.setText(words.getMeaning());
        time_text.setText(words.getDate());

        return convertView;
    }

}