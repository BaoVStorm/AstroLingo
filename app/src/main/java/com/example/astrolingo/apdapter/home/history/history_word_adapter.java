package com.example.astrolingo.apdapter.home.history;

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
import com.bumptech.glide.Glide;
import com.example.astrolingo.R;
import com.example.astrolingo.api.UserStarApi;
import com.example.astrolingo.domain.home.history.history_word;
import com.example.astrolingo.domain.test.nav_answer;
import com.example.astrolingo.domain.test.question_test;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class history_word_adapter extends ArrayAdapter<history_word> {

    private final List<history_word> originalList;   // Danh sách đầy đủ
    private List<history_word> displayList;          // Danh sách đang hiển thị
    private ClipboardManager clipboard;
    private String user_id, token;

    public history_word_adapter(Context context, List<history_word> words, ClipboardManager clipboard) {
        super(context, 0, words);
        this.originalList = new ArrayList<>(words);
        this.displayList = new ArrayList<>(words);

        this.clipboard = clipboard;
    }

    public void setUserId(String user_id, String token) {
        this.user_id = user_id;
        this.token = token;
    }

    @Override
    public int getCount() {
        return displayList.size();
    }

    @Override
    public history_word getItem(int position) {
        return displayList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        history_word words = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.act_history_wordtranslated_adapter, parent, false);
        }

        TextView translateType = convertView.findViewById(R.id.translateType);
        TextView word_text = convertView.findViewById(R.id.word_text);
        TextView meaning_text = convertView.findViewById(R.id.meaning_text);
        TextView time_text = convertView.findViewById(R.id.time_text);
        ImageView icon_mark = convertView.findViewById(R.id.icon_mark);
        BottomSheetDialog bottomDialog_filter;

        // init value
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

        if(!words.isTranslateEnglish()) {
            origin_title.setText(getContext().getString(R.string.english));
            translate_title.setText(getContext().getString(R.string.vietnamese));
        }
        else {
            origin_title.setText(getContext().getString(R.string.vietnamese));
            translate_title.setText(getContext().getString(R.string.english));
        }

        if(words.getIsStar())
            icon_mark.setImageResource(R.drawable.icon_assets_star_check);
        else
            icon_mark.setImageResource(R.drawable.icon_assets_star_uncheck);

        EditText origin_text = bottomDialog_filter.findViewById(R.id.origin_text);
        EditText translate_text = bottomDialog_filter.findViewById(R.id.translate_text);

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
            params.put("user_id", user_id);
            params.put("type_star", "translate");
            params.put("isTranslateEnglish", words.isTranslateEnglish() + "");
            params.put("user_lookup_id", words.getUserLookupId());

            if(words.getIsStar()) {
                // remove star

                UserStarApi.removeWordUserStars(
                    params,
                    getContext(),
                    token,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Xử lý khi thành công
                            // Toast.makeText(getContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();

                            icon_mark.setImageResource(R.drawable.icon_assets_star_uncheck);
                            words.setIsStar(false);
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
            }
            else {
                // add star
                params.put("word", words.getWord());
                params.put("meaning", words.getMeaning());

                UserStarApi.addWordUserStars(
                    params,
                    getContext(),
                    token,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Xử lý khi thành công
                            // Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();

                            icon_mark.setImageResource(R.drawable.icon_assets_star_check);
                            words.setIsStar(true);
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
            }
        });

        return convertView;
    }

    public void filterEnglish() {
        displayList.clear();
        for (history_word item : originalList) {
            if (item.isTranslateEnglish()) {
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
    public void filterVietnamese() {
        displayList.clear();
        for (history_word item : originalList) {
            if (!item.isTranslateEnglish()) {
                displayList.add(item);
            }
        }
        notifyDataSetChanged();
    }

//    private void copyToClipboard(String text) {
//        ClipData clip = ClipData.newPlainText("label", text);
//        clipboard.setPrimaryClip(clip);
//        Toast.makeText(this, "Đã lưu vào bộ nhớ tạm", Toast.LENGTH_SHORT).show();
//    }

}