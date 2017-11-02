package pomelo.kenttan.shadowphone.util;

public class Config {
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "shadowphone";
    public static final String TOKEN = "token";
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    public static final String GOOGLE_REQUESTID_TOKEN = "721489690199-a289vf2smnnkvji6ikhvg1mtsqrr27hi.apps.googleusercontent.com";
}
