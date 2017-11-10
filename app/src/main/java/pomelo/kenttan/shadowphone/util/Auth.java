package pomelo.kenttan.shadowphone.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Auth {
    public static String getAuthToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Config.USER_SHARED_PREF, 0);
        String accessToken = pref.getString(Config.ACCESS_TOKEN, "");
        return accessToken;
    }

    public static boolean haveToken(Context context) {
        String accessToken = Auth.getAuthToken(context);
        if (accessToken.length() > 0) {
            return true;
        }
        return false;
    }
}
