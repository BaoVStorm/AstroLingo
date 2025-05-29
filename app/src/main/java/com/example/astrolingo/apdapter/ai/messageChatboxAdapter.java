package com.example.astrolingo.apdapter.ai;

import android.app.Dialog;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.AudioManager;
import com.example.astrolingo.api.UserStarApi;
import com.example.astrolingo.domain.ai.messageChatbox;
import com.example.astrolingo.domain.home.history.history_word;
import com.example.astrolingo.domain.home.learn_word.vocabulary;
import com.example.astrolingo.domain.home.my_word.myWords;
import com.example.astrolingo.function.AudioListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class messageChatboxAdapter extends ArrayAdapter<messageChatbox> {
    private static final int TYPE_AI = 0;
    private static final int TYPE_USER = 1;
    private Context context;
    private String user_id, token;

    public messageChatboxAdapter(Context context, List<messageChatbox> listWords) {
        super(context, R.layout.act_word_learn_adapter, listWords);
        this.context = context;
    }

    public void setUserId(String user_id, String token) {
        this.user_id = user_id;
        this.token = token;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getItemViewType(int position) {
        messageChatbox message = getItem(position);
        assert message != null;
        if (message.isUser()) {
            return TYPE_USER;
        } else {
            return TYPE_AI;
        }
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        messageChatbox message = getItem(position);
        int viewType = getItemViewType(position);

        if (convertView == null) {
            if (viewType == TYPE_USER) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.page_chatbox_user_adapter, parent, false);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.page_chatbox_ai_adapter, parent, false);
            }
        }

//        if (viewType == TYPE_VOCABULARY) {
//            setTypeVocabulary(convertView, words.getVocabulary(), words);
//        } else {
//
//            setTypeHistoryWord(convertView, words.getWord(), words);
//        }

        TextView message_input = convertView.findViewById(R.id.message_input);
        assert message != null;
        message_input.setText(message.getMessage());

        return convertView;
    }



}