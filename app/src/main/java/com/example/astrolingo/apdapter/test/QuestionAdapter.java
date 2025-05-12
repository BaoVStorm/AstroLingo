package com.example.astrolingo.apdapter.test;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.astrolingo.R;
import com.example.astrolingo.domain.test.question_test;

import java.util.List;
import java.util.Objects;

public class QuestionAdapter extends ArrayAdapter<question_test>{
    public QuestionAdapter(Context context, List<question_test> questions) {
        super(context, 0, questions);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.page_test_detail_adapter_question, parent, false);

        question_test question = getItem(position);

        // init
        TextView questionText = convertView.findViewById(R.id.txt_question);
        TextView ans1 = convertView.findViewById(R.id.answer1);
        TextView ans2 = convertView.findViewById(R.id.answer2);
        TextView ans3 = convertView.findViewById(R.id.answer3);
        TextView ans4 = convertView.findViewById(R.id.answer4);

        // set value
        assert question != null;
        if(!Objects.equals(question.getQuestionText(), "null")) {
            questionText.setText(question.getQuestionText());
        }
        if(!Objects.equals(question.getAns1(), "null")) {
            ans1.setText(question.getAns1());
        }
        if(!Objects.equals(question.getAns2(), "null")) {
            ans2.setText(question.getAns2());
        }
        if(!Objects.equals(question.getAns3(), "null")) {
            ans3.setText(question.getAns3());
        }
        if(!Objects.equals(question.getAns4(), "null")) {
            ans4.setText(question.getAns4());
        }


        return convertView;
    }

}
