package vn.izisolution.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import vn.izisolution.R;
import vn.izisolution.views.ValidateEditText;

/**
 * Created by ToanNMDev on 3/13/2017.
 */

public class Utils {

    public static final int WORK_AGE = 20;
    public static final int PLAN_CUSOMER_NEW_SUBTRACT_MONTH = 6;


    public interface OnMainUIClick {
        public void onMainUIClick();
    }

    public static void setupUI(View view, final Activity activity, final OnMainUIClick onMainUIClick, final boolean hideKeyboard) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText) || !(view instanceof ValidateEditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (hideKeyboard)
                        Utils.hideKeyBoard(activity);
                    if (onMainUIClick != null)
                        onMainUIClick.onMainUIClick();
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, activity);
            }
        }
    }

    public static void setupUI(View view, final Activity activity, final OnMainUIClick onMainUIClick) {
        setupUI(view, activity, onMainUIClick, true);
    }

    public static void setupUI(View view, final Activity activity) {
        setupUI(view, activity, null);
    }


    public static void showKeyBoard(Activity activity, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    public static void showKeyBoard(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    public static void hideKeyBoard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ex) {

        }
    }

    public static String checkTextEqualFalse(String s) {
        return s.equals("false") ? "" : s;
    }

    public static int checkIntEqualFalse(String s) {
        if (s != null && !s.equals("") && !s.equals("null"))
            return s.equals("false") ? -1 : Integer.parseInt(s);
        else return -1;
    }

    public static long convertYearToLong(int year) {
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.YEAR, -year);
        return c.getTime().getTime();
    }

    public static String convertStringMoney(long money, int groupSize) {
        if (money != 0) {
            DecimalFormat fmt = new DecimalFormat();
            DecimalFormatSymbols fmts = new DecimalFormatSymbols();
            fmts.setGroupingSeparator('.');
            fmt.setGroupingSize(groupSize);
            fmt.setGroupingUsed(true);
            fmt.setDecimalFormatSymbols(fmts);

            return fmt.format(money);
        } else return "0";
    }

    @Deprecated
    public static String convertStringMoney(long money) {
        if (money != 0) {
            DecimalFormat fmt = new DecimalFormat();
            DecimalFormatSymbols fmts = new DecimalFormatSymbols();
            fmts.setGroupingSeparator('.');
            fmt.setGroupingSize(3);
            fmt.setGroupingUsed(true);
            fmt.setDecimalFormatSymbols(fmts);

            return fmt.format(money) + " đ";
        } else return "0 đ";
    }

    public static Typeface getTypefaceRoboto(Context context) {
        return Typeface.createFromAsset(
                context.getAssets(),
                "fonts/Roboto-Regular.ttf");
    }

    public static Typeface getTypefaceRobotoMedium(Context context) {
        return Typeface.createFromAsset(
                context.getAssets(),
                "fonts/Roboto-Black.ttf");
    }

    public static Typeface getTypefaceOpenSans(Context context) {
        try {
            return Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/OpenSans-Regular.ttf");
        } catch (Exception ex) {

        }
        return null;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void clearNotifications(Context context) {
        NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }

    public static long countDayBetween(String date1, String date2, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date Date1 = null, Date2 = null;
        try {
            Date1 = sdf.parse(date1);
            Date2 = sdf.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000);
    }

    public static String convertWorkTime(String time) {
        int hourIndex = time.lastIndexOf(".");
        Debug.IS_DEBUG = true;
        Debug.Log("convertWorkTime -> hourIndex -> " + hourIndex + ", " + time);
//        String hour = String.valueOf(time.charAt(0));
        if (hourIndex != -1) {
            String hour = time.substring(0, hourIndex);
            String min = time.substring(time.indexOf("."));
            float minute = Float.parseFloat(min);

            int h = Integer.parseInt(hour);
            if (h < 10)
                hour = "0" + h;
            int newMin = (int) minute * 60;
            if (newMin < 10)
                min = "0" + newMin;

            return hour + ":" + min;
        } else return "";
    }

    public static void openCHPlay(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://appvmt.izisolution.vn")));
    }

    public static void openCHPlay(Context context, String link) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
//        final String appPackageName = context.getPackageName();
//        try {
//            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//        } catch (android.content.ActivityNotFoundException anfe) {
//            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//        }
    }

    public static void killApp(boolean killSafely) {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }

    public static String checkDomain(Context context, String domain,String protocol) {
//        String prefixDomain = context.getResources().getString(R.string.prefix_domain);
        String firstDomain = protocol;
        if (!domain.startsWith(firstDomain)) {
            domain = firstDomain + domain;
        }
//        if (!domain.endsWith(prefixDomain)) {
//            domain += prefixDomain;
//        }
        return domain.replace(" ", "");
    }
    public static String checkDomain1( String domain,String protocol) {
        String firstDomain = protocol;
        if (!domain.startsWith(firstDomain)) {
            domain = firstDomain + domain;
        }
        return domain.replace(" ", "");
    }

    public static boolean subtractDate(Date dateToCompare, Date referenceDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(referenceDate);
        int subtract_month = PLAN_CUSOMER_NEW_SUBTRACT_MONTH * -1;
        c.add(Calendar.MONTH, subtract_month);

        return dateToCompare.compareTo(c.getTime()) >= 0 ? true : false;
    }

    public static String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        String datetime = dateformat.format(c.getTime());
        return datetime;
    }

    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("kk:mm:ss");
        String datetime = dateformat.format(c.getTime());
        return datetime;
    }

}
