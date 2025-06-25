package com.example.astrolingo.activity.test;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.api.TestApi;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class TestFragment extends Fragment {
    SharedPreferenceClass sharedPreClass;
    TextView headerText;

    View view_;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        sharedPreClass = new SharedPreferenceClass(view.getContext());

        // init profile
        if (getActivity() != null) {
            headerText = getActivity().findViewById(R.id.format_textView);
            if (headerText != null) {
                headerText.setText(getString(R.string.testFragment));
            }
        }

        // init
        TextView fullTestTitle = view.findViewById(R.id.fullTestTitle);
        TextView miniTestTitle = view.findViewById(R.id.miniTestTitle);
        fullTestTitle.setText("TOEIC Listening & Reading FullTest | 8");
        miniTestTitle.setText("TOEIC Listening & Reading MiniTest | 8");

        populateTests(view, view.findViewById(R.id.fullTestGrid), view.findViewById(R.id.fullContainer), "true");
        populateTests(view, view.findViewById(R.id.miniTestGrid), view.findViewById(R.id.miniContainer), "false");
        return view;
    }

    private void populateTests(View view, GridLayout gridlayout, FlexboxLayout flexboxLayout, String isFullTest){
        gridlayout.removeAllViews();

        TestApi.getListTest(isFullTest,
            view.getContext(),
            sharedPreClass.getValue_string("token"),
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Xử lý khi thành công
                    addTest(response, gridlayout, flexboxLayout);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Xử lý khi có lỗi
                    Log.e("API_ERROR_TestPage", error.toString());
                }
            }
        );
    }

    private void addTest(JSONArray jsonArray, GridLayout gridlayout, FlexboxLayout flexboxLayout) {
//        for(int i = 0; i < displayCount; i++){
//            View itemView = inflater.inflate(R.layout.page_test_main_item, gridlayout, false);
//            TextView itemLabel = itemView.findViewById(R.id.itemLabel);
//
//            String itemLabel_FullTest = "Test " + (i + 1) + "\nETS 2024";
//            String itemLabel_MiniTest = "Test " + (i + 1);
//
//            if(isFullTest){
//                itemLabel.setText(itemLabel_FullTest);
//            } else{
//                itemLabel.setText(itemLabel_MiniTest);
//            }
//
//            itemView.setOnClickListener(v ->{
//                Intent intent = new Intent(getContext(), TestDetailActivity.class);
//                intent.putExtra("testItem", new PageTestMainItem(isFullTest ? itemLabel_FullTest : itemLabel_MiniTest, isFullTest));
//                /*
//                * Phương thức intent.putExtra(String key, Value value) trong Android được dùng để
//                * truyền dữ liệu từ một Activity (hoặc Fragment) này sang Activity (hoặc Fragment) khác thông qua Intent
//                * */
//                startActivity(intent);
//            });
//
//            gridlayout.addView(itemView);
//        }
        try {

            LayoutInflater inflater = LayoutInflater.from(getContext());

            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.isNull(i))
                    continue;

                JSONObject testObject = jsonArray.getJSONObject(i);

                View itemView = inflater.inflate(R.layout.page_test_main_item, flexboxLayout, false);
                TextView itemLabel = itemView.findViewById(R.id.itemLabel);

                String nameLabel = testObject.getString("title");
                itemLabel.setText(nameLabel);

                // Set click event
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), TestDetailActivity.class);
                    intent.putExtra("testObject", testObject.toString());
                    startActivity(intent);
                });

                // Set layout params for spacing
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(16, 16, 16, 16); // khoảng cách giữa các ô

                itemView.setLayoutParams(params);

                flexboxLayout.addView(itemView);
            }

        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật lại nội dung khi quay lại fragment
//        if (getView() != null) {
//            populateTests(getView().findViewById(R.id.fullTestGrid), 12, true);
//            populateTests(getView().findViewById(R.id.miniTestGrid), 12, false);
//        }
    }
}