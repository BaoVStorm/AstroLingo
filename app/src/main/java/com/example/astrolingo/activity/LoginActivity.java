package com.example.astrolingo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import java.util.Objects;

// // google login
//import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
//import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
//import androidx.core.os.CancellationSignal;
//import androidx.credentials.Credential;
//import androidx.credentials.CredentialManager;
//import androidx.credentials.CredentialManagerCallback;
//import androidx.credentials.GetCredentialRequest;
//import androidx.credentials.GetCredentialResponse;
//import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    SharedPreferenceClass sharedPreClass;
    UtilService utilService;
    EditText input_username, input_password;
    CheckBox checkBox;
    TextView error_username, error_password, error_checkbox, error_login;
    ConstraintLayout button_login, button_login_google;
    TextView forgot_password, register_field;
    ProgressBar progressBar;

    String username, password;
    boolean isCheck;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    private ActivityResultLauncher<Intent> signIn_GoogleLauncher;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.loginregister_login);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        utilService = new UtilService();
        sharedPreClass = new SharedPreferenceClass(this);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        input_username= findViewById(R.id.input_username);
        input_password = findViewById(R.id.input_password);
        checkBox = findViewById(R.id.checkBox_rule);

        register_field = findViewById(R.id.register_field);
        forgot_password = findViewById(R.id.forgot_password_field);

        error_username = findViewById(R.id.error_username);
        error_password = findViewById(R.id.error_password);
        error_checkbox = findViewById(R.id.error_checkbox);
        error_login = findViewById(R.id.error_login);

        button_login = findViewById(R.id.button_login);
        button_login_google = findViewById(R.id.button_login_google);

        inVisibiltyError();

        register_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.e("error login google", "error login google");
                progressBar.setVisibility(View.VISIBLE);

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        button_login.setOnClickListener(v -> {
            error_login.setVisibility(View.INVISIBLE);
            // tắt keyboard
            utilService.hideKeyboard(v, LoginActivity.this);

            username = input_username.getText().toString();
            password = input_password.getText().toString();
            isCheck = checkBox.isChecked();

            if(validate(v)) {
                loginUser();
            }
        });

        // google login
        signIn_GoogleLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        handleSignInGoogleResult(data);
                    }
                    else {
                        // Log.e("error login google", "error login google");
                        error_login.setText("Error Login Google!!");
                        error_login.setVisibility(View.VISIBLE);
                    }
                }
        );

        button_login_google.setOnClickListener(v -> {
            error_login.setVisibility(View.INVISIBLE);

            if(checkBox.isChecked()) {
                loginUser_google();
            }
            else {
                error_checkbox.setText(getString(R.string.error_checkboxes));
                error_checkbox.setVisibility(View.VISIBLE);
            }
        });

    }

    private void loginUser() {
        progressBar.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("user_name", username);
        params.put("password", password);

        String apiLogin = getString(R.string.api_key) + "api/auth/login";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiLogin,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Log.e("success", "success");

                        try {
                            if(response.getBoolean("success")) {
                                String token = response.getString("token");
                                String user_id = response.getString("user_id");

                                sharedPreClass.setValue_string("token", token);
                                sharedPreClass.setValue_string("user_id", user_id);

                                Toast.makeText(LoginActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                                // Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                        // Log.e("VolleyError", "Error: " + error.toString());

                        NetworkResponse response = error.networkResponse;
                        if(error instanceof ServerError && response != null) {

                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject obj = new JSONObject(res);
                                // thông báo
                                Toast.makeText(LoginActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();

                                if(!obj.getBoolean("verified")) {
                                    String mail = obj.getString("email");

                                    Intent resultIntent = new Intent(LoginActivity.this, Register_VerifyEmailActivity.class);
                                    resultIntent.putExtra("email", mail);
                                    startActivity(resultIntent);
                                }
                                else {
                                    boolean error_server = obj.getBoolean("error_server");
                                    if(!error_server) {
                                        error_login.setText(getString(R.string.error_login_invalid));
                                        error_login.setVisibility(View.VISIBLE);
                                    }
                                }

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

        // Log.e("passs", "passs");

        // set retry policy
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // request add
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private boolean validate(View v) {
        boolean isValid = true;

        if(TextUtils.isEmpty(username))
        {
            isValid = false;
            error_username.setText(getString(R.string.error_username_empty));
            error_username.setVisibility(View.VISIBLE);
        }
//        else {
//            if(StringManager.containsSpecialCharacter(username) || StringManager.containsSpace(username)) {
//                isValid = false;
//                error_username.setText(getString(R.string.error_username_specialsympol));
//                error_username.setVisibility(View.VISIBLE);
//            }
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

        if(!isCheck)
        {
            isValid = false;
            error_checkbox.setText(getString(R.string.error_checkboxes));
            error_checkbox.setVisibility(View.VISIBLE);
        }

        return isValid;
    }

    public void inVisibiltyError() {
        error_username.setVisibility(View.INVISIBLE);
        error_password.setVisibility(View.INVISIBLE);
        error_checkbox.setVisibility(View.INVISIBLE);
        error_login.setVisibility(View.INVISIBLE);

        input_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    error_login.setVisibility(View.INVISIBLE);
                    error_username.setVisibility(View.INVISIBLE);
                }

            }
        });
        input_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    error_login.setVisibility(View.INVISIBLE);
                    error_password.setVisibility(View.INVISIBLE);
                }

            }
        });
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            error_login.setVisibility(View.INVISIBLE);
            error_checkbox.setVisibility(View.INVISIBLE);
        });
    }

    // -------------------------------------- Login Google Api --------------------------------------
    private void loginUser_google() {
        progressBar.setVisibility(View.VISIBLE);

        Intent signInIntent = gsc.getSignInIntent();
        signIn_GoogleLauncher.launch(signInIntent);
    }

    private void handleSignInGoogleResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            // Signed in successfully
            String google_id = account.getId();
            String email = account.getEmail();
            String full_name = account.getDisplayName();
            String photoUrl = String.valueOf(account.getPhotoUrl());

            String save  = "googleid :" + google_id + "\n" + "email :" + email + "\n" + "fullname :" + full_name + "\n" +"photoUrl: " + photoUrl;

            callAPILoginWithGoogle(google_id, email, full_name, photoUrl);

//            Toast.makeText(LoginActivity.this, email + "_" + full_name, Toast.LENGTH_SHORT).show();

//            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        } catch (ApiException e) {

            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            // Sign in failed
        }
    }

    private void callAPILoginWithGoogle(String google_id, String email, String full_name, String photoUrl) {
        HashMap<String, String> params = new HashMap<>();
        params.put("google_id", google_id);
        params.put("email", email);
        params.put("full_name", full_name);
        params.put("photo_url", photoUrl);

        String apiLoginGoogle = getString(R.string.api_key) + "api/auth/googleauth";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiLoginGoogle,
            new JSONObject(params),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        // Log.e("success google auth", "success google auth");

                        if(response.getBoolean("success")) {
                            String token = response.getString("token");
                            String user_id = response.getString("user_id");

                            sharedPreClass.setValue_string("token", token);
                            sharedPreClass.setValue_string("user_id", user_id);

                            Toast.makeText(LoginActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                            // Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                    // Log.e("error google auth", "error google auth");

                    NetworkResponse response = error.networkResponse;
                    if(error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                            JSONObject obj = new JSONObject(res);

                            // thông báo
                            Toast.makeText(LoginActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();

                            error_login.setText(obj.getString("msg"));
                            error_login.setVisibility(View.VISIBLE);

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
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // request add
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);

        if(SharedPreferenceClass.isAllowToken) {
            SharedPreferenceClass tempsharedPreClass = new SharedPreferenceClass(this);

            if(!Objects.equals(tempsharedPreClass.getValue_string("user_id"), "null")) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    }
}
