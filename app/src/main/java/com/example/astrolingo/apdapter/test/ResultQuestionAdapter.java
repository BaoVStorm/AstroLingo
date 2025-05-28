package com.example.astrolingo.apdapter.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.astrolingo.R;
import com.example.astrolingo.activity.home.history_test.historyTestDetailActivity;
import com.example.astrolingo.activity.test.checkAnswerResultActivity;
import com.example.astrolingo.domain.test.nav_answer;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ResultQuestionAdapter extends ArrayAdapter<nav_answer> {
    private static final int TYPE_LISTENING = 0;
    private static final int TYPE_READING = 1;

    private BottomSheetDialog bottomDialog_filter;

    private final List<nav_answer> originalList;   // Danh sách đầy đủ
    private List<nav_answer> displayList;          // Danh sách đang hiển thị
    private Context context;

    public void setBottomDialog_filter(BottomSheetDialog bottomDialog_filter) {
        this.bottomDialog_filter = bottomDialog_filter;
    }

    public ResultQuestionAdapter(Context context, List<nav_answer> questions) {
        super(context, 0, questions);
        this.originalList = new ArrayList<>(questions);
        this.displayList = new ArrayList<>(questions);
        this.context = context;
    }

    @Override
    public int getCount() {
        return displayList.size();
    }

    @Override
    public nav_answer getItem(int position) {
        return displayList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        nav_answer answer = getItem(position);
        if (answer != null && answer.getPart() >= 5) {
            return TYPE_READING;
        } else {
            return TYPE_LISTENING;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2; // Listening và Reading
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        nav_answer question = getItem(position);
        int viewType = getItemViewType(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.page_test_detail_dialog_filter_adapter_answer, parent, false);
        }

        ConstraintLayout main_adapter = convertView.findViewById(R.id.main_adapter);

        ImageView icon_warning = convertView.findViewById(R.id.icon_warning);
        TextView question_text = convertView.findViewById(R.id.question_text);
        ConstraintLayout navAnswer1 = convertView.findViewById(R.id.navAnswer1);
        ConstraintLayout navAnswer2 = convertView.findViewById(R.id.navAnswer2);
        ConstraintLayout navAnswer3 = convertView.findViewById(R.id.navAnswer3);
        ConstraintLayout navAnswer4 = convertView.findViewById(R.id.navAnswer4);

        unHighLightAnswer(navAnswer1.findViewById(R.id.avatarImageView1), navAnswer1.findViewById(R.id.navAnswer_text1), convertView.getContext());
        unHighLightAnswer(navAnswer2.findViewById(R.id.avatarImageView2), navAnswer2.findViewById(R.id.navAnswer_text2), convertView.getContext());
        unHighLightAnswer(navAnswer3.findViewById(R.id.avatarImageView3), navAnswer3.findViewById(R.id.navAnswer_text3), convertView.getContext());
        unHighLightAnswer(navAnswer4.findViewById(R.id.avatarImageView4), navAnswer4.findViewById(R.id.navAnswer_text4), convertView.getContext());

        icon_warning.setVisibility(View.VISIBLE);


        // highlight unCorrect ansswer
        if(question.getCurrentChoose() != 0) {
            if(question.getCorrectAnswer() != question.getCurrentChoose())
                icon_warning.setImageResource(R.drawable.icon_assets_wrong);
            else
                icon_warning.setImageResource(R.drawable.icon_assets_correct);

            if(question.getCurrentChoose() == 1)
                highLightAnswer(navAnswer1.findViewById(R.id.avatarImageView1), navAnswer1.findViewById(R.id.navAnswer_text1), convertView.getContext());
            else
            if(question.getCurrentChoose() == 2)
                highLightAnswer(navAnswer2.findViewById(R.id.avatarImageView2), navAnswer2.findViewById(R.id.navAnswer_text2), convertView.getContext());
            else
            if(question.getCurrentChoose() == 3)
                highLightAnswer(navAnswer3.findViewById(R.id.avatarImageView3), navAnswer3.findViewById(R.id.navAnswer_text3), convertView.getContext());
            else
                highLightAnswer(navAnswer4.findViewById(R.id.avatarImageView4), navAnswer4.findViewById(R.id.navAnswer_text4), convertView.getContext());
        }
        else
            icon_warning.setImageResource(R.drawable.icon_assets_unavailable);

        // highlight correct ansswer
        if(question.getCorrectAnswer() == 1)
            highLightAnswerCorrect(navAnswer1.findViewById(R.id.avatarImageView1), navAnswer1.findViewById(R.id.navAnswer_text1), convertView.getContext());
        else
        if(question.getCorrectAnswer() == 2)
            highLightAnswerCorrect(navAnswer2.findViewById(R.id.avatarImageView2), navAnswer2.findViewById(R.id.navAnswer_text2), convertView.getContext());
        else
        if(question.getCorrectAnswer() == 3)
            highLightAnswerCorrect(navAnswer3.findViewById(R.id.avatarImageView3), navAnswer3.findViewById(R.id.navAnswer_text3), convertView.getContext());
        else
            highLightAnswerCorrect(navAnswer4.findViewById(R.id.avatarImageView4), navAnswer4.findViewById(R.id.navAnswer_text4), convertView.getContext());

        navAnswer2.setVisibility(View.VISIBLE);
        navAnswer3.setVisibility(View.VISIBLE);
        navAnswer4.setVisibility(View.VISIBLE);

        if(question.getCountAns() <= 3)
            navAnswer4.setVisibility(View.GONE);
        if(question.getCountAns() <= 2)
            navAnswer3.setVisibility(View.GONE);
        if(question.getCountAns() <= 1)
            navAnswer2.setVisibility(View.GONE);

        // Thiết lập text
        question_text.setText(getContext().getString(R.string.Sentence) + " " + question.getQuestion_id());

        question_text.setOnClickListener(v -> {
            bottomDialog_filter.dismiss();
        });

        main_adapter.setOnClickListener(v->{
            Intent intent = new Intent(context, checkAnswerResultActivity.class);
//            intent.putExtra("group_question_id", word);

            intent.putExtra("question_id", question.getQuestion_id());
            intent.putExtra("part_id", question.getPart());
            intent.putExtra("group_question_id", question.getGroup_question_id());
            intent.putExtra("selected_number", question.getCurrentChoose());
            intent.putExtra("correct_number", question.getCorrectAnswer());


            if (context != null) {
                ((Activity) context).startActivity(intent);
            }
        });

        return convertView;
    }

    private void unHighLightAnswer(ShapeableImageView avatarImageView, TextView navAnswer_text, Context context) {
        avatarImageView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        avatarImageView.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.dark_grey)));

        navAnswer_text.setTextColor(ContextCompat.getColor(context, R.color.dark_grey));
    }

    private void highLightAnswerCorrect(ShapeableImageView avatarImageView, TextView navAnswer_text, Context context) {
        avatarImageView.setBackgroundColor(ContextCompat.getColor(context, R.color.correctAnswer));
        avatarImageView.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.correctAnswer_Border)));

        navAnswer_text.setTextColor(ContextCompat.getColor(context, R.color.white));
    }

    private void highLightAnswer(ShapeableImageView avatarImageView, TextView navAnswer_text, Context context) {
        avatarImageView.setBackgroundColor(ContextCompat.getColor(context, R.color.wrongAnswer));
        avatarImageView.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.wrongAnswer_Border)));

        navAnswer_text.setTextColor(ContextCompat.getColor(context, R.color.white));
    }

    // ==== Các hàm lọc ====

    public void filterListening() {
        displayList.clear();
        for (nav_answer item : originalList) {
            if (item.getPart() < 5) {
                displayList.add(item);
            }
        }
        notifyDataSetChanged();
    }

    public void filterAll() {
        displayList.clear();
        displayList.addAll(originalList);
        notifyDataSetChanged();
    }
    public void filterReading() {
        displayList.clear();
        for (nav_answer item : originalList) {
            if (item.getPart() >= 5) {
                displayList.add(item);
            }
        }
        notifyDataSetChanged();
    }

    public void resetFilter() {
        displayList.clear();
        displayList.addAll(originalList);
        notifyDataSetChanged();
    }
}