package com.example.astrolingo.apdapter.home.history_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.astrolingo.R;
import com.example.astrolingo.activity.home.history_test.historyTestDetailActivity;
import com.example.astrolingo.activity.home.learn_vocab.learnVocabMainActivity;
import com.example.astrolingo.activity.home.learn_vocab.learnVocab_FlashCardActivity;
import com.example.astrolingo.domain.home.history_test.history_test;
import java.util.List;

public class history_test_adapter extends ArrayAdapter<history_test> {
    private String user_id, token;
    private Context context;

    public history_test_adapter(Context context, List<history_test> words) {
        super(context, 0, words);

        this.context = context;
    }

    public void setUserId(String user_id, String token) {
        this.user_id = user_id;
        this.token = token;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        history_test word = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.act_history_test_adapter, parent, false);
        }

        // init
        TextView title_test = convertView.findViewById(R.id.title_test);
        TextView time_text = convertView.findViewById(R.id.time_text);

        TextView score_text = convertView.findViewById(R.id.score_text);
        TextView score_spe = convertView.findViewById(R.id.score_spe);
        TextView fullScore_text = convertView.findViewById(R.id.fullScore_text);

        ConstraintLayout main_adapter = convertView.findViewById(R.id.main_adapter);

        // set data
        assert word != null;
        title_test.setText(word.getCertificate_name());
        time_text.setText(word.getAwarded_at());

        score_text.setText(String.valueOf(word.getCorrect_count()));

        int fullScore = word.getCorrect_count() + word.getWrong_count();
        fullScore_text.setText(String.valueOf(fullScore));

        if(word.getCorrect_count() < word.getWrong_count()) {
            score_text.setTextColor(context.getColor(R.color.lowScore));
            score_spe.setTextColor(context.getColor(R.color.lowScore));
            fullScore_text.setTextColor(context.getColor(R.color.lowScore));
        }
        else {
            score_text.setTextColor(context.getColor(R.color.highScore));
            score_spe.setTextColor(context.getColor(R.color.highScore));
            fullScore_text.setTextColor(context.getColor(R.color.highScore));
        }

        main_adapter.setOnClickListener(v -> {
            Intent intent = new Intent(context, historyTestDetailActivity.class);
            intent.putExtra("word", word);

            if (context != null) {
                ((Activity) context).startActivity(intent);
            }
        });

        return convertView;
    }

}