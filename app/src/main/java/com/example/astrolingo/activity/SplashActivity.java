package com.example.astrolingo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    SharedPreferenceClass sharedPreClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.loginregister_load);

        progressBar = findViewById(R.id.progressBar);
        sharedPreClass = new SharedPreferenceClass(this);

        sharedPreClass.setValue_string("user_id", "null");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }

    protected void VerifyToken() {
        if(!sharedPreClass.contains("token")) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }

        String apiRegister = getString(R.string.api_key) + "api/auth/verify";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiRegister,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")) {
                                String user_id = response.getString("user_id");

                                sharedPreClass.setValue_string("user_id", user_id);

                                Log.e("success?", "success?");

                                // Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            }
                            else
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            // progressBar.setVisibility(View.GONE);
                        } catch (JSONException je) {
                            je.printStackTrace();
                            // progressBar.setVisibility(View.GONE);

                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", sharedPreClass.getValue_string("token"));
                return headers;
            }
        };

        // set retry policy
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(1500);

                    if(SharedPreferenceClass.isAllowToken) {
//                        if(!Objects.equals(sharedPreClass.getValue_string("user_id"), "null")) {
//                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                        }
//                        else {
//                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                        }

                        VerifyToken();
                    }
                    else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();

                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }
        };

        thread.start();

    }

    @Override
    protected void onStop() {
        super.onStop();

        finish();
    }


}
