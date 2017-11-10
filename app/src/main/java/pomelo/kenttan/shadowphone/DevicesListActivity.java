package pomelo.kenttan.shadowphone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pomelo.kenttan.shadowphone.util.Config;
import pomelo.kenttan.shadowphone.util.Fetch;

public class DevicesListActivity extends AppCompatActivity {
    private static final String TAG = QRScannerActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    TextView qrCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);

        progressDialog = new ProgressDialog(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.deviceListToolbar);
        qrCodeView = (TextView) findViewById(R.id.qrCodeTextView);

        setSupportActionBar(myToolbar);
        pairDevice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.devicelistmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_device:
                gotoAddDevice();
                return true;
            case R.id.my_qr_code:
                gotoMyQrCode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void gotoAddDevice() {
        Intent qrCodeScannerActivity = new Intent(getApplicationContext(), QRScannerActivity.class);
        startActivity(qrCodeScannerActivity);
    }

    private void gotoMyQrCode() {
        Intent qrCodeActivity = new Intent(getApplicationContext(), QRCodeActivity.class);
        startActivity(qrCodeActivity);
    }

    private void showProgressDialog(boolean show) {
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);

        if (show) {
            progressDialog.show();
        } else {
            progressDialog.hide();
        }
    }

    private void pairDevice() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String qrCodeJson = bundle.getString(Config.QR_TAG);

            try {
                JSONObject qrObject = new JSONObject(qrCodeJson);
                String token = qrObject.getString("token");

                postPairDevice(token);
            } catch (JSONException e) {
                Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void postPairDevice(String token) {
        showProgressDialog(true);

        String url = "https://shadow-phone-api.herokuapp.com/pair";

        Map<String, String> params = new HashMap();
        params.put("token", token);

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    String parentId = data.getString("id");
                    String displayName = data.getString("displayName");
                    qrCodeView.setText(displayName);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    showProgressDialog(false);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Failed to pair device", Toast.LENGTH_LONG).show();
                showProgressDialog(false);
            }
        };
        Fetch.POST(this, url, params, responseListener, errorListener);
    }
}
