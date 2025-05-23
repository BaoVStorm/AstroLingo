package com.example.astrolingo.activity.setting;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.astrolingo.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ChinhSuaHoSoActivity extends AppCompatActivity {
    private JSONObject userObject;
    private ImageView avatarImageView, backIcon;
    TextView usernameTextView, coinTextView, txtFullName, txtEmail, txtPhone, txtDate, txtGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_setting_chinhsuahoso);

        // get view
        avatarImageView = findViewById(R.id.imgAvatar);
        usernameTextView = findViewById(R.id.txtUsername);
        coinTextView = findViewById(R.id.txtScore);
        txtFullName = findViewById(R.id.txtFullName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtDate = findViewById(R.id.txtDate);
        txtGender = findViewById(R.id.txtGender);
        backIcon = findViewById(R.id.backIcon);

        // get data from intent
        String jsonString = getIntent().getStringExtra("user_data");

        // set default value
        setDefaultValue(jsonString);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        backIcon.setOnClickListener(v -> {
            finish();
        });
    }

    private void setDefaultValue(String jsonString) {
        txtPhone.setText("");
        txtDate.setText("");
        txtGender.setText("Other");

        try {
            JSONObject userObject = new JSONObject(jsonString);

            if(!userObject.isNull("photo_url")) {
//                avatarImageView.setImageURI(Uri.parse(userObject.getString("photo_url")));

                Glide.with(this.getApplicationContext())
                        .load(userObject.getString("photo_url"))
                        .placeholder(R.drawable.icon_ava2) // hình mặc định nếu chưa có ảnh
                        .error(R.drawable.icon_ava2)         // hình nếu lỗi tải
                        .into(avatarImageView);
            }

            if(userObject.isNull("score"))
                coinTextView.setText(String.valueOf(0));
            else
                coinTextView.setText(String.valueOf(userObject.getInt("score")));

            usernameTextView.setText(userObject.getString("user_name"));
            txtFullName.setText(userObject.getString("full_name"));
            txtEmail.setText(userObject.getString("email"));

            if(!userObject.isNull("phone_number"))
                txtPhone.setText(userObject.getString("phone_number"));

            if(!userObject.isNull("date_of_birth"))
                txtDate.setText(userObject.getString("date_of_birth").split("T")[0]);

            if(!userObject.isNull("gender"))
                txtGender.setText(userObject.getString("gender"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
