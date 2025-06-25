package com.example.astrolingo.activity;

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
import com.example.astrolingo.Service.SharedPreferenceClass;
import com.example.astrolingo.function.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.astrolingo.R;
import com.example.astrolingo.Service.UtilService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    SharedPreferenceClass sharedPreClass;

    EditText input_mail, input_username, input_telephone, input_password, input_passwordAu;
    TextView error_mail, error_username, error_telephone, error_password, error_passwordAu, error_checkbox;
    Button btn_register;
    TextView login_field;
    ImageView backIcon;
    ProgressBar progressBar;
    UtilService utilService;
    CheckBox checkBox;
    String mail, username, telephone, password, passwordAu;
    boolean isCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.loginregister_register);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        login_field = findViewById(R.id.login_field);
        input_mail = findViewById(R.id.input_mail);
        input_username = findViewById(R.id.input_username);
        input_telephone = findViewById(R.id.input_telephone);
        input_password = findViewById(R.id.input_password);
        input_passwordAu = findViewById(R.id.input_passwordAu);

        error_mail = findViewById(R.id.error_mail);
        error_username = findViewById(R.id.error_username);
        error_telephone = findViewById(R.id.error_telephone);
        error_password = findViewById(R.id.error_password);
        error_passwordAu = findViewById(R.id.error_passwordAu);
        error_checkbox = findViewById(R.id.error_checkbox);

        btn_register = findViewById(R.id.button_register);
        checkBox = findViewById(R.id.checkBox_rule);

        backIcon = findViewById(R.id.backIcon);

        utilService = new UtilService();
        sharedPreClass = new SharedPreferenceClass(this);

        inVisibiltyError();

        login_field.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btn_register.setOnClickListener(v -> {
            // tắt keyboard
            utilService.hideKeyboard(v, RegisterActivity.this);

            mail = input_mail.getText().toString();
            username = input_username.getText().toString();
            telephone = input_telephone.getText().toString();
            password = input_password.getText().toString();
            passwordAu = input_passwordAu.getText().toString();
            isCheck = checkBox.isChecked();

            if(validate(v)) {
                registerUser();
            }

        });

        // back icon
        backIcon.setOnClickListener(v -> {
            finish();
        });
    }

    private void registerUser() {
        progressBar.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("user_name", username);
        params.put("email", mail);
        params.put("phone_number", telephone);
        params.put("password", password);

        String apiRegister = getString(R.string.api_key) + "api/auth/register";

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

                                Toast.makeText(RegisterActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();

                                Intent resultIntent = new Intent(RegisterActivity.this, Register_VerifyEmailActivity.class);
                                resultIntent.putExtra("email", mail);
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

                            // Log.e("err", "err");

                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                                JSONObject obj = new JSONObject(res);

                                // thông báo
                                Toast.makeText(RegisterActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();

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
        int socketTime = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    // request add
    public boolean validate(View view) {
        boolean isValid = true;

        if(TextUtils.isEmpty(mail)) {
            isValid = false;
            error_mail.setText(getString(R.string.error_email_empty));
            error_mail.setVisibility(View.VISIBLE);
        }
        else {
            if(!StringManager.isValidEmail(mail)) {
                isValid = false;
                error_mail.setText(getString(R.string.error_email_invalid));
                error_mail.setVisibility(View.VISIBLE);
            }
        }

        if(TextUtils.isEmpty(username))
        {
            isValid = false;
            error_username.setText(getString(R.string.error_username_empty));
            error_username.setVisibility(View.VISIBLE);
        }
        else {
            if(StringManager.containsSpecialCharacter(username) || StringManager.containsSpace(username)) {
                isValid = false;
                error_username.setText(getString(R.string.error_username_specialsympol));
                error_username.setVisibility(View.VISIBLE);
            }
        }

        if(!TextUtils.isEmpty(telephone))
        {
            if(!StringManager.isPhoneNumber(telephone)) {
                isValid = false;
                error_telephone.setText(getString(R.string.error_telephone_invalid));
                error_telephone.setVisibility(View.VISIBLE);
            }
        }
//        else {
//            isValid = false;
//            error_telephone.setText(getString(R.string.error_telephone_empty));
//            error_telephone.setVisibility(View.VISIBLE);
//        }

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

        if(!isCheck)
        {
            isValid = false;
            error_checkbox.setText(getString(R.string.error_checkboxes));
            error_checkbox.setVisibility(View.VISIBLE);
//            utilService.showSnackBar(view, getString(R.string.tickRule));
        }

        return isValid;
    }

    public void inVisibiltyError() {
        error_mail.setVisibility(View.INVISIBLE);
        error_username.setVisibility(View.INVISIBLE);
        error_telephone.setVisibility(View.INVISIBLE);
        error_password.setVisibility(View.INVISIBLE);
        error_passwordAu.setVisibility(View.INVISIBLE);
        error_checkbox.setVisibility(View.INVISIBLE);

        input_mail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    error_mail.setVisibility(View.INVISIBLE);
            }
        });
        input_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    error_username.setVisibility(View.INVISIBLE);
            }
        });
        input_telephone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    error_telephone.setVisibility(View.INVISIBLE);
            }
        });
        input_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    error_password.setVisibility(View.INVISIBLE);
            }
        });
        input_passwordAu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    error_passwordAu.setVisibility(View.INVISIBLE);
            }
        });
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            error_checkbox.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);

        if(SharedPreferenceClass.isAllowToken) {

            if(!Objects.equals(sharedPreClass.getValue_string("user_id"), "null")) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if(SharedPreferenceClass.isAllowToken) {
//            SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceClass.USER_PREP, MODE_PRIVATE);
//
//            if(sharedPreferences.contains("token")) {
//                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
//                finish();
//            }
//        }
//
//    }
}
