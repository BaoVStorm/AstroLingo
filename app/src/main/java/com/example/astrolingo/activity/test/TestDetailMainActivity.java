package com.example.astrolingo.activity.test;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.AudioTestManager;
import com.example.astrolingo.Service.CountdownHelper;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.apdapter.test.TestDetailAdapter;
import com.example.astrolingo.domain.test.AudioState;
import com.example.astrolingo.domain.test.testDetail_page;

import com.example.astrolingo.api.TestApi;
import com.example.astrolingo.function.CountdownListener;
import com.example.astrolingo.function.StringManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestDetailMainActivity extends AppCompatActivity  {
    SharedPreferenceClass sharedPreClass;
    ViewPager2 viewpager;
    JSONObject testObject;
    TextView header_time, header_part_full, header_part_number, header_spe, header_part, header_part_full_bottom, header_submit;
    ImageView backIcon;
    View header_spe_bottom;
    List<testDetail_page> list_page;
    TestDetailAdapter testDetailAdapter;
    JSONArray partJSONArray;
    int lastAudioPosition = -1;
    CountdownHelper countdownHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_test_detail_main);

        sharedPreClass = new SharedPreferenceClass(this);
        // init
        initValue();

        // get test object
        String testString = getIntent().getStringExtra("testObject");

        // add temp value
        list_page = new ArrayList<>();
//        list_page.add(new testDetail_page("start_part"));
//        list_page.add(new testDetail_page("part1"));

        try {
            testObject = new JSONObject(testString);
        } catch (JSONException e) {
            e.printStackTrace();
            finish();
        }

        initCountTime(Integer.parseInt(testObject.optString("test_time")));

        // init value
        header_part_full.setText(testObject.optString("question_number"));
        header_part_full_bottom.setText(testObject.optString("question_number"));

        // set backIcon
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testDetailAdapter != null)
                    testDetailAdapter.release();

                AudioTestManager.releaseAll();
                finish();
            }
        });

        // set viewpager listener
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Nếu có audio đang phát ở trang trước thì dừng
                if (lastAudioPosition != -1) {
                    AudioState prev = AudioTestManager.map_audio.get(lastAudioPosition);
                    if (prev != null && prev.getMediaPlayer() != null && prev.getMediaPlayer().isPlaying()) {
                        prev.getMediaPlayer().pause();
                        prev.setPlaying(false);
                        prev.changeAudioPauseStop(true);
                    }
                }

                lastAudioPosition = position;

                // update audio_endtime
                AudioState curAudio = AudioTestManager.map_audio.get(position);
                if(curAudio != null)
                    curAudio.updateAudioEndtime();

                // Cập nhật header_test
                if (list_page != null && position < list_page.size()) {
                    testDetail_page currentPage = list_page.get(position);
                    String header_part_text =  getString(R.string.part) + " " + currentPage.getPart();
                    header_part.setText(header_part_text);

                    // Chỉ cập nhật nếu không phải là trang giới thiệu ("start_part")
                    if (currentPage.getType() != 0) {
                        // Cập nhật text cho header_part_number
                        header_part_number.setVisibility(View.VISIBLE);
                        header_part_number.setText(currentPage.getPartHeader());

                        if(currentPage.getQuestionCount() <= 1) {
                            header_spe_bottom.setVisibility(View.GONE);
                            header_part_full_bottom.setVisibility(View.GONE);

                            header_spe.setVisibility(View.VISIBLE);
                            header_part_full.setVisibility(View.VISIBLE);
                        }
                        else {
                            header_spe.setVisibility(View.GONE);
                            header_part_full.setVisibility(View.GONE);

                            header_spe_bottom.setVisibility(View.VISIBLE);
                            header_part_full_bottom.setVisibility(View.VISIBLE);
                        }

                    } else {
                        header_part_number.setVisibility(View.GONE);
                        header_spe.setVisibility(View.GONE);
                        header_spe_bottom.setVisibility(View.GONE);
                        header_part_full.setVisibility(View.GONE);
                        header_part_full_bottom.setVisibility(View.GONE);
                    }
                }
            }
        });

        getListPart_addListPage();
    }

    private void initValue() {
        backIcon = findViewById(R.id.backIcon);

        header_submit = findViewById(R.id.header_submit);
        header_submit.setPaintFlags(header_submit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        viewpager = findViewById(R.id.viewpager);
        header_time = findViewById(R.id.header_time);
        header_part = findViewById(R.id.header_part);
        header_spe = findViewById(R.id.header_spe);
        header_spe_bottom = findViewById(R.id.header_spe_bottom);
        header_part_full_bottom = findViewById(R.id.header_part_full_bottom);
        header_part_full = findViewById(R.id.header_part_full);
        header_part_number = findViewById(R.id.header_part_number);
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
                "test_id": 1,
                "first_question_id": "test1_1",
                "question_count": 1
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
                                detailPage.setPart(jsonObject.getInt("part_id"));
                                editTestDetailPage(detailPage, jsonObject.getInt("part_id"));

                                list_page.add(detailPage);

                                partDefault = jsonObject.getInt("part_id");
                            }

                            testDetail_page detailPage = new testDetail_page();
                            detailPage.setPart(jsonObject.getInt("part_id"));

                            detailPage.setGroupQuestionId(jsonObject.getString("group_question_id"));

                            int first_question = StringManager.extractLastNumber(jsonObject.getString("first_question_id"));
                            int question_count = jsonObject.getInt("question_count");
                            detailPage.setPartHeader(first_question, question_count);

                            // set type
                            if(jsonObject.getInt("part_id") <= 4)
                                detailPage.setType("listening");
                            else
                                detailPage.setType("reading");

                            // add list imageurl
                            ArrayList<String> listImageUrl = new ArrayList<>();

                            if(!jsonObject.isNull("url_image1"))
                                listImageUrl.add(jsonObject.getString("url_image1"));

                            for(int index = 1; index <= 5; index++) {
                                String urlImageText = "url_image" + (index + 1);

                                if(!jsonObject.isNull(urlImageText))
                                    listImageUrl.add(jsonObject.getString(urlImageText));
                                else
                                    break;
                            }

                            detailPage.setListImageUrl(listImageUrl);

                            // add audio url
                            if(!jsonObject.isNull("url_audio"))
                                detailPage.setAudioUrl(jsonObject.getString("url_audio"));

                            list_page.add(detailPage);
                        }

                        // thêm danh sách vào viewpager
                        testDetailAdapter = new TestDetailAdapter(list_page, viewpager);
                        viewpager.setAdapter(testDetailAdapter);

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
    }

    private void initCountTime(int time) {
        countdownHelper = new CountdownHelper(new CountdownListener() {
            @Override
            public void onTick(String formattedTime) {
                header_time.setText(formattedTime);
            }

            @Override
            public void onFinish() {
               Log.e("Time", "Time countdown end");
                // Thêm logic khi hết giờ
            }
        });

        countdownHelper.startCountdown((long) time * 60 * 1000); // 2 tiếng
    }

    @Override
    protected void onStop() {
        super.onStop();

        finish();
    }

    @Override
    protected void onDestroy() {
        if (testDetailAdapter != null) {
            testDetailAdapter.release();
        }
        AudioTestManager.releaseAll();
        super.onDestroy();
    }
}
