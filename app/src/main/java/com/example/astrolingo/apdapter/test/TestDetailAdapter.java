package com.example.astrolingo.apdapter.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrolingo.domain.test.testDetail_page;

import java.util.List;

import com.example.astrolingo.R;
public class TestDetailAdapter extends RecyclerView.Adapter<TestDetailAdapter.ViewHolder>{
    private List<testDetail_page> itemList;

    public TestDetailAdapter(List<testDetail_page> itemList) {
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView txtTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
//            txtTitle = itemView.findViewById(R.id.txtTitle);
        }
    }

    @NonNull
    @Override
    public TestDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.page_test_detail_startpart, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.page_test_detail_part1, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.page_test_detail_part2, parent, false);
        }


        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        testDetail_page item = itemList.get(position);
        return item.getType(); // giả sử bạn có 1 phương thức getType() trả về int
    }

    @Override
    public void onBindViewHolder(@NonNull TestDetailAdapter.ViewHolder holder, int position) {
        testDetail_page item = itemList.get(position);
//        holder.txtTitle.setText(item.getTitle());
//        holder.imgThumbnail.setImageResource(item.getImageResId()); // Nếu dùng ảnh online thì load bằng Glide/Picasso
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
