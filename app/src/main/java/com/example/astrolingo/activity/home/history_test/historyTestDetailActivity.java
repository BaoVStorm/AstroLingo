package com.example.astrolingo.activity.home.history_test;

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
import com.example.astrolingo.Service.DateUtils;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.api.UserApi;
import com.example.astrolingo.domain.home.history_test.history_test;

import org.json.JSONException;
import org.json.JSONObject;

public class historyTestDetailActivity extends AppCompatActivity {
    private SharedPreferenceClass sharedPreClass;

    private View header;
    private ImageView backIcon;
    private TextView header_title;

    private TextView name_text, gender_text, country_text, DOB_text;
    private TextView test_date_content, valid_date_content, name_test;
    private TextView listening_score, reading_score, total_score, pass_score;
    private ImageView imgAvatar;
    private int numberQuestion, numberCorrect, numberWrong, numberCorrect_Listening, numberCorrect_Reading;
    private int total_score_test;
    private ConstraintLayout button_gotIt, button_result;
    private history_test word;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_test_submit);

        word = (history_test) getIntent().getSerializableExtra("word");

        // init
        sharedPreClass = new SharedPreferenceClass(this);

        header = findViewById(R.id.header);

        backIcon = header.findViewById(R.id.backIcon);
        header_title = header.findViewById(R.id.header_title);

        backIcon = findViewById(R.id.backIcon);
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

        button_gotIt.setVisibility(View.GONE);
        button_result.setVisibility(View.GONE);

        // init
        header_title.setText(getString(R.string.certificate));

        assert word != null;
        name_test.setText(word.getCertificate_name());

        // score test
        scoreTest();

        setDate();

        // edit user info
        getUserInfo();

        // backIcon
        backIcon.setOnClickListener(v ->{
            // history_words.clear();
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private void scoreTest() {
        numberWrong = word.getWrong_count();
        numberCorrect = word.getCorrect_count();
        numberQuestion = numberWrong + numberCorrect;

        numberCorrect_Listening = word.getListening_score();
        numberCorrect_Reading = word.getReading_score();;

        // edit score view
        listening_score.setText(numberCorrect_Listening + "/" + (numberQuestion / 2));
        reading_score.setText(numberCorrect_Reading + "/" + (numberQuestion / 2));
        total_score.setText(numberCorrect + "/" + numberQuestion);

        total_score_test = Math.min((numberQuestion) * 5 - 10, numberCorrect * 5);
        pass_score.setText(total_score_test + "");
    }

    private void setDate() {
        String curDate = word.getAwarded_at();

        test_date_content.setText(curDate.split(" \\| ")[0]);

        String next2Year = DateUtils.addTwoYears(curDate);
        valid_date_content.setText(next2Year);
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
                            JSONObject userObject = response.getJSONObject("user");

                            updateViewUser(userObject);
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
                        Toast.makeText(historyTestDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );


    }

    private void updateViewUser(JSONObject userObject) {
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

}
