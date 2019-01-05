package vn.izisolution.utils;

import android.util.Log;

/**
 * Created by ToanNMDev on 4/22/2017.
 */

public class Debug {

    private static boolean canDebug = false;
    public static boolean IS_DEBUG = false;
    private static final int maxLength = 1000;

    public static void Log(String message) {
        if (canDebug)
            if (IS_DEBUG) {
                Log.e("tusinh", message);
            }
    }

    public static void FullLog(String message) {
        for(int i = 0; i <= message.length() / maxLength; i++) {
            int start = i * maxLength;
            int end = (i+1) * maxLength;
            end = end > message.length() ? message.length() : end;
            Log(message.substring(start, end));
        }
    }

}