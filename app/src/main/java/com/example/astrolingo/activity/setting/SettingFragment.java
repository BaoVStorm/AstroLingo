package com.example.astrolingo.activity.setting;

import static androidx.core.app.ActivityCompat.recreate;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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

import com.bumptech.glide.Glide;
import com.example.astrolingo.R;

// API library
import com.example.astrolingo.Service.LocaleHelper;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.activity.LoginActivity;
import com.example.astrolingo.activity.MainActivity;
import com.example.astrolingo.api.UserApi;

import com.android.volley.VolleyError;
import com.android.volley.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class SettingFragment extends Fragment {
    SharedPreferenceClass sharedPreClass;
    private JSONObject userObject;
    TextView logout_field;
    LinearLayout btnChinhSuaHoSo;
    ImageView avatarImageView;
    TextView usernameTextView, coinTextView;
    TextView headerText;
    TextView languageTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout page_setting vào fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // init profile
        if (getActivity() != null) {
            headerText = getActivity().findViewById(R.id.format_textView);
            if (headerText != null) {
                headerText.setText(getString(R.string.settingFragment));
            }
        }

        // Khởi tạo SharedPreferenceClass
        sharedPreClass = new SharedPreferenceClass(view.getContext());

        // Khởi tạo các thành phần trong layout
        logout_field = view.findViewById(R.id.logoutTextView);
        btnChinhSuaHoSo = view.findViewById(R.id.btnChinhSuaHoSo);
        avatarImageView = view.findViewById(R.id.avatarImageView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        coinTextView = view.findViewById(R.id.coinTextView);
        languageTextView = view.findViewById(R.id.languageTextView);

        // set up underline
        logout_field.setPaintFlags(logout_field.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

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
//                btnChinhSuaHoSo.setBackgroundColor(Color.parseColor("#9F7EFF"));

                // Mở activity ChinhSuaHoSoActivity
                Intent intent = new Intent(getActivity(), ChinhSuaHoSoActivity.class);
                intent.putExtra("user_data", userObject.toString());  // convert Json thành chuỗi
                startActivity(intent);
            }
        });

        // insert UserAPI
        insertUserAPI(view);

        languageTextView.setOnClickListener(v -> {
            LocaleHelper.toggleLanguage(this.getContext());

            if (getActivity() != null) {
                getActivity().recreate();
            }
        });

        return view;
    }

    // logout user
    private void logOut() {
        sharedPreClass.clearAll();
        Intent intent = new Intent(getActivity(), LoginActivity.class);

        Toast.makeText(getActivity(), "User Log out! Please login again!", Toast.LENGTH_SHORT).show();

        startActivity(intent);
        getActivity().finish();
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
//                            avatarImageView.setImageURI(Uri.parse(userObject.getString("photo_url")));

                            Glide.with(view.getContext())
                                    .load(userObject.getString("photo_url"))
                                    .placeholder(R.drawable.icon_ava2) // hình mặc định nếu chưa có ảnh
                                    .error(R.drawable.icon_ava2)         // hình nếu lỗi tải
                                    .into(avatarImageView);
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
