package com.example.astrolingo.apdapter.test;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrolingo.domain.test.question_test;
import com.example.astrolingo.domain.test.testDetail_page;

import java.util.ArrayList;
import java.util.List;

import com.example.astrolingo.R;
public class TestDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<testDetail_page> itemList;

    public TestDetailAdapter(List<testDetail_page> itemList) {
        this.itemList = itemList;
    }


    // ---------------------------------- Edit layout ----------------------------------
    public static class StartPartViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitlePart, txtContentPart;

        public StartPartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitlePart = itemView.findViewById(R.id.title_part);
            txtContentPart = itemView.findViewById(R.id.content_part);
        }
    }

    public static class ListeningViewHolder extends RecyclerView.ViewHolder {
        ListView lv_question;
        ImageView audio_reply5, audio_pause, audio_forward5;
        TextView audio_starttime, audio_endtime;
        SeekBar audio_seekbar;

        public ListeningViewHolder(@NonNull View itemView) {
            super(itemView);

            lv_question = itemView.findViewById(R.id.lv_question);
            audio_reply5 = itemView.findViewById(R.id.audio_reply5);
            audio_pause = itemView.findViewById(R.id.audio_pause);
            audio_forward5 = itemView.findViewById(R.id.audio_forward5);

            audio_starttime = itemView.findViewById(R.id.audio_starttime);
            audio_endtime = itemView.findViewById(R.id.audio_endtime);

            audio_seekbar = itemView.findViewById(R.id.audio_seekbar);
        }
    }


    // ---------------------------------- ----------------------------------

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.page_test_detail_startpart, parent, false);

            return new StartPartViewHolder(view);

        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.page_test_detail_listening, parent, false);

            return new ListeningViewHolder(view);

        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.page_test_detail_reading, parent, false);

            return new ListeningViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        testDetail_page item = itemList.get(position);
        return item.getType();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        testDetail_page item = itemList.get(position);

        if (holder instanceof StartPartViewHolder) {
            StartPartViewHolder viewHolder = (StartPartViewHolder) holder;

            viewHolder.txtTitlePart.setText(String.valueOf(item.getTitle()));
            viewHolder.txtContentPart.setText(String.valueOf(item.getContent()));

        } else if (holder instanceof ListeningViewHolder) {
            ListeningViewHolder viewHolder = (ListeningViewHolder) holder;

            List<question_test> list_QuestionTest = new ArrayList<>();
            list_QuestionTest.add(new question_test("What does the woman want to find?", "(A) Some money", "(B) A file", "(C) An office key", "(D) A check", 2));
            list_QuestionTest.add(new question_test("What does the woman want to find?", "(A) Some money", "(B) A file", "(C) An office key", "(D) A check", 2));
            list_QuestionTest.add(new question_test("What does the woman want to find?", "(A) Some money", "(B) A file", "(C) An office key", "(D) A check", 2));
            list_QuestionTest.add(new question_test("What does the woman want to find?", "(A) Some money", "(B) A file", "(C) An office key", "(D) A check", 2));

            QuestionAdapter adapter = new QuestionAdapter(viewHolder.lv_question.getContext(), list_QuestionTest);
            viewHolder.lv_question.setAdapter(adapter);

        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}


