package com.example.astrolingo.activity.test;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.astrolingo.R;

public class TestFragment extends Fragment {
    TextView headerText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        // init profile
        if (getActivity() != null) {
            headerText = getActivity().findViewById(R.id.format_textView);
            if (headerText != null) {
                headerText.setText(getString(R.string.testFragment));
            }
        }

        TextView fullTestTitle = view.findViewById(R.id.fullTestTitle);
        TextView miniTestTitle = view.findViewById(R.id.miniTestTitle);
        fullTestTitle.setText("TOEIC Listening & Reading FullTest | " + 15);
        miniTestTitle.setText("TOEIC Listening & Reading MiniTest | " + 13);
        populateTests(view.findViewById(R.id.fullTestGrid), 12, true);
        populateTests(view.findViewById(R.id.miniTestGrid), 12, false);

        return view;
    }

    private void populateTests(GridLayout gridlayout, int displayCount, boolean isFullTest){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        gridlayout.removeAllViews();

        for(int i = 0; i < displayCount; i++){
            View itemView = inflater.inflate(R.layout.page_test_main_item, gridlayout, false);
            TextView itemLabel = itemView.findViewById(R.id.itemLabel);

            String itemLabel_FullTest = "Test " + (i + 1) + "\nETS 2024";
            String itemLabel_MiniTest = "Test " + (i + 1);

            if(isFullTest){
                itemLabel.setText(itemLabel_FullTest);
            } else{
                itemLabel.setText(itemLabel_MiniTest);
            }

            itemView.setOnClickListener(v ->{
                Intent intent = new Intent(getContext(), TestDetailActivity.class);
                intent.putExtra("testItem", new PageTestMainItem(isFullTest ? itemLabel_FullTest : itemLabel_MiniTest, isFullTest));
                /*
                * Phương thức intent.putExtra(String key, Value value) trong Android được dùng để
                * truyền dữ liệu từ một Activity (hoặc Fragment) này sang Activity (hoặc Fragment) khác thông qua Intent
                * */
                startActivity(intent);
            });

            gridlayout.addView(itemView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật lại nội dung khi quay lại fragment
        if (getView() != null) {
            populateTests(getView().findViewById(R.id.fullTestGrid), 12, true);
            populateTests(getView().findViewById(R.id.miniTestGrid), 12, false);
        }
    }
}