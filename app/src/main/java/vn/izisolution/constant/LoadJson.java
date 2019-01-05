package vn.izisolution.constant;

import android.content.Context;

import cz.msebera.android.httpclient.entity.ContentType;
import vn.izisolution.utils.Debug;
import vn.izisolution.utils.ShowPopup;
import vn.izisolution.utils.Utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by ToanNMDev on 7/10/2017.
 */

public class LoadJson {

    private static final String EXCEPTION_TIME_OUT = "java.net.SocketTimeoutException";
    private static final String EXCEPTION_HTTP_CLIENT_TIME_OUT = "cz.msebera.android.httpclient.conn.ConnectTimeoutException";

    private static final String MESSAGE_TIME_OUT = "Kết nối quá thời hạn, mất quá nhiều thời gian để nhận phản hồi\n Xin vui lòng thử lại sau";

    private AsyncHttpClient client;
    private Context context;
    private String url;
    private StringEntity entity;
    private RequestParams requestParams;
    private OnLoadJson listener;
    private ShowPopup.OnPopupActionListener noInternerListener;
    private JsonHttpResponseHandler jsonHttpResponseHandler;

    private boolean needToShowErrorDialog = true;

    public boolean showTimeOutDialog = true;
    long loadingTime = 0;

    public interface OnLoadJson {
        void onLoadJsonLoading();

        void onLoadJsonSuccess(JSONObject response);

        void onLoadJsonError(String error, boolean needToShowErrorDialog);
    }

    public LoadJson(Context context, String url, RequestParams requestParams, OnLoadJson listener) {
        this.context = context;
        this.url = url;
        this.requestParams = requestParams;
        this.listener = listener;
        init();
    }

    public LoadJson(Context context, String url, StringEntity entity, OnLoadJson listener) {
        this.context = context;
        this.url = url;
        this.entity = entity;
        this.listener = listener;

        init();
    }

    public void cancelAllRequests(boolean mayInterruptflRunning) {
        if (client != null)
            client.cancelAllRequests(mayInterruptflRunning);
        init();
    }

    public AsyncHttpClient getAsyncHttpClient() {
        return client;
    }

    public void init() {
        client = Constants.getAsyncHttpClient();

        jsonHttpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    if (!Constants.checkDuplicateAccessToken(response, context))
                if (listener != null)
                    listener.onLoadJsonSuccess(response);
                loadingTime = System.currentTimeMillis() - loadingTime;
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Debug.Log("LoadJson statusCode -> " + statusCode + "\n " + responseString + "\n" + throwable);
//                Debug.Log("LoadJson responseString -> " + responseString + ", " + throwable);
                if (showTimeOutDialog)
                    if (throwable.equals(EXCEPTION_TIME_OUT)
                            || throwable.toString().startsWith(EXCEPTION_HTTP_CLIENT_TIME_OUT)
                            || throwable.toString().toLowerCase().endsWith("timed out")) {
                        if (context != null) {
                            new ShowPopup(context).info(MESSAGE_TIME_OUT).show();
                            needToShowErrorDialog = false;
                        }
                    }
                if (listener != null)
                    listener.onLoadJsonError(responseString, needToShowErrorDialog);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Debug.Log("LoadJson statusCode 1 -> " + statusCode + "\n " + errorResponse + "\n" + throwable);
//                Debug.Log("LoadJson responseString 1-> " + errorResponse + ", " + throwable);
                if (showTimeOutDialog)
                    if (throwable.toString().equals(EXCEPTION_TIME_OUT)
                            || throwable.toString().startsWith(EXCEPTION_HTTP_CLIENT_TIME_OUT)
                            || throwable.toString().toLowerCase().endsWith("timed out")) {
                        if (context != null) {
                            try {
                                new ShowPopup(context).info(MESSAGE_TIME_OUT).show();
                                needToShowErrorDialog = false;
                            } catch (Exception ex) {
                            }
                        }
                    }
                if (listener != null)
                    listener.onLoadJsonError(throwable.toString(), needToShowErrorDialog);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Debug.Log("LoadJson statusCode 2 -> " + statusCode);
//                Debug.Log("LoadJson responseString 2-> " + errorResponse + ", " + throwable);
                if (showTimeOutDialog)
                    if (throwable.toString().equals(EXCEPTION_TIME_OUT)
                            || throwable.toString().startsWith(EXCEPTION_HTTP_CLIENT_TIME_OUT)
                            || throwable.toString().toLowerCase().endsWith("timed out")) {
                        if (context != null) {
                            new ShowPopup(context).info(MESSAGE_TIME_OUT).show();
                            needToShowErrorDialog = false;
                        }
                    }
                if (listener != null)
                    listener.onLoadJsonError(throwable.toString(), needToShowErrorDialog);

            }
        };
    }

    public void setOnNoInternetListener(ShowPopup.OnPopupActionListener noInternerListener) {
        this.noInternerListener = noInternerListener;
    }

    public void loadData() {
        loadingTime = System.currentTimeMillis();
        if (!Utils.isNetworkConnected(context)) {
            if (Constants.noInternetDialog != null)
                Constants.noInternetDialog.dismiss();

            new ShowPopup(context).info("Không có kết nối internet,\n Xin vui lòng thử lại",
                    noInternerListener).show();
            if (listener != null)
                listener.onLoadJsonError("", false);
        } else {
            if (listener != null)
                listener.onLoadJsonLoading();
            needToShowErrorDialog = true;

            if (entity != null) {
                client.post(context, url, entity, Constants.TYPE_JSON, jsonHttpResponseHandler);
            } else {
                //application/x-www-form-urlencoded "text/html"
                client.post(context, url, null, requestParams, "application/x-www-form-urlencoded", jsonHttpResponseHandler);
            }
        }
    }

}
