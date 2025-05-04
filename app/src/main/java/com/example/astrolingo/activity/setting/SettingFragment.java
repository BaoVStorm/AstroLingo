package com.example.astrolingo.activity.setting;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.astrolingo.R;

// API library
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.activity.LoginActivity;
import com.example.astrolingo.api.UserApi;

import com.android.volley.VolleyError;
import com.android.volley.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingFragment extends Fragment {
    SharedPreferenceClass sharedPreClass;
    private JSONObject userObject;
    TextView logout_field;
    LinearLayout btnChinhSuaHoSo;
    ImageView avatarImageView;
    TextView usernameTextView, coinTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout page_setting vào fragment
        View view = inflater.inflate(R.layout.page_setting, container, false);

        // Khởi tạo SharedPreferenceClass
        sharedPreClass = new SharedPreferenceClass(view.getContext());

        // Khởi tạo các thành phần trong layout
        logout_field = view.findViewById(R.id.logoutTextView);
        btnChinhSuaHoSo = view.findViewById(R.id.btnChinhSuaHoSo);
        avatarImageView = view.findViewById(R.id.avatarImageView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        coinTextView = view.findViewById(R.id.coinTextView);

        // Điều chỉnh sự kiện logout
        logout_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        // Gán sự kiện click cho nút "Chỉnh sửa hồ sơ"
        btnChinhSuaHoSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đổi màu nền của nút
                btnChinhSuaHoSo.setBackgroundColor(Color.parseColor("#9F7EFF"));

                // Mở activity ChinhSuaHoSoActivity
                Intent intent = new Intent(getActivity(), ChinhSuaHoSoActivity.class);
                intent.putExtra("user_data", userObject.toString());  // convert Json thành chuỗi
                startActivity(intent);
            }
        });

        // insert UserAPI
        insertUserAPI(view);

        return view;
    }

    // logout user
    private void logOut() {

    }

    // insert userAPI
    private void insertUserAPI(View view) {
        UserApi.getUserSchema(
            view.getContext(),
            sharedPreClass.getValue_string("token"),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Xử lý khi thành công
                    // Log.d("API_SUCCESS", response.toString());
                    try {
                        userObject = response.getJSONObject("user");

                        if(!userObject.isNull("photo_url")) {
                            avatarImageView.setImageURI(Uri.parse(userObject.getString("photo_url")));
                        }

                        usernameTextView.setText(userObject.getString("full_name"));

                        if(userObject.isNull("score"))
                            coinTextView.setText(String.valueOf(0));
                        else
                            coinTextView.setText(String.valueOf(userObject.getInt("score")));

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
                    Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        );


    }



}
