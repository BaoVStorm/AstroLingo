package com.example.astrolingo.apdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.astrolingo.R;

import java.util.List;

public class CustomStringAdapter extends ArrayAdapter<String> {
    private Context context;
//    private List<String> items;
    private Typeface typeface, typefaceHighLight; // Font tuỳ chỉnh
    private int curPosition;
    private boolean NumberCount;

    public CustomStringAdapter(Context context, List<String> items, int curPosition, boolean NumberCount) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
//        this.items = items;
        this.typeface = ResourcesCompat.getFont(context, R.font.afacad_medium);
        this.typefaceHighLight = ResourcesCompat.getFont(context, R.font.afacad_bold);
        this.NumberCount = NumberCount;
        this.curPosition = curPosition;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView textView = (TextView) view.findViewById(android.R.id.text1);

        if(curPosition == position) {
            textView.setTypeface(typefaceHighLight);  // Gán font
            textView.setTextSize(17);        // Gán kích thước nếu cần
            textView.setTextColor(context.getColor(R.color.main_color_purple_dark)); // Gán màu nếu cần
        }
        else {
            textView.setTypeface(typeface);  // Gán font
            textView.setTextSize(16);        // Gán kích thước nếu cần
            textView.setTextColor(context.getColor(R.color.black_light)); // Gán màu nếu cần
        }

        String originalText = getItem(position);

        if (NumberCount) {
            textView.setText((position + 1) + ". " + originalText);
        } else {
            textView.setText(originalText);
        }

        return view;
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
    }
}
