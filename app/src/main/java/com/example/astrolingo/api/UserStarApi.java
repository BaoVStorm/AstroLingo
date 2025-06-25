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

public class UserStarApi {


    // Hàm gọi API, dùng context để gọi được các phương thức Android
    public static void addWordUserStars(HashMap<String, String> params, Context context, String token, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        String apiurl = context.getString(R.string.api_key) + "api/user_star/addWordUserStars";

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

    /*
        ------ gọi hàm getUserSchema tại 1 activity nào đó (để lấy dữ liệu của người dùng từ token đã có) -----
        UserLookupHistoryApi.addLookUpHistory( params, this, token,
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

    public static void removeWordUserStars(HashMap<String, String> params, Context context, String token, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        String apiurl = context.getString(R.string.api_key) + "api/user_star/removeWordUserStars";

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

    public static void removeWordUserStarsById(HashMap<String, String> params, Context context, String token, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        String apiurl = context.getString(R.string.api_key) + "api/user_star/removeWordUserStarsById";

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

    //    [
//        {
//            "_id": "6831edd8e4dfc22d7b190491",
//                "user_id": "6826953fdd0f324c408858d9",
//                "word": "con bò nhảy lò cò 🐄🐄",
//                "meaning": "The cow jumped the trigger 🐄🐄",
//                "isTranslateEnglish": true,
//                "lookup_at": "2025-05-24T16:03:36.450Z",
//                "__v": 0
//        },
//    ]
    public static void getWordUserStars(HashMap<String, String> params, Context context, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        String apiurl = context.getString(R.string.api_key) + "api/user_star/getWordUserStars";

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
