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

public class CertificateApi {

    // Hàm gọi API, dùng context để gọi được các phương thức Android
    public static void addCertificate(HashMap<String, String> params, Context context, String token, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        String apiurl = context.getString(R.string.api_key) + "api/certificate/addCertificate";

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
                headers.put("Authorization", "Bearer " + token); // nếu cần token
                return headers;
            }
        };

        // Cấu hình retry
        int socketTime = 7000;
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

    public static void getCertificate(String user_id, Context context, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        String apiurl = context.getString(R.string.api_key) + "api/certificate/getCertificate";

        apiurl = apiurl + "?user_id=" + user_id;

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
        int socketTime = 7000;
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
        TestApi.getPart( <part_id>, this, token,
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

    public static void addUserAnswers(HashMap<String, Object> params, Context context, String token, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        String apiurl = context.getString(R.string.api_key) + "api/certificate/addUserAnswers";

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
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Cấu hình retry
        int socketTime = 7000;
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
