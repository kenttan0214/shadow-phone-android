package pomelo.kenttan.shadowphone.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fetch {
    public static void POST(Context context, String url,
                            Map<String, String> params,
                            Response.Listener<JSONObject> responseListener,
                            Response.ErrorListener errorListener) {
        Fetch.addToQueue(context, url, params, responseListener, errorListener, Request.Method.POST);
    }

    public static void GET(Context context, String url,
                           Map<String, String> params,
                           Response.Listener<JSONObject> responseListener,
                           Response.ErrorListener errorListener) {
        Fetch.addToQueue(context, url, params, responseListener, errorListener, Request.Method.GET);
    }

    private static void addToQueue(
            Context context, String url, Map<String, String> params,
            Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener,
            int RequestMethod) {
        final String accessToken = Auth.getAuthToken(context);

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject parameters = new JSONObject();

        if (params != null) {
            parameters = new JSONObject(params);
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                RequestMethod,
                url,
                parameters,
                responseListener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-access-token", accessToken);
                return params;
            }
        };
        queue.add(jsObjRequest);
    }
}
