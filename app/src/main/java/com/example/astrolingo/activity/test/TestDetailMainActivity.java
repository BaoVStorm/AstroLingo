package com.example.astrolingo.activity.test;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.apdapter.test.TestDetailAdapter;
import com.example.astrolingo.domain.test.testDetail_page;

import com.example.astrolingo.api.TestApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestDetailMainActivity extends AppCompatActivity  {
    SharedPreferenceClass sharedPreClass;
    ViewPager2 viewpager;
    JSONObject testObject;
    TextView header_time, header_part_full, header_part_number;
    List<testDetail_page> list_page;

    JSONArray partJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_test_detail_main);

        sharedPreClass = new SharedPreferenceClass(this);

        // init
        viewpager = findViewById(R.id.viewpager);
        header_time = findViewById(R.id.header_time);
        header_part_full = findViewById(R.id.header_part_full);
        header_part_number = findViewById(R.id.header_part_number);

        //
        String testString = getIntent().getStringExtra("testObject");

        // add temp value
        list_page = new ArrayList<>();
//        list_page.add(new testDetail_page("start_part"));
//        list_page.add(new testDetail_page("part1"));
//        list_page.add(new testDetail_page("part2"));

        try {
            testObject = new JSONObject(testString);
        } catch (JSONException e) {
            e.printStackTrace();
            finish();
        }

        // init value
        header_time.setText(testObject.optString("test_time"));
        header_part_full.setText(testObject.optString("question_number"));

        // add list
//        try {
//            addListPage();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            finish();
//        }
        getListPart_addListPage();

    }

    private void getListPart_addListPage() {

        TestApi.getListPart(
            this,
            sharedPreClass.getValue_string("token"),
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    // Xử lý khi thành công
                    try {
                        partJSONArray = jsonArray;

                        // thêm listPage();
                        addListPage();
                    } catch (JSONException e) {
                        Log.e("error", e.toString());
                        throw new RuntimeException(e);
                    }

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Xử lý khi có lỗi
                    Log.e("API_ERROR", error.toString());
                }
            }
        );

    }

    private void addListPage() throws JSONException {

        /*
        temp response

        [
            {
                "_id": "68206059a87bed1c0357e730",
                "part_id": 1,
                "group_question_id": "test1_part1_1",
                "url_image1": "https://estudyme.hoc102.com/legacy-data/kslearning/images/418922160-1620725865601-pic1.png",
                "url_audio": "https://storage.googleapis.com/estudyme/dev/2022/06/27/30449101.mp3",
                "test_id": 1
            }
        ]

         */

        // get list group question
        TestApi.getListGroupQuestion(
            testObject.getInt("test_id"),
            this,
            sharedPreClass.getValue_string("token"),
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    // Xử lý khi thành công
                    try {
                        int partDefault = 0;

                        for(int i = 0; i < jsonArray.length(); i++) {
                            if(jsonArray.isNull(i)) {
                                throw new JSONException("Null value");
                            }

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if(partDefault != jsonObject.getInt("part_id")) {
                                testDetail_page detailPage = new testDetail_page("start_part");

                                editTestDetailPage(detailPage, jsonObject.getInt("part_id"));

                                list_page.add(detailPage);

                                partDefault = jsonObject.getInt("part_id");
                            }

                            testDetail_page detailPage = new testDetail_page();
                            detailPage.setGroupQuestionId(jsonObject.getString("group_question_id"));

                            if(jsonObject.getInt("part_id") <= 4)
                                detailPage.setType("listening");
                            else
                                detailPage.setType("reading");

                            list_page.add(detailPage);

                        }

                        // thêm danh sách vào viewpager
                        TestDetailAdapter adapter = new TestDetailAdapter(list_page);
                        viewpager.setAdapter(adapter);

                    } catch (JSONException e) {
                        Log.e("error", e.toString());
                        throw new RuntimeException(e);
                    }

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Xử lý khi có lỗi
                    Log.e("API_ERROR", error.toString());
                }
            }
        );

    }

    private void editTestDetailPage(testDetail_page detailPage, int part_id) throws JSONException {
        // set part_id
        detailPage.setPart(part_id);
        detailPage.setContent(partJSONArray.getJSONObject(part_id - 1).getString("description"));
        detailPage.setTitle(partJSONArray.getJSONObject(part_id - 1).getString("title"));

        Log.e("print", detailPage.getContent());
    }
}
