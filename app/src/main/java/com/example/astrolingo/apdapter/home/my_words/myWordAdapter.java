package com.example.astrolingo.apdapter.home.my_words;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.AudioManager;
import com.example.astrolingo.api.UserStarApi;
import com.example.astrolingo.domain.home.history.history_word;
import com.example.astrolingo.domain.home.learn_word.vocabulary;
import com.example.astrolingo.domain.home.my_word.myWords;
import com.example.astrolingo.domain.test.question_test;
import com.example.astrolingo.function.AudioListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class myWordAdapter extends ArrayAdapter<myWords> {
    private static final int TYPE_VOCABULARY = 0;
    private static final int TYPE_HISTORY_WORD = 1;
    ClipboardManager clipboard;
    private Context context;
    private int saveTopPadding = -1;

    private final List<myWords> originalList;   // Danh sách đầy đủ
    private List<myWords> displayList;          // Danh sách đang hiển thị

    private String user_id, token;

    public myWordAdapter(Context context, List<myWords> listWords, ClipboardManager clipboard) {
        super(context, R.layout.act_word_learn_adapter, listWords);
        this.context = context;

        this.originalList = listWords;
        this.displayList = new ArrayList<>(listWords);

        this.clipboard = clipboard;
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
    public int getCount() {
        return displayList.size();
    }

    @Override
    public myWords getItem(int position) {
        return displayList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        myWords word = getItem(position);
        if (word.getType().equals("vocabulary")) {
            return TYPE_VOCABULARY;
        } else {
            return TYPE_HISTORY_WORD;
        }
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        myWords words = getItem(position);
        int viewType = getItemViewType(position);

        if (convertView == null) {
            if (viewType == TYPE_VOCABULARY) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.act_word_learn_adapter, parent, false);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.act_history_wordtranslated_adapter, parent, false);
            }
        }

        if (viewType == TYPE_VOCABULARY) {

            setTypeVocabulary(convertView, words.getVocabulary(), words);
        } else {

            setTypeHistoryWord(convertView, words.getWord(), words);
        }


        return convertView;
    }

    private void setTypeVocabulary(View convertView, vocabulary words, myWords word) {
        ConstraintLayout main_adapter = convertView.findViewById(R.id.main_adapter);

        TextView word_text, pronunciation_text, type_text, meaning_text;
        word_text = convertView.findViewById(R.id.word_text);
        pronunciation_text = convertView.findViewById(R.id.pronunciation_text);
        type_text = convertView.findViewById(R.id.type_text);
        meaning_text = convertView.findViewById(R.id.meaning_text);

        assert words != null;
        word_text.setText(words.getWord());
        pronunciation_text.setText(words.getPronunciation());
        type_text.setText(words.getTypeOfWord());
        meaning_text.setText(words.getMeaningVietnamese());

        ImageView icon_loud = convertView.findViewById(R.id.icon_loud);
        icon_loud.setImageResource(R.drawable.icon_asset_loud1);

        icon_loud.setOnClickListener(v -> {
            if(!AudioManager.isPlaying()) {
                AudioManager.setAudio(context ,words.getAudioUrl(), new AudioListener() {
                    @Override
                    public void onPrepared() {
                        icon_loud.setImageResource(R.drawable.icon_asset_loud3);
                    }

                    @Override
                    public void onCompletion() {
                        icon_loud.setImageResource(R.drawable.icon_asset_loud1);
                    }
                    @Override
                    public void onError() {
                        Log.e("AUDIO", "Error during playback");
                    }
                });
            }
        });

        ImageView icon_mark = convertView.findViewById(R.id.icon_mark);

        icon_mark.setImageResource(R.drawable.icon_assets_star_check);

        // set Star History Word
        icon_mark.setOnClickListener(v-> {
            HashMap<String, String> params = new HashMap<>();
            params.put("id", word.getId());

            UserStarApi.removeWordUserStarsById(
                    params,
                    context,
                    token,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Xử lý khi thành công
                            // Toast.makeText(getContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();

                            originalList.remove(word);
                            displayList.remove(word);
                            notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý khi có lỗi
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

        });

        // init dialog detail
        Dialog dialog_info = new Dialog(context);
        dialog_info.setContentView(R.layout.act_word_learn_dialog_detail);
        dialog_info.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_info.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.page_test_detail_dialog_info_bg));
        dialog_info.setCanceledOnTouchOutside(true);


        // init value dialog
        TextView second_word = dialog_info.findViewById(R.id.second_word);
        second_word.setText(words.getWord());

        TextView second_word_meaning_english = dialog_info.findViewById(R.id.second_word_meaning_english);
        second_word_meaning_english.setText(words.getMeaningEnglish());

        TextView second_word_meaning_vietnamese = dialog_info.findViewById(R.id.second_word_meaning_vietnamese);
        second_word_meaning_vietnamese.setText(words.getMeaningVietnamese());

        TextView second_example_meaning_english = dialog_info.findViewById(R.id.second_example_meaning_english);
        second_example_meaning_english.setText(words.getExampleEnglish());

        TextView second_example_meaning_vietnamese = dialog_info.findViewById(R.id.second_example_meaning_vietnamese);
        second_example_meaning_vietnamese.setText(words.getExampleVietnamese());

        main_adapter.setOnClickListener(v -> {
            dialog_info.show();
        });
    }

    private void setTypeHistoryWord(View convertView, history_word words, myWords word) {
        TextView translateType = convertView.findViewById(R.id.translateType);
        TextView word_text = convertView.findViewById(R.id.word_text);
        TextView meaning_text = convertView.findViewById(R.id.meaning_text);
        TextView time_text = convertView.findViewById(R.id.time_text);
        ImageView icon_mark = convertView.findViewById(R.id.icon_mark);
        BottomSheetDialog bottomDialog_filter;

        // init value
        if(!Objects.equals(word.getType(), "translate")) {
            translateType.setVisibility(View.GONE);
        }
        else
            translateType.setVisibility(View.VISIBLE);

        bottomDialog_filter = new BottomSheetDialog(getContext());
        bottomDialog_filter.setContentView(R.layout.act_translate_dialog);
        bottomDialog_filter.setCanceledOnTouchOutside(true);

        bottomDialog_filter.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED); // Mở full
                behavior.setSkipCollapsed(true); // Bỏ qua trạng thái collapsed
                behavior.setDraggable(false); // Ngăn không cho vuốt xuống để đóng
            }
        });

        //
        TextView origin_title = bottomDialog_filter.findViewById(R.id.origin_title);
        TextView translate_title = bottomDialog_filter.findViewById(R.id.translate_title);

        EditText origin_text = bottomDialog_filter.findViewById(R.id.origin_text);
        EditText translate_text = bottomDialog_filter.findViewById(R.id.translate_text);

        if(saveTopPadding == -1)
            saveTopPadding = origin_text.getPaddingTop();

        if(Objects.equals(word.getType(), "translate")) {
            origin_title.setVisibility(View.VISIBLE);
            translate_title.setVisibility(View.VISIBLE);

            if(!words.isTranslateEnglish()) {
                origin_title.setText(getContext().getString(R.string.english));
                translate_title.setText(getContext().getString(R.string.vietnamese));
            }
            else {
                origin_title.setText(getContext().getString(R.string.vietnamese));
                translate_title.setText(getContext().getString(R.string.english));
            }

            origin_text.setPadding(origin_text.getPaddingLeft(), saveTopPadding, origin_text.getPaddingRight(), origin_text.getPaddingBottom());
            translate_text.setPadding(translate_text.getPaddingLeft(), saveTopPadding, translate_text.getPaddingRight(), translate_text.getPaddingBottom());
        }
        else {
            origin_title.setVisibility(View.GONE);
            translate_title.setVisibility(View.GONE);

            origin_text.setPadding(origin_text.getPaddingLeft(), 60, origin_text.getPaddingRight(), origin_text.getPaddingBottom());
            translate_text.setPadding(translate_text.getPaddingLeft(), 60, translate_text.getPaddingRight(), translate_text.getPaddingBottom());
        }

        icon_mark.setImageResource(R.drawable.icon_assets_star_check);


        origin_text.setText(words.getWord());
        translate_text.setText(words.getMeaning());

        ImageView icon_copy_origin = bottomDialog_filter.findViewById(R.id.icon_copy_origin);
        ImageView icon_copy_translate = bottomDialog_filter.findViewById(R.id.icon_copy_translate);

        icon_copy_origin.setOnClickListener(v -> {
            ClipData clip = ClipData.newPlainText("label", words.getWord());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Đã lưu vào bộ nhớ tạm", Toast.LENGTH_SHORT).show();
        });

        icon_copy_translate.setOnClickListener(v -> {
            ClipData clip = ClipData.newPlainText("label", words.getMeaning());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Đã lưu vào bộ nhớ tạm", Toast.LENGTH_SHORT).show();
        });

        // click show dialog
        ConstraintLayout main_adapter = convertView.findViewById(R.id.main_adapter);
        main_adapter.setOnClickListener(v-> {
            bottomDialog_filter.show();
        });

        translateType.setText(convertView.getResources().getString(words.getTranslateType()));
        word_text.setText(words.getWord());
        meaning_text.setText(words.getMeaning());
        time_text.setText(words.getDate());

        // set Star History Word
        icon_mark.setOnClickListener(v-> {
            HashMap<String, String> params = new HashMap<>();
            params.put("id", word.getId());

            UserStarApi.removeWordUserStarsById(
                    params,
                    context,
                    token,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Xử lý khi thành công
                            // Toast.makeText(getContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();

                            originalList.remove(word);
                            displayList.remove(word);
                            notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý khi có lỗi
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

        });
    }


    public void filterVocabulary() {
        updateDisplayList("vocabulary");
    }

    public void filterTranslate() {
        updateDisplayList("translate");
    }

    public void filterCreated() {
        updateDisplayList("create");
    }

    private void updateDisplayList(String type) {
        displayList.clear();
        for (myWords item : originalList) {
            if (item.getType().equals(type)) {
                displayList.add(item);
            }
        }
        notifyDataSetChanged();
    }


}



//            if(words.getIsStar()) {
//                // remove star
//
//                UserStarApi.removeWordUserStars(
//                        params,
//                        getContext(),
//                        token,
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                // Xử lý khi thành công
//                                // Toast.makeText(getContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();
//
//                                icon_mark.setImageResource(R.drawable.icon_assets_star_uncheck);
//                                words.setIsStar(false);
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                // Xử lý khi có lỗi
//                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                );
//            }
//            else {
//                // add star
//                params.put("word", words.getWord());
//                params.put("meaning", words.getMeaning());
//
//                UserStarApi.addWordUserStars(
//                        params,
//                        getContext(),
//                        token,
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                // Xử lý khi thành công
//                                // Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
//
//                                icon_mark.setImageResource(R.drawable.icon_assets_star_check);
//                                words.setIsStar(true);
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                // Xử lý khi có lỗi
//                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                );
//            }
