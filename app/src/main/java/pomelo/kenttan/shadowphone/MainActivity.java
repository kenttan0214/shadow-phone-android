package pomelo.kenttan.shadowphone;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pomelo.kenttan.shadowphone.util.Auth;
import pomelo.kenttan.shadowphone.util.Config;
import pomelo.kenttan.shadowphone.util.Fetch;
import pomelo.kenttan.shadowphone.util.NotificationUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        if (Auth.haveToken(this)) {
            goToDeviceListActivity();
        } else {
            showProgressDialog(true);
            validateUser();
        }
    }

    private void validateUser() {
        if (getCurrentUser() != null) {
            registerBoardcastReceiver();
            registerDevice();
        } else {
            goToLoginActivity();
        }
    }

    private void goToLoginActivity () {
        Intent loginAcgivity = new Intent(getApplicationContext(), LoginActivity.class);
        loginAcgivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginAcgivity);
    }

    private void goToDeviceListActivity() {
        Intent deviceListActivity = new Intent(getApplicationContext(), DevicesListActivity.class);
        deviceListActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(deviceListActivity);
    }

    private FirebaseUser getCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = null;
        return currentUser;
    }

    private void registerBoardcastReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    registerDevice();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                }
            }
        };
    }

    private String getDeviceRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString(Config.TOKEN, null);
        return regId;
    }

    private void registerDevice() {
        FirebaseUser currentUser = getCurrentUser();
        String url = "https://shadow-phone-api.herokuapp.com/signon";

        Map<String, String> params = new HashMap();
        params.put("token", getDeviceRegId());
        params.put("userId", currentUser.getUid());
        params.put("userName", currentUser.getDisplayName());

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    String accessToken = data.getString("accessToken");

                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.USER_SHARED_PREF, 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Config.ACCESS_TOKEN, accessToken);
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    showProgressDialog(false);
                    goToDeviceListActivity();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showProgressDialog(false);
                Toast.makeText(getApplicationContext(), "Failed to register device", Toast.LENGTH_LONG).show();
            }
        };

        Fetch.POST(this, url, params, responseListener, errorListener);
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

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
