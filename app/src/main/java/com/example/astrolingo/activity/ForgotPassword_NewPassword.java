package com.example.astrolingo.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.Service.UtilService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ForgotPassword_NewPassword extends AppCompatActivity {
    SharedPreferenceClass sharedPreClass;
    UtilService utilService;
    TextView input_password, input_passwordAu;
    TextView error_password, error_passwordAu;
    ImageView backIcon;
    ConstraintLayout next_button;
    ProgressBar progressBar;

    String password, passwordAu;
    String emailSend, otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.forgotpassword_newpassword);

        input_password = findViewById(R.id.input_password);
        input_passwordAu = findViewById(R.id.input_repassword);

        error_password = findViewById(R.id.error_password);
        error_passwordAu = findViewById(R.id.error_repassword);

        next_button = findViewById(R.id.button_forgotPassword);
        progressBar = findViewById(R.id.progressBar);

        backIcon = findViewById(R.id.backIcon);

        // init
        utilService = new UtilService();
        sharedPreClass = new SharedPreferenceClass(this);

        // header_text.setText(getText(R.string.registerPage));

        emailSend = getIntent().getStringExtra("email");
        otp = getIntent().getStringExtra("otp");

        inVisibiltyError();

        // click button
        next_button.setOnClickListener(v -> {
            // tắt keyboard
            utilService.hideKeyboard(v, ForgotPassword_NewPassword.this);

            password = input_password.getText().toString();
            passwordAu = input_passwordAu.getText().toString();

            if(validate(v)) {
                forgotPassword_newPassword();
            }

        });

        // back icon
        backIcon.setOnClickListener(v -> {
            finish();
        });
    }

    private void forgotPassword_newPassword() {
        progressBar.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("email", emailSend);
        params.put("otp", otp);
        params.put("newPassword", password);

        String apiRegister = getString(R.string.api_key) + "api/forgot_password/reset";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiRegister,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")) {
                                String token = response.getString("token");
                                String user_id = response.getString("user_id");

                                sharedPreClass.setValue_string("token", token);
                                sharedPreClass.setValue_string("user_id", user_id);

                                Toast.makeText(ForgotPassword_NewPassword.this, response.getString("msg"), Toast.LENGTH_SHORT).show();

                                Intent resultIntent = new Intent(ForgotPassword_NewPassword.this, SuccessActivity.class);
                                resultIntent.putExtra("text", "password");
                                startActivity(resultIntent);
                            }

                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException je) {
                            je.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if(error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                                JSONObject obj = new JSONObject(res);

                                // thông báo
                                Toast.makeText(ForgotPassword_NewPassword.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.GONE);
                            } catch (JSONException | UnsupportedEncodingException je) {
                                je.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // set retry policy
        int socketTime = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public boolean validate(View view) {
        boolean isValid = true;

        if(TextUtils.isEmpty(password))
        {
            isValid = false;
            error_password.setText(getString(R.string.error_password_empty));
            error_password.setVisibility(View.VISIBLE);
        }
        else {
            if(password.length() < 8) {
                isValid = false;
                error_password.setText(getString(R.string.error_password_invalid));
                error_password.setVisibility(View.VISIBLE);
            }
        }

        if(TextUtils.isEmpty(passwordAu))
        {
            isValid = false;
            error_passwordAu.setText(getString(R.string.error_repassword_empty));
            error_passwordAu.setVisibility(View.VISIBLE);
        }
        else {
            if(!password.equals(passwordAu)) {
                isValid = false;
                error_passwordAu.setText(getString(R.string.error_repassword_invalid));
                error_passwordAu.setVisibility(View.VISIBLE);
            }
        }

        return isValid;
    }

    public void inVisibiltyError() {
        progressBar.setVisibility(View.GONE);
        error_password.setVisibility(View.INVISIBLE);
        error_passwordAu.setVisibility(View.INVISIBLE);

        input_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    error_password.setVisibility(View.INVISIBLE);
                }
            }
        });

        input_passwordAu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    error_passwordAu.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);

//        if(SharedPreferenceClass.isAllowToken) {
//
//            if(!Objects.equals(sharedPreClass.getValue_string("user_id"), "null")) {
//                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
//                finish();
//            }
//        }
    }

}
