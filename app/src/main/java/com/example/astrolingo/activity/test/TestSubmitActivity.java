package com.example.astrolingo.activity.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.AnswerTestMananger;
import com.example.astrolingo.Service.DateUtils;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.api.UserApi;

import org.json.JSONException;
import org.json.JSONObject;

public class TestSubmitActivity extends AppCompatActivity {
    private SharedPreferenceClass sharedPreClass;
    private JSONObject userObject;
    private TextView header_title, name_text, gender_text, country_text, DOB_text;
    private TextView test_date_title, test_date_content, valid_date_title, valid_date_content, name_test;
    private TextView listening_score, reading_score, total_score, pass_score;
    private ImageView backIcon, imgAvatar;
    private JSONObject testObject;
    private int numberQuestion, numberCorrect, numberWrong, numberCorrect_Listening, numberCorrect_Reading;
    String currentDate, validDate;

    ConstraintLayout button_gotIt, button_result;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_test_submit);

        String testString = getIntent().getStringExtra("testObject");

        sharedPreClass = new SharedPreferenceClass(this);

        header_title = findViewById(R.id.header_title);
        name_text = findViewById(R.id.name_text);
        gender_text = findViewById(R.id.gender_title);
        country_text = findViewById(R.id.country_text);
        DOB_text = findViewById(R.id.DOB_text);
        imgAvatar = findViewById(R.id.imgAvatar);

        name_test = findViewById(R.id.name_test);
        test_date_content = findViewById(R.id.test_date_content);
        valid_date_content = findViewById(R.id.valid_date_content);

        listening_score = findViewById(R.id.listening_score);
        reading_score = findViewById(R.id.reading_score);
        total_score = findViewById(R.id.total_score);
        pass_score = findViewById(R.id.pass_score);

        button_gotIt = findViewById(R.id.button_gotIt);
        button_result = findViewById(R.id.button_result);

        // init value
        header_title.setText("Test: Exam results");

        try {
            testObject = new JSONObject(testString);

            name_test.setText(testObject.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
            finish();
        }

        // edit user info
        getUserInfo();

        // get Date
        currentDate = DateUtils.getCurrentDate();
        validDate = DateUtils.calculateDate(2, 0, 0); // +2 năm

        test_date_content.setText(currentDate);
        valid_date_content.setText(validDate);

        // score test
        scoreTest();

        // set event button
        button_gotIt.setOnClickListener(v -> {
            finish();
        });

        button_result.setOnClickListener(v -> {
            saveTestScore();
        });
    }

    private void saveTestScore() {

    }

    private void getUserInfo() {
        UserApi.getUserSchema(
                this,
                sharedPreClass.getValue_string("token"),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý khi thành công
                        try {
                            userObject = response.getJSONObject("user");

                            updateViewUser();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý khi có lỗi
                        Log.e("API_ERROR", error.toString());
                        Toast.makeText(TestSubmitActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );


    }

    private void updateViewUser() {
        try {
            if(!userObject.isNull("photo_url")) {
//                avatarImageView.setImageURI(Uri.parse(userObject.getString("photo_url")));

                Glide.with(this.getApplicationContext())
                        .load(userObject.getString("photo_url"))
                        .placeholder(R.drawable.icon_ava2) // hình mặc định nếu chưa có ảnh
                        .error(R.drawable.icon_ava2)         // hình nếu lỗi tải
                        .into(imgAvatar);
            }

            name_text.setText(userObject.getString("full_name"));

            if(!userObject.isNull("gender"))
                gender_text.setText(userObject.getString("gender"));

            if(!userObject.isNull("date_of_birth")) {
                String date = DateUtils.formatDate(userObject.getString("date_of_birth"));
                DOB_text.setText(date);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void scoreTest() {
        numberQuestion = AnswerTestMananger.getNumberOfQuestion();
        numberWrong = AnswerTestMananger.getNumberOfWrong();

        numberCorrect = AnswerTestMananger.getNumberOfCorrect();
        numberCorrect_Listening = AnswerTestMananger.getNumberOfCorrect_Listening();
        numberCorrect_Reading = AnswerTestMananger.getNumberOfCorrect_Reading();

        // edit score view
        listening_score.setText(numberCorrect_Listening + "/" + (numberQuestion / 2));
        reading_score.setText(numberCorrect_Reading + "/" + (numberQuestion / 2));
        total_score.setText(numberCorrect + "/" + numberQuestion);

        pass_score.setText(AnswerTestMananger.getScore() + "");

    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        finish();
//    }
}
