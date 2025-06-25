package com.example.astrolingo.activity.home.history_test;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.activity.home.history.historyWordTranslatedMainActivity;
import com.example.astrolingo.apdapter.home.history.history_word_adapter;
import com.example.astrolingo.apdapter.home.history_test.history_test_adapter;
import com.example.astrolingo.api.CertificateApi;
import com.example.astrolingo.api.UserLookupHistoryApi;
import com.example.astrolingo.domain.home.history.history_word;
import com.example.astrolingo.domain.home.history_test.history_test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class historyTestMainActivity extends AppCompatActivity {
    private SharedPreferenceClass sharedPreClass;

    private View header;
    private ImageView backIcon;
    private TextView header_title;
    private ArrayList<history_test> historyTests = new ArrayList<>();
    private ListView lv_testResults;
    private history_test_adapter historyTestAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_history_test);

        // init
        sharedPreClass = new SharedPreferenceClass(this);

        header = findViewById(R.id.header);

        backIcon = header.findViewById(R.id.backIcon);
        header_title = header.findViewById(R.id.header_title);

        lv_testResults = findViewById(R.id.lv_testResults);

        // init
        header_title.setText(getString(R.string.historyTest_header));

        // get ListHistoryWord
        getListHistoryTest();

        // backIcon
        backIcon.setOnClickListener(v ->{
            // history_words.clear();
            finish();
        });
    }

    private void getListHistoryTest() {

//        "msg": "Certificates fetched successfully",
//        "certificates": [
//            {
//                "_id": "682fdc2a04934c492068e088",
//                    "test_id": 1,
//                    "user_id": "682609861e4d02c654d9a74a",
//                    "certificate_name": "Test 1 ETS 2024",
//                    "reading_score": 0,
//                    "listening_score": 0,
//                    "total_score": 0,
//                    "correct_count": 0,
//                    "wrong_count": 200,
//                    "awarded_at": "2025-05-23T02:23:38.134Z",
//                    "createdAt": "2025-05-23T02:23:38.136Z",
//                    "updatedAt": "2025-05-23T02:23:38.136Z",
//                    "__v": 0,
//                    "awarded_at_vietnam": "23/05/2025 | 09:23:38"
//            },

        historyTests.clear();
        CertificateApi.getCertificate(
                sharedPreClass.getValue_string("user_id"),
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý khi thành công
                        try {
                            JSONArray jsonArray = response.getJSONArray("certificates");

                            for(int i = 0; i < jsonArray.length(); i ++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String test_id = jsonObject.getString("test_id");
                                String certificate_name = jsonObject.getString("certificate_name");
                                String awarded_at = jsonObject.getString("awarded_at_vietnam");

                                int listening_score = jsonObject.getInt("listening_score");
                                int total_score = jsonObject.getInt("total_score");
                                int reading_score = jsonObject.getInt("reading_score");
                                int correct_count = jsonObject.getInt("correct_count");
                                int wrong_count = jsonObject.getInt("wrong_count");

                                history_test historyTest = new history_test(
                                        test_id,
                                        certificate_name,
                                        awarded_at,
                                        listening_score,
                                        total_score,
                                        reading_score,
                                        correct_count,
                                        wrong_count
                                );

                                historyTests.add(historyTest);
                            }

                            historyTestAdapter = new history_test_adapter(historyTestMainActivity.this, historyTests);
                            // historyTestAdapter.setUserId(sharedPreClass.getValue_string("user_id"), sharedPreClass.getValue_string("token"));
                            lv_testResults.setAdapter(historyTestAdapter);

                        } catch (JSONException e) {
                            Toast.makeText(historyTestMainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý khi có lỗi
                        Toast.makeText(historyTestMainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
