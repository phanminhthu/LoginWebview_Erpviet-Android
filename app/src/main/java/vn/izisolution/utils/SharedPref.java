package vn.izisolution.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ToanNMDev on 3/13/2017.
 */

public class SharedPref {

    public static final String KEY_AUTO_LOGIN = "key_auto_login";
    public static final String KEY_LOGIN_BACKGROUND = "key_login_background";

    private static final String MY_SHARED_PREFERENCE = "MySharedPreference";
    public static final String DOMAIN = "domain";
    public static final String LOGIN = "login";
    public static final String USER_PASSWORD = "user_password";
    public static final String NAME_DATABASE = "name_database";
    public static final String PROTOCOL = "protocol";
    public static final String NOTIFICATION_ID = "notification_id";
    public static final String FIREBASE_TOKEN = "firebase_token";

    public static final String HAS_PERMISSION_CRM = "has_permission_crm";
    public static final String HAS_PERMISSION_ACCOUNT = "has_permission_account";
    public static final String HAS_PERMISSION_STOCK = "has_permission_stock";
    public static final String HAS_PERMISSION_HR = "has_permission_hr";

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(
                MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, 0);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        int ret = 0;
        try {
            SharedPreferences pref = context.getSharedPreferences(
                    MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);

            ret = pref.getInt(key, defaultValue);
        } catch (Exception e) {

        }
        return ret;
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(
                MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        String ret = defaultValue;
        try {
            SharedPreferences pref = context.getSharedPreferences(
                    MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);

            ret = pref.getString(key, defaultValue);
            if (ret.equals(""))
                ret = defaultValue;
        } catch (Exception e) {
        }
        return ret;
    }

}
