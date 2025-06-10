package com.example.astrolingo.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.astrolingo.R;
import com.example.astrolingo.function.StringManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class translateApi {

    // Hàm gọi API, dùng context để gọi được các phương thức Android
    public static void translateLanguage(String textTranslate, boolean isTranslateEnglish, Context context, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        String apiurl = "";

        if(isTranslateEnglish)
            apiurl = context.getString(R.string.api_translateVTE_key);
        else
            apiurl = context.getString(R.string.api_translateETV_key);

        String encoderString = StringManager.changeStringtoURLEncoder(textTranslate);

        apiurl = apiurl + encoderString;

        Log.d("TRANSLATE_URL", "Requesting URL: " + apiurl);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET,
            apiurl,
            null,
            onSuccess, // listener thành công truyền từ Activity
            onError    // listener lỗi truyền từ Activity
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Cấu hình retry
        int socketTime = 3500;
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        jsonObjectRequest.setRetryPolicy(policy);

        // Thêm request vào hàng đợi
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    public static void translateLanguage2(String textTranslate, boolean isTranslateEnglish, Context context, Response.Listener<JSONArray> onSuccess, Response.ErrorListener onError) {


        String apiurl = "";

        if(isTranslateEnglish)
            apiurl = context.getString(R.string.api_translateVTE_key2);
        else
            apiurl = context.getString(R.string.api_translateETV_key2);

        String encoderString = StringManager.changeStringtoURLEncoder(textTranslate);

        apiurl = apiurl + encoderString;


        Log.d("TRANSLATE_URL", "Requesting URL: " + apiurl);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiurl,
                null,
                onSuccess, // listener thành công truyền từ Activity
                onError    // listener lỗi truyền từ Activity
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Cấu hình retry
        int socketTime = 3500;
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        jsonObjectRequest.setRetryPolicy(policy);

        // Thêm request vào hàng đợi
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    /*
        ------ gọi hàm getUserSchema tại 1 activity nào đó (để lấy dữ liệu của người dùng từ token đã có) -----
        translateApi.translateLanguage( textTranslate, isTranslateEnglish, this,
        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Xử lý khi thành công
                Log.d("API_SUCCESS", response.toString());
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Xử lý khi có lỗi
                Log.e("API_ERROR", error.toString());
            }
        });
     */

}
