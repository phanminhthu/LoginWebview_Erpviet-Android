package vn.izisolution.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.entity.StringEntity;
import vn.izisolution.constant.Constants;

/*creat by tusinh on 28/12/2018 */
public class MyRequestClient {
    private static MyRequestClient instance = null;
    private  AsyncHttpClient client;
//    private PersistentCookieStore myCookieStore = null;


    private MyRequestClient() {
        init();
    }

    /* làm singleton thì thỉnh thoảng back từ màn hình webview về thì autologin lại error  statuscode: 0 của asynchttpclient*/
    /*Chuyển về  không check == null nữa thì thấy ổn => đóe hiểu sao .....*/
    public static MyRequestClient getInstance() {
//        if (instance == null) {
        instance = new MyRequestClient();
//        }
        return instance;
    }

    public  void init() {
        client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.setMaxRetriesAndTimeout(0, Constants.TIME_OUT);
        client.setConnectTimeout(Constants.TIME_OUT);
        client.setTimeout(Constants.TIME_OUT);
        client.setResponseTimeout(Constants.TIME_OUT);
    }
//    public static AsyncHttpClient getAsyncHttpClient() {
//        return new AsyncHttpClient();
//    }
//
//    public void cancelAllRequests(boolean mayInterruptflRunning) {
//        if (client != null)
//            client.cancelAllRequests(mayInterruptflRunning);
////        instance = new MyRequestClient();
//    }

    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Debug.Log("requestGET url:" + url);
        client.addHeader("Content-Type", "text/html");
        client.get(url, params, responseHandler);
    }

    public void postLogin(Context context, String url, StringEntity entity, AsyncHttpResponseHandler responseHandler) {
        //  client.addHeader("Accept", "application/json");
        Debug.Log("postLogin url: " + url);
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        entity.setContentType("application/json");
        client.setUserAgent("android");
        client.post(context, url, entity, "application/json", responseHandler);
    }

    public void postJson(Context context, String url, StringEntity entity, JsonHttpResponseHandler jsonHttpResponseHandler) {
        Debug.Log("postJson url: " + url);
        client.addHeader("Accept", "application/json");
        client.addHeader(AsyncHttpClient.HEADER_CONTENT_TYPE, "application/json");
        client.post(context, url, entity, Constants.TYPE_JSON, jsonHttpResponseHandler);
    }

    public void setCookie(PersistentCookieStore myCookieStore) {
        client.setCookieStore(myCookieStore);
    }

    public AsyncHttpClient getClient() {
        return client;
    }
}
