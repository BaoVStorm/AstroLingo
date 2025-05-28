package com.example.astrolingo.apdapter.home.retry_wrong_answer;

import android.annotation.SuppressLint;
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
import com.example.astrolingo.activity.test.checkAnswerResultActivity;
import com.example.astrolingo.domain.home.history_test.history_test;
import com.example.astrolingo.domain.home.retry_wrong_answer.wrongAnswer;

import java.util.List;

public class retryWrongAnswerAdapter extends ArrayAdapter<wrongAnswer> {

    private String user_id, token;
    private Context context;

    public retryWrongAnswerAdapter(Context context, List<wrongAnswer> words) {
        super(context, 0, words);

        this.context = context;
    }

    public void setUserId(String user_id, String token) {
        this.user_id = user_id;
        this.token = token;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        wrongAnswer word = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.act_retry_wrong_answer_adapter, parent, false);
        }

        // init
        TextView title_test = convertView.findViewById(R.id.title_test);
        TextView time_text = convertView.findViewById(R.id.time_text);
        TextView part_text = convertView.findViewById(R.id.part_text);
        TextView question_text = convertView.findViewById(R.id.question_text);

        // set Value
        assert word != null;
        title_test.setText(word.getTest_title());
        time_text.setText(word.getAnswered_at());

        part_text.setText(word.getPart_id() + "");
        question_text.setText(word.getQuestion_id());

        ConstraintLayout main_adapter = convertView.findViewById(R.id.main_adapter);


        main_adapter.setOnClickListener(v -> {
            Intent intent = new Intent(context, checkAnswerResultActivity.class);

            intent.putExtra("question_id", word.getQuestion_number());
            intent.putExtra("part_id", word.getPart_id());
            intent.putExtra("group_question_id", word.getGroup_question_id());
            intent.putExtra("selected_number", word.getSelected_answer());
            intent.putExtra("correct_number", word.getCorrect_answer());

            if (context != null) {
                ((Activity) context).startActivity(intent);
            }
        });

        return convertView;
    }
}
