package com.example.astrolingo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.astrolingo.function.StringManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//import com.chaos.view.PinView;


public class ForgotPasswordActivity extends AppCompatActivity {
    UtilService utilService;

    TextView input_text;
    TextView error_text;
    ConstraintLayout button_next;
    ProgressBar progressBar;
    ImageView backIcon;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.forgotpassword);

        utilService = new UtilService();

        input_text = findViewById(R.id.input_username);
        button_next = findViewById(R.id.button_forgotPassword);
        error_text = findViewById(R.id.error_password);
        progressBar = findViewById(R.id.progressBar);
        backIcon = findViewById(R.id.backIcon);

        inVisibiltyError();

        button_next.setOnClickListener(v -> {
            // tắt keyboard
            utilService.hideKeyboard(v, ForgotPasswordActivity.this);

            email = input_text.getText().toString();

            if(validate(v)) {
                forgotPassword();
            }
        });

        backIcon.setOnClickListener(v -> {
            finish();
        });
    }

    private void forgotPassword() {
        progressBar.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);

        String apiRegister = getString(R.string.api_key) + "api/forgot_password/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiRegister,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(ForgotPasswordActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();

                            Intent resultIntent = new Intent(ForgotPasswordActivity.this, ForgotPassword_VerifyEmailActivity.class);
                            resultIntent.putExtra("email", email);
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
                        Log.e("saa", "ass");

                        NetworkResponse response = error.networkResponse;
                        if(error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                                JSONObject obj = new JSONObject(res);

                                // thông báo
                                Toast.makeText(ForgotPasswordActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();

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
        int socketTime = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private boolean validate(View v) {
        boolean isValid = true;

        if(TextUtils.isEmpty(email)) {
            isValid = false;
            error_text.setText(getString(R.string.error_email_empty));
            error_text.setVisibility(View.VISIBLE);
        }
        else {
            if(!StringManager.isValidEmail(email)) {
                isValid = false;
                error_text.setText(getString(R.string.error_email_invalid));
                error_text.setVisibility(View.VISIBLE);
            }
        }

        return isValid;
    }

    public void inVisibiltyError() {
        progressBar.setVisibility(View.GONE);
        error_text.setVisibility(View.INVISIBLE);

        input_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    error_text.setVisibility(View.INVISIBLE);
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
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                finish();
//            }
//        }
    }
}

