package com.example.astrolingo.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.astrolingo.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScoreAPI {

    public static void updateScore(HashMap<String, String> params, Context context, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        String apiurl = context.getString(R.string.api_key) + "api/score/updateScore";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            apiurl,
            new JSONObject(params),
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
        int socketTime = 60000;
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
}
