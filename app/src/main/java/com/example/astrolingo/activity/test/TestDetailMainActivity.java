package com.example.astrolingo.activity.test;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.AnswerTestMananger;
import com.example.astrolingo.Service.AudioTestManager;
import com.example.astrolingo.Service.CountdownHelper;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.apdapter.test.FilterQuestionAdapter;
import com.example.astrolingo.apdapter.test.QuestionAdapter;
import com.example.astrolingo.apdapter.test.TestDetailAdapter;
import com.example.astrolingo.domain.test.AudioState;
import com.example.astrolingo.domain.test.nav_answer;
import com.example.astrolingo.domain.test.testDetail_page;

import com.example.astrolingo.api.TestApi;
import com.example.astrolingo.function.CountdownListener;
import com.example.astrolingo.function.StringManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestDetailMainActivity extends AppCompatActivity  {
    SharedPreferenceClass sharedPreClass;
    ViewPager2 viewpager;
    JSONObject testObject;
    String testString;
    TextView header_time, header_part_full, header_part_number, header_spe, header_part, header_part_full_bottom, header_submit;
    ImageView header_info, header_setting, header_pause, header_overview;
    ImageView backIcon;
    View header_spe_bottom;
    List<testDetail_page> list_page;
    TestDetailAdapter testDetailAdapter;
    JSONArray partJSONArray;
    int lastAudioPosition = -1;
    CountdownHelper countdownHelper;

    Dialog dialog_info, dialog_pause, dialog_submit;
    BottomSheetDialog bottomDialog_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_test_detail_main);

        sharedPreClass = new SharedPreferenceClass(this);
        // init
        initValue();

        initDialog();

        // add temp value
        list_page = new ArrayList<>();

        // get test object
        testString = getIntent().getStringExtra("testObject");

        try {
            testObject = new JSONObject(testString);
        } catch (JSONException e) {
            e.printStackTrace();
            exitActivity();
        }

        initCountTime(Integer.parseInt(testObject.optString("test_time")));

        // init value
        header_part_full.setText(testObject.optString("question_number"));
        header_part_full_bottom.setText(testObject.optString("question_number"));

        // set backIcon
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(testDetailAdapter != null)
//                    testDetailAdapter.release();
//
//                exitActivity();
                dialog_pause.show();
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

        // set listener icon
        setListenerIcon();

        // edit bottom Dialog filter
        header_overview.setVisibility(View.GONE);
        int test_id = 0;
        try {
            test_id = testObject.getInt("test_id");
            getAndAddQuestionOfTest(test_id);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        // edit submit dialog
        editPauseDialog();

        // edit submit dialog
        editSubmitDialog();
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

        // init icon header
        header_info = findViewById(R.id.header_info);
        header_setting = findViewById(R.id.header_setting);
        header_pause = findViewById(R.id.header_pause);
        header_overview = findViewById(R.id.header_overview);
    }

    private void initDialog() {
        dialog_info = new Dialog(this);
        dialog_info.setContentView(R.layout.page_test_detail_dialog_info);
        dialog_info.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_info.getWindow().setBackgroundDrawable(getDrawable(R.drawable.page_test_detail_dialog_info_bg));
        dialog_info.setCanceledOnTouchOutside(true);
//        dialog_info.dismiss();
//        dialog_info.show();

        dialog_pause = new Dialog(this);
        dialog_pause.setContentView(R.layout.page_test_detail_dialog_pause);
        dialog_pause.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_pause.getWindow().setBackgroundDrawable(getDrawable(R.drawable.page_test_detail_dialog_info_bg));
        dialog_pause.setCanceledOnTouchOutside(true);

        dialog_submit = new Dialog(this);
        dialog_submit.setContentView(R.layout.page_test_detail_dialog_submit);
        dialog_submit.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_submit.getWindow().setBackgroundDrawable(getDrawable(R.drawable.page_test_detail_dialog_info_bg));
        dialog_submit.setCanceledOnTouchOutside(true);

        bottomDialog_filter = new BottomSheetDialog(this);
        bottomDialog_filter.setContentView(R.layout.page_test_detail_dialog_filter);
//        bottomDialog_filter.getWindow().setBackgroundDrawable(getDrawable(R.drawable.page_test_detail_dialog_info_bg));
        bottomDialog_filter.setCanceledOnTouchOutside(true);

        // ngăn lướt xuống dialog
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
    }

    private void setListenerIcon() {
        header_info.setOnClickListener( v -> {
            dialog_info.show();
        });

        header_pause.setOnClickListener( v -> {
            dialog_pause.show();
        });

        header_overview.setOnClickListener( v -> {
            bottomDialog_filter.show();
        });

        header_submit.setOnClickListener( v -> {
            dialog_submit.show();
        });
    }
    private void editSubmitDialog() {
        ConstraintLayout button_next = dialog_submit.findViewById(R.id.button_next);
        ConstraintLayout button_exit = dialog_submit.findViewById(R.id.button_exit);

        button_next.setOnClickListener(v -> {
            submitTest();
        });

        button_exit.setOnClickListener(v -> {
            dialog_submit.dismiss();
        });
    }

    private void editPauseDialog() {
        ConstraintLayout button_next = dialog_pause.findViewById(R.id.button_next);
        ConstraintLayout button_exit = dialog_pause.findViewById(R.id.button_exit);

        button_next.setOnClickListener(v -> {
            dialog_pause.dismiss();
        });

        button_exit.setOnClickListener(v -> {
            exitActivity();
        });
    }

    private void editBottomDialogFilter() {
        header_overview.setVisibility(View.VISIBLE);

        ListView questionListView = bottomDialog_filter.findViewById(R.id.lv_question);

        FilterQuestionAdapter adapter = new FilterQuestionAdapter(this, AnswerTestMananger.list_answer);
        adapter.setViewPager(viewpager);
        adapter.setBottomDialog_filter(bottomDialog_filter);
        questionListView.setAdapter(adapter);

        ConstraintLayout filter_listening = bottomDialog_filter.findViewById(R.id.filter_listening);
        ConstraintLayout filter_reading = bottomDialog_filter.findViewById(R.id.filter_reading);
        ConstraintLayout filter_all = bottomDialog_filter.findViewById(R.id.filter_all);

        View group_listening_highlight = bottomDialog_filter.findViewById(R.id.group_listening_highlight);
        View group_reading_highlight = bottomDialog_filter.findViewById(R.id.group_reading_highlight);
        View group_all_highlight = bottomDialog_filter.findViewById(R.id.group_all_highlight);

        LinearLayout box_type = bottomDialog_filter.findViewById(R.id.box_type);
        assert box_type != null;

        // init view
        box_type.setVisibility(View.GONE);
        group_listening_highlight.setVisibility(View.GONE);
        group_reading_highlight.setVisibility(View.GONE);
        group_all_highlight.setVisibility(View.VISIBLE);

        // Event type
        assert filter_listening != null;
        filter_listening.setOnClickListener(v -> {
            adapter.filterListening();
            questionListView.post(() -> questionListView.smoothScrollToPosition(0));

            group_listening_highlight.setVisibility(View.VISIBLE);
            group_reading_highlight.setVisibility(View.GONE);
            group_all_highlight.setVisibility(View.GONE);
        });

        assert filter_reading != null;
        filter_reading.setOnClickListener(v -> {
            adapter.filterReading();
            questionListView.post(() -> questionListView.smoothScrollToPosition(0));

            group_listening_highlight.setVisibility(View.GONE);
            group_reading_highlight.setVisibility(View.VISIBLE);
            group_all_highlight.setVisibility(View.GONE);
        });

        assert filter_all != null;
        filter_all.setOnClickListener(v -> {
            adapter.resetFilter();
            questionListView.post(() -> questionListView.smoothScrollToPosition(0));

            group_listening_highlight.setVisibility(View.GONE);
            group_reading_highlight.setVisibility(View.GONE);
            group_all_highlight.setVisibility(View.VISIBLE);
        });
    }

    private void submitTest() {
        if(dialog_info != null)
            dialog_info.dismiss();

        if(dialog_pause != null)
            dialog_pause.dismiss();

        if(dialog_submit != null)
            dialog_submit.dismiss();

        if(bottomDialog_filter != null)
            bottomDialog_filter.dismiss();

        Intent intent = new Intent(this, TestSubmitActivity.class);
        intent.putExtra("testObject", testString);
        startActivity(intent);

        finish();
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

    private int position = -1;
    private String lastestGroupQuestion = "";

    private void getAndAddQuestionOfTest(int test_id) {

//        [
//            {
//                "_id": "68206440a87bed1c0357e798",
//                "question_id": "test1_1",
//                "group_question_id": "test1_part1_1",
//                "question_text": "Look at the picture marked number 1 in your test book",
//                "correct_answer": 4,
//                "ans_1": "A",
//                "ans_2": "B",
//                "ans_3": "C",
//                "ans_4": "D"
//            }
//        ]
        AnswerTestMananger.releaseAll();
//        AnswerTestMananger.list_answer.add(new nav_answer(0, 0));

        TestApi.getListQuestionByTestId(test_id,
                this,
                sharedPreClass.getValue_string("token"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        // Xử lý khi thành công
                        try {

                            for(int i = 0; i < jsonArray.length(); i++) {
                                if(jsonArray.isNull(i)) {
                                    throw new JSONException("Null value");
                                }

                                JSONObject object = jsonArray.getJSONObject(i);

                                String group_question_id = object.getString("group_question_id");
                                int question_id = StringManager.extractLastNumber(object.getString("question_id"));
                                int part_id = StringManager.extractNumberFromSecondPart(group_question_id);
                                int count_ans = 0;
                                int correct_ans = 1;

                                if(!object.isNull("ans_1"))
                                    count_ans++;

                                if(!object.isNull("ans_2"))
                                    count_ans++;

                                if(!object.isNull("ans_3"))
                                    count_ans++;

                                if(!object.isNull("ans_4"))
                                    count_ans++;

                                if(!object.isNull("correct_answer"))
                                    correct_ans = object.getInt("correct_answer");

                                if(!group_question_id.equals(lastestGroupQuestion)) {
                                    lastestGroupQuestion = group_question_id;
                                    position++;
                                }

                                nav_answer navAnswer = new nav_answer(0, count_ans);
                                navAnswer.setCorrectAnswer(correct_ans);
                                navAnswer.setInfo(part_id, question_id);
                                navAnswer.setGroup_question_id(group_question_id);
                                navAnswer.setQuestion_id_text(object.getString("question_id"));
                                navAnswer.setPosition(position);

//                            AnswerTestMananger.map_answer.put(question_id, navAnswer);
                                AnswerTestMananger.list_answer.add(navAnswer);
                            }

                            editBottomDialogFilter();
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
                submitTest();
            }
        });

        countdownHelper.startCountdown((long) time * 60 * 1000); // 2 tiếng
    }

    private void exitActivity() {
        if(dialog_info != null)
            dialog_info.dismiss();

        if(dialog_pause != null)
            dialog_pause.dismiss();

        if(dialog_submit != null)
            dialog_submit.dismiss();

        if(bottomDialog_filter != null)
            bottomDialog_filter.dismiss();

        AudioTestManager.releaseAll();
        AnswerTestMananger.releaseAll();

        finish();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        finish();
//    }

    @Override
    protected void onDestroy() {
        if (testDetailAdapter != null) {
            testDetailAdapter.release();
        }
        AudioTestManager.releaseAll();
        super.onDestroy();
    }
}
