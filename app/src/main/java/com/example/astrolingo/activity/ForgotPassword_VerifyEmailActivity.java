package com.example.astrolingo.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.chaos.view.PinView;
import com.example.astrolingo.R;
import com.example.astrolingo.Service.UtilService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ForgotPassword_VerifyEmailActivity extends AppCompatActivity {

    UtilService utilService;

    TextView header_text, your_email;
    TextView error_text, resend_field;
    ImageView backIcon;
    PinView pinView;
    ConstraintLayout next_button;
    ProgressBar progressBar;

    String emailSend, digit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.forgotpassword_verifyemail);

        header_text = findViewById(R.id.header_text);
        your_email = findViewById(R.id.your_email);
        error_text = findViewById(R.id.error_text);
        pinView = findViewById(R.id.pinView);
        next_button = findViewById(R.id.button_forgotPassword);
        progressBar = findViewById(R.id.progressBar);
        resend_field = findViewById(R.id.resend_field);
        backIcon = findViewById(R.id.backIcon);

        // init
        utilService = new UtilService();

        // header_text.setText(getText(R.string.registerPage));

        emailSend = getIntent().getStringExtra("email");
        your_email.setText(emailSend);

        progressBar.setVisibility(View.GONE);

        inVisibiltyError();

        // click button
        next_button.setOnClickListener(v -> {
            // tắt keyboard
            utilService.hideKeyboard(v, ForgotPassword_VerifyEmailActivity.this);

            digit = pinView.getText().toString();

            if(validate(v)) {
                verifyAccount();
            }

        });

        // resend email
        resend_field.setOnClickListener(v -> {
            // tắt keyboard
            utilService.hideKeyboard(v, ForgotPassword_VerifyEmailActivity.this);

            resendEmail();
        });

        // back icon
        backIcon.setOnClickListener(v -> {
            finish();
        });
    }

    private void verifyAccount() {
        progressBar.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("email", emailSend);
        params.put("otp", digit);

        String apiRegister = getString(R.string.api_key) + "api/forgot_password/check_valid_digit";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiRegister,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(ForgotPassword_VerifyEmailActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent(ForgotPassword_VerifyEmailActivity.this, ForgotPassword_NewPassword.class);
                            resultIntent.putExtra("email", emailSend);
                            resultIntent.putExtra("otp", digit);
                            startActivity(resultIntent);

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
                                Toast.makeText(ForgotPassword_VerifyEmailActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();

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

//        Log.e("passs", "passs");

        // set retry policy
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void resendEmail() {
        progressBar.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("email", emailSend);

        String apiRegister = getString(R.string.api_key) + "api/email_verification/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiRegister,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(ForgotPassword_VerifyEmailActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(ForgotPassword_VerifyEmailActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();

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

//        Log.e("passs", "passs");

        // set retry policy
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public boolean validate(View view) {
        boolean isValid = true;

        if (digit.length() != 4) {
            // Đã nhập đủ 4 số
            isValid = false;

            error_text.setVisibility(View.VISIBLE);
            error_text.setText(getText(R.string.error_digited_invalid));
        }

        return isValid;
    }

    public void inVisibiltyError() {
        progressBar.setVisibility(View.GONE);
        error_text.setVisibility(View.INVISIBLE);

        pinView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    error_text.setVisibility(View.INVISIBLE);
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
