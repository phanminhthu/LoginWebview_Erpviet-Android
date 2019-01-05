package vn.izisolution.constant;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import vn.izisolution.BuildConfig;
import vn.izisolution.LoginActivity;
import vn.izisolution.utils.SharedPref;
import vn.izisolution.utils.ShowPopup;
import vn.izisolution.utils.Utils;

/**
 * Created by ToanNMDev on 3/13/2017.
 */

public class Constants {

    public static Dialog noInternetDialog;
    public static final int RESPONSE_CODE_DUPLICATE_ACCESS_TOKEN = 1004;
    public static final int TIME_OUT = 300 * 1000;
    public static final int ITEM_PER_PAGE = 15;
    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
//    private static final String URL_LOGIN = "/web/login";
    //    public static final String URL_GET_DB_ODOO8 = "/web/database/get_list";
    public static final String URL_GET_DB_ODOO11 = "/web/database/list";
    public static final String URL_LOGIN = "/web/login?db=";

    public static final String TAG_SUCCESS = "+location.hash;";
    public static final String TAG_SUCCESS_1 = "Redirecting";
    //<html><head><script>window.location='/web?'+location.hash;</script></head></html>
    public static final String TAG_SCRIPT = "<script";
    public static final String TAG_SCRIPT_OPEN = "<input type=\"hidden\" name=\"csrf_token\" value=";
    public static final String TAG_SCRIPT_CLOSE = "/>";

    public static final String TAG_ODOO_OPEN = "odoo.define('web.csrf',function(require){";
    public static final String TAG_ODOO_CLOSE = "});";

    public static final String TAG_VAR_TOKEN = "vartoken=";
    public static final Pattern ptrnToken = Pattern.compile(TAG_SCRIPT_OPEN + ".*?" + TAG_SCRIPT_CLOSE);

    public static final String TYPE_JSON = "application/json";
//    public static final String DOMAIN = "http://erp.vmt.vn/ev_app_cs_webservice_v2";
//    public static final String DOMAIN = "http://demo68.erpviet.vn";// test Server
//    public static final String DOMAIN = "http://10.10.10.109:1099/ev_webservice";// test Server
//    public static final String DOMAIN = "http://ad5d9188-54cf-11e7-main.erpviet.vn/saas_portal/izi_create_new_database";// test Server
//    http://client.odooviet.vn/ev_webservice

//    $sURL = "http://ad5d9188-54cf-11e7-main.erpviet.vn/saas_portal/izi_create_new_database";

    public static final String API_GET_DEFAULT_DATA = "http://main.erpviet.vn/izi_saas/get_default_data";

//    public static final String API_UPDATE_FIREBASE_TOKEN = DOMAIN + "/update_firebase_token";
//    public static final String API_LOGIN = DOMAIN + "/login";

    /*
     * Methods
     * */
    public static AsyncHttpClient getAsyncHttpClient() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(0, Constants.TIME_OUT);
        client.setConnectTimeout(Constants.TIME_OUT);
        client.setTimeout(Constants.TIME_OUT);
        client.setResponseTimeout(Constants.TIME_OUT);
        return client;
    }

    public static JSONObject getJSONParams(Context context) {
        JSONObject mainJsonObject = new JSONObject();
        try {
            mainJsonObject.put("login", SharedPref.getString(context, "LOGIN"));
            mainJsonObject.put("access_token", SharedPref.getString(context, "ACCESS_TOKEN"));
            mainJsonObject.put("version", BuildConfig.VERSION_NAME);
            mainJsonObject.put("version_app", BuildConfig.VERSION_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mainJsonObject;
    }

    public static boolean checkDuplicateAccessToken(JSONObject response, final Context context) {
//        try {
//            if (response.getJSONArray("result").getJSONObject(0)
//                    .getInt("code") == RESPONSE_CODE_DUPLICATE_ACCESS_TOKEN) {
//                SharedPref.saveInt(context, SharedPref.KEY_AUTO_LOGIN, 1);
//                new ShowPopup(context).info("Tài khoản của bạn đã bị đăng nhập ở 1 nơi khác"
//                        , new ShowPopup.OnPopupActionListener() {
//                            @Override
//                            public void onCancel() {
//
//                            }
//
//                            @Override
//                            public void onAccept() {
//                                Utils.clearNotifications(context);
//                                Intent intent = new Intent(context, LoginActivity.class);
//                                SharedPref.saveString(context, SharedPref.LOGIN, "");
//                                SharedPref.saveString(context, SharedPref.USER_PASSWORD, "");
//                                context.startActivity(intent);
//                            }
//                        }).show();
//                return true;
//            } else
//                return false;
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return false;
//        }
        return false;
    }

    public interface OnLoadData {
        void onLoading();

        void onError(String error);

        void onSuccess(JSONObject response);
    }

}
