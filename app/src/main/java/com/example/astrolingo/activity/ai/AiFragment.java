package com.example.astrolingo.activity.ai;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.astrolingo.R;
import com.example.astrolingo.apdapter.ai.messageChatboxAdapter;
import com.example.astrolingo.domain.ai.messageChatbox;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AiFragment extends Fragment {
    TextView headerText;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AiFragment() {
        // Required empty public constructor


    }

    public static AiFragment newInstance(String param1, String param2) {
        AiFragment fragment = new AiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ListView lv_chatbox;
    private EditText editText;
    private ImageView sentMessage;
    private messageChatboxAdapter messageAdapter;
    private ArrayList<messageChatbox> list_message = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ai, container, false);

        // init profile
        if (getActivity() != null) {
            headerText = getActivity().findViewById(R.id.format_textView);
            if (headerText != null) {
                headerText.setText(getString(R.string.AiFragment));
            }
        }

        lv_chatbox = view.findViewById(R.id.lv_chatbox);
        editText = view.findViewById(R.id.editText);
        sentMessage = view.findViewById(R.id.sentMessage);

        // init listview
        initValueListView();

        messageAdapter = new messageChatboxAdapter(getContext(), list_message);
        lv_chatbox.setAdapter(messageAdapter);
        messageAdapter.notifyDataSetChanged();

        sentMessage.setOnClickListener(v-> {
            String mess = editText.getText().toString();

            if (!mess.isEmpty()) {
                messageChatbox userChat = new messageChatbox(mess, true);
                list_message.add(userChat);
                messageAdapter.notifyDataSetChanged();
            }

            editText.setText("");
        });

        return view;
    }

    private void initValueListView() {
        messageChatbox aiChat = new messageChatbox("tôi là AI - Astrolingo được phát triển bởi nhóm 6. Tôi có thể giúp được gì không?", false);
        list_message.add(aiChat);
    }
}