package pomelo.kenttan.shadowphone;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pomelo.kenttan.shadowphone.util.Config;
import pomelo.kenttan.shadowphone.util.Fetch;

public class QRCodeActivity extends AppCompatActivity {
    private static final String TAG = QRCodeActivity.class.getSimpleName();
    private ImageView qrCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        qrCodeView = (ImageView) findViewById(R.id.qrCodeImgView);
        getQRCode();
    }

    private void getQRCode() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://shadow-phone-api.herokuapp.com/qr";

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    String qr = data.getString("qr");
                    displayBase64Image(qr);
                    Log.d(TAG, "received QR code"+ qr);
                } catch (JSONException e) {
                    Log.e(TAG, "failed to get QR code");
                }
            }
        };

        Response.ErrorListener errorListener =  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Failed to generate QR code", Toast.LENGTH_LONG).show();
            }
        };

        Fetch.GET(this, url, null, responseListener, errorListener);
    }

    private void displayBase64Image(String qrBase64) {
        qrBase64 = qrBase64.substring(qrBase64.indexOf(",") + 1);
        byte[] decodedString = Base64.decode(qrBase64.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        qrCodeView.setImageBitmap(decodedByte);
    }
}
