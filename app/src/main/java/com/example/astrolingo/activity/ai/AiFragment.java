package com.example.astrolingo.activity.ai;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.apdapter.ai.messageChatboxAdapter;
import com.example.astrolingo.api.ChatBoxApi;
import com.example.astrolingo.domain.ai.messageChatbox;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
    private ProgressBar process_bar;

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
        process_bar = view.findViewById(R.id.process_bar);

        process_bar.setVisibility(View.GONE);

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

            process_bar.setVisibility(View.VISIBLE);
            editText.setEnabled(false);

            chatAI(mess, getContext());

            editText.setText("");
        });

        return view;
    }

    private void chatAI(String mess, Context context) {
        HashMap<String, String> params = new HashMap<>();
        params.put("query", mess);

        ChatBoxApi.chat(
                params,
                context,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý khi thành công
                        Log.d("API_SUCCESS", response.toString());

                        String aiChat_text = response.optString("response");

                        messageChatbox aiChat = new messageChatbox(aiChat_text, false);
                        list_message.add(aiChat);
                        messageAdapter.notifyDataSetChanged();

                        process_bar.setVisibility(View.GONE);
                        editText.setEnabled(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý khi có lỗi
                        //Log.e("API_ERROR", error.toString());

                        process_bar.setVisibility(View.GONE);
                        editText.setEnabled(true);
                    }
                });
    }

    private void initValueListView() {
        messageChatbox aiChat = new messageChatbox("tôi là AI - Astrolingo được phát triển bởi nhóm 6. Tôi có thể giúp được gì không?", false);
        list_message.add(aiChat);
    }
}