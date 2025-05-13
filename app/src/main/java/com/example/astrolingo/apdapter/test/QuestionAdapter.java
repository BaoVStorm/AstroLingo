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

import com.bumptech.glide.Glide;
import com.example.astrolingo.R;
import com.example.astrolingo.domain.test.question_test;

import java.util.List;
import java.util.Objects;

public class QuestionAdapter extends ArrayAdapter<question_test>{
    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_TEXT = 1;

    public QuestionAdapter(Context context, List<question_test> questions) {
        super(context, 0, questions);
    }

    @Override
    public int getViewTypeCount() {
        return 2; // image and text
    }

    @Override
    public int getItemViewType(int position) {
        question_test question = getItem(position);
        if (question != null && "image".equals(question.getType())) {
            return TYPE_IMAGE;
        } else {
            return TYPE_TEXT;
        }
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        question_test question = getItem(position);
        int viewType = getItemViewType(position);

        if (convertView == null) {
            if (viewType == TYPE_IMAGE) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.page_test_detail_adapter_image, parent, false);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.page_test_detail_adapter_question, parent, false);
            }
        }

        if (viewType == TYPE_IMAGE) {
            ImageView imageQuestion = convertView.findViewById(R.id.imageQuestion);
            if (imageQuestion != null && !Objects.equals(question.getImageUrl(), "null")) {
                Glide.with(convertView.getContext())
                        .load(question.getImageUrl())
                        .into(imageQuestion);
            }
        } else {
            TextView questionText = convertView.findViewById(R.id.txt_question);
            TextView ans1 = convertView.findViewById(R.id.answer1);
            TextView ans2 = convertView.findViewById(R.id.answer2);
            TextView ans3 = convertView.findViewById(R.id.answer3);
            TextView ans4 = convertView.findViewById(R.id.answer4);

            if (questionText != null && !Objects.equals(question.getQuestionText(), "null")) {
                questionText.setText(question.getQuestionText());
            }
            else
                questionText.setText("VStorm is so proo!!!");

            if (ans1 != null && !Objects.equals(question.getAns1(), "null")) {
                ans1.setText(question.getAns1());
            }
            else
                ans1.setVisibility(View.GONE);


            if (ans2 != null && !Objects.equals(question.getAns2(), "null")) {
                ans2.setText(question.getAns2());
            }
            else
                ans2.setVisibility(View.GONE);

            if (ans3 != null && !Objects.equals(question.getAns3(), "null")) {
                ans3.setText(question.getAns3());
            }
            else
                ans3.setVisibility(View.GONE);

            if (ans4 != null && !Objects.equals(question.getAns4(), "null")) {
                ans4.setText(question.getAns4());
            }
            else
                ans4.setVisibility(View.GONE);

        }

        return convertView;
    }

}
