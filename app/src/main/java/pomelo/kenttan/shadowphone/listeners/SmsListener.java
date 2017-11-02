package pomelo.kenttan.shadowphone.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import pomelo.kenttan.shadowphone.util.Config;

public class SmsListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Config.SMS_RECEIVED)) {
            readSms(intent);
        }
    }

    private void readSms(Intent intent) {
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            try {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msgs = new SmsMessage[pdus.length];

                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String msg_from = msgs[i].getOriginatingAddress();
                    String msgBody = msgs[i].getMessageBody();
                    Log.e("msgBody", msgBody);
                }
            } catch (Exception e) {
                Log.e("Failed to read sms ", e.getMessage());
            }
        }
    }
}
