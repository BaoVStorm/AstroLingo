package com.example.astrolingo.activity.setting;

import android.net.Uri;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.Service.UtilService;
import com.example.astrolingo.activity.home.translate.translateDetailActivity;
import com.example.astrolingo.api.UserApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ChinhSuaHoSoActivity extends AppCompatActivity {
    SharedPreferenceClass sharedPreClass;
    private JSONObject userObject;
    private ImageView avatarImageView, backIcon;
    TextView usernameTextView, coinTextView, txtEmail;
    EditText editFullName, editPhone, editDate;
    Spinner spinnerGender;

    Button btnEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_setting_chinhsuahoso);

        // Khởi tạo SharedPreferenceClass
        sharedPreClass = new SharedPreferenceClass(this);

        // get view
        avatarImageView = findViewById(R.id.imgAvatar);
        usernameTextView = findViewById(R.id.txtUsername);
        coinTextView = findViewById(R.id.txtScore);
        editFullName = findViewById(R.id.editFullName);
        txtEmail = findViewById(R.id.txtEmail);
        editPhone = findViewById(R.id.editPhone);
        editDate = findViewById(R.id.editDate);
        spinnerGender = findViewById(R.id.spinnerGender);
        backIcon = findViewById(R.id.backIcon);

        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> saveEditedInfo());

        // get data from intent
        String jsonString = getIntent().getStringExtra("user_data");

        // Cài đặt giới tính
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this, R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // set default value
        setDefaultValue(jsonString);

        // Xử lý chọn ngày
        editDate.setOnClickListener(v -> showDatePicker());

        // Xử lý back
        backIcon.setOnClickListener(v -> {
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

    }

    private void setDefaultValue(String jsonString) {
        editPhone.setText("");
        editDate.setText("");

        try {
            this.userObject = new JSONObject(jsonString);
            //JSONObject userObject = new JSONObject(jsonString);

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
            editFullName.setText(userObject.getString("full_name"));
            txtEmail.setText(userObject.getString("email"));


            if(!userObject.isNull("phone_number"))
                editPhone.setText(userObject.optString("phone_number", ""));

            if(!userObject.isNull("date_of_birth"))
                editDate.setText(userObject.getString("date_of_birth").split("T")[0]);

            // Giới tính
            String gender = userObject.optString("gender", "Other");
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerGender.getAdapter();
            int position = adapter.getPosition(gender);
            spinnerGender.setSelection(position);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    editDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Button chỉnh sửa
    private void saveEditedInfo() {
        String fullName = editFullName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String dateOfBirth = editDate.getText().toString().trim();

        String[] genderArrayEn = getResources().getStringArray(R.array.fit_gender_array);  // Lấy mảng gender bằng tiếng Anh

        String gender = genderArrayEn[spinnerGender.getSelectedItemPosition()];;

        if (fullName.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            userObject.put("user_id", sharedPreClass.getValue_string("user_id"));
            userObject.put("full_name", fullName);
            userObject.put("phone_number", phone);
            userObject.put("date_of_birth", dateOfBirth);

            userObject.put("gender", gender);

            Log.d("userObject", userObject.toString());

            // TODO: Nếu có API backend thì gọi tại đây để cập nhật
            UserApi.updateUserInfo(
                    userObject,
                    this,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(ChinhSuaHoSoActivity.this, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý khi có lỗi
                            Log.e("API_ERROR", error.toString());
                            Toast.makeText(ChinhSuaHoSoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Chỉnh sửa thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        UtilService UtilService = new UtilService();

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                int[] screenCoords = new int[2];
                view.getLocationOnScreen(screenCoords);
                float x = ev.getRawX() + view.getLeft() - screenCoords[0];
                float y = ev.getRawY() + view.getTop() - screenCoords[1];

                if (x < view.getLeft() || x > view.getRight() ||
                        y < view.getTop() || y > view.getBottom()) {
                    UtilService.hideKeyboard(view, this);
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}
