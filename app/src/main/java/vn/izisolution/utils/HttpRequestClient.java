package vn.izisolution.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.loopj.android.http.HttpGet;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;

import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.scheme.SocketFactory;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.conn.ssl.X509HostnameVerifier;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.conn.SingleClientConnManager;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;

import vn.izisolution.constant.Constants;

import static cz.msebera.android.httpclient.HttpHeaders.USER_AGENT;

public class HttpRequestClient {

    private static final String URL_LOGIN = "/web/login";
//    public static final String URL_GET_DB_ODOO8 = "/web/database/get_list";
    public static final String URL_GET_DB_ODOO11 = "/web/database/list";

    private static final String TAG_SUCCESS = "+location.hash;";
    private static final String TAG_SUCCESS_1 = "Redirecting";
    //<html><head><script>window.location='/web?'+location.hash;</script></head></html>
    private static final String TAG_SCRIPT = "<script";
    private static final String TAG_SCRIPT_OPEN = "<input type=\"hidden\" name=\"csrf_token\" value=";
    private static final String TAG_SCRIPT_CLOSE = "/>";

    private static final String TAG_ODOO_OPEN = "odoo.define('web.csrf',function(require){";
    private static final String TAG_ODOO_CLOSE = "});";

    private static final String TAG_VAR_TOKEN = "vartoken=";
    static int Flag = 1;
    static final int FLAGERROR = 1;
    static final int FLAGOK = 2;


    //(\\S+) (.+?)
    private static final Pattern ptrnToken = Pattern.compile(TAG_SCRIPT_OPEN + ".*?" + TAG_SCRIPT_CLOSE);

    private static Context context;
    public static HttpRequestClient instance;
    private static ShowPopup showPopup;

    public HttpRequestClient(Context context) {
        this.context = context;
    }

    public interface OnHttpRequestCallback {

        void onLoading();

        void onSuccess();

        void onFailure();

        void onDatabaseIsNotExists();

        void onPasswordIncorrect();
    }
    public interface OnHttpRequestCallbackParams{
        void onLoading();

        void onSuccess(JSONObject jsonObject);

        void onFailure();

        void onDatabaseIsNotExists();

        void onPasswordIncorrect();
        void onrequesthttps();
    }

    public static HttpRequestClient getInstance(Context context) {
        if (instance == null) {
            instance = new HttpRequestClient(context);
            showPopup = new ShowPopup(context);
        }
        return instance;
    }

    public static void usingHttpClient(final OnHttpRequestCallback requestCallback, final String... args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                _usingHttpClient(requestCallback, args);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    public static void usingHttpClientgetDatabase(final OnHttpRequestCallbackParams requestCallbackParams, String urlDb,String protocol, final String... args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                _usingHttpClientgetDatabase(requestCallbackParams, urlDb, protocol,args);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private static void _usingHttpClient(final OnHttpRequestCallback requestCallback, final String... args) {
        final DefaultHttpClient httpclient = new DefaultHttpClient();

        CookieSyncManager.createInstance(context);
        final CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();

        if (!Utils.isNetworkConnected(context)) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    new ShowPopup(context).info("Không có kết nối internet. Xin vui lòng thử lại!!!").show();
                }
            };
            mainHandler.post(myRunnable);

            if (requestCallback != null)
                requestCallback.onFailure();

        } else {

            try {
                if (requestCallback != null)
                    requestCallback.onLoading();

                final URI uri = new URI(args[0] + URL_LOGIN+"?db="+args[3]);
                Debug.Log("uri login: "+uri.toString());
                HttpGet httpGet = new HttpGet(uri);

                httpclient.execute(httpGet, new ResponseHandler<HttpResponse>() {
                    @Override
                    public HttpResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                        String line = "";
                        StringBuilder contentBuilder = new StringBuilder();
                        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        while ((line = rd.readLine()) != null) {
//                            Debug.Log("line "+line);
                            contentBuilder.append(line);
                        }

                        final String content = contentBuilder.toString();

                        content.replace("\n", "").replace(" ", "");

                        final Matcher matcher = ptrnToken.matcher(content);

                        if (matcher.find()) {
                            String res = content.substring(matcher.start(), matcher.end());
                            res = res
                                    .replace(TAG_SCRIPT_OPEN, "").replace(TAG_SCRIPT_CLOSE, "")
                                    .replace(" ", "").replace("\n", "");

                            String token = res.replace("\"", "").replace(TAG_VAR_TOKEN, "");

                            List<Cookie> cookies = httpclient.getCookieStore().getCookies();
                            if (!cookies.isEmpty()) {
                                CookieSyncManager.createInstance(context);
                                CookieManager cookieManager = CookieManager.getInstance();
                                for (Cookie cookie : cookies) {
                                    Cookie sessionInfo = cookie;
                                    String cookieString = sessionInfo.getName() + "=" + sessionInfo.getValue() + "; domain=" + sessionInfo.getDomain();
                                    cookieManager.setCookie(args[0], cookieString);
                                    CookieSyncManager.getInstance().sync();
                                }
                            }



                            HttpPost httpPost = new HttpPost(uri);

                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                            nameValuePairs.add(new BasicNameValuePair("db", args[3]
//                                    .replace("http://", "")
//                                    .replace("https://", "")));
                            nameValuePairs.add(new BasicNameValuePair("login", args[1]));
                            nameValuePairs.add(new BasicNameValuePair("password", args[2]));
                            nameValuePairs.add(new BasicNameValuePair("csrf_token", token));
//                            nameValuePairs.add(new BasicNameValuePair("token", token));

                            Debug.Log("nameValuePairs -> " + nameValuePairs);

                            httpPost.setHeader(HTTP.CONTENT_TYPE,
                                    "application/x-www-form-urlencoded;charset=UTF-8");
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                            httpclient.execute(httpPost, new ResponseHandler<HttpResponse>() {

                                @Override
                                public HttpResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                                    String line = "";
                                    StringBuilder contentBuilder = new StringBuilder();
                                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                                    while ((line = rd.readLine()) != null) {
                                        contentBuilder.append(line);
                                        Debug.Log(line);
                                    }

                                    String content = contentBuilder.toString();
//                                    Debug.Log("content -> " + content);
                                    content = content.replace("\n", "").replace(" ", "");

                                    if (requestCallback != null && (content.contains(TAG_SUCCESS) || content.contains(TAG_SUCCESS_1)))
                                        requestCallback.onSuccess();
                                    else if (requestCallback != null)
                                        requestCallback.onPasswordIncorrect();

                                    return null;
                                }
                            });

                        } else {
                            if (requestCallback != null)
                                requestCallback.onDatabaseIsNotExists();
                        }

                        return null;
                    }
                });

            } catch (URISyntaxException e) {
                Debug.Log("URISyntaxException :"+e.toString());
                if (requestCallback != null)

//                    requestCallback.onDatabaseIsNotExists();
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                Debug.Log("ClientProtocolException :"+e.toString());
                if (requestCallback != null)
//                    requestCallback.onDatabaseIsNotExists();
                e.printStackTrace();
            } catch (IOException e) {
                Debug.Log("IOException :"+e.toString());
                if (requestCallback != null)
//                    requestCallback.onDatabaseIsNotExists();
                e.printStackTrace();
            }
        }
    }

    private static void _usingHttpClientgetDatabase(final OnHttpRequestCallbackParams requestCallbackParams, String urlData,String protocol, final String... args) {

        final DefaultHttpClient httpclient = new DefaultHttpClient();

        Debug.Log("arg[0]: "+args[0]);
        if(protocol.equals(Constants.HTTPS)){
            Debug.Log("start code resolve sslexception");
            HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
            sslSocketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", (SocketFactory) sslSocketFactory,443));
            SingleClientConnManager mgr = new SingleClientConnManager(httpclient.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, httpclient.getParams());
            // Set verifier
//            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            // Do not do this in production!!!
            HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier(){
                public boolean verify(String string,SSLSession ssls) {
                    return true;
                }
            });
            Debug.Log("end code resolve sslexception");
        }

//        CookieSyncManager.createInstance(context);
//        final CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();

        if (!Utils.isNetworkConnected(context)) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    new ShowPopup(context).info("Không có kết nối internet. Xin vui lòng thử lại!!!").show();
                }
            };
            mainHandler.post(myRunnable);

            if (requestCallbackParams != null)
                requestCallbackParams.onFailure();

        } else {

            try {
                if (requestCallbackParams != null)
                    requestCallbackParams.onLoading();
                String url = Utils.checkDomain1(args[0],protocol);
//                Debug.Log("args[0]: " + args[0]);
//                Debug.Log("urlData: " + urlData);
                final URI uri = new URI(url + urlData);
//                final URI uri = new URI(url);
                Debug.Log("uri: " + uri.toString());
                HttpPost httpPost = new HttpPost(uri);

//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("{}", "{}"));
//                Debug.Log("nameValuePairs -> " + nameValuePairs);
                httpPost.setHeader(HTTP.CONTENT_TYPE,
                        "application/json");
                JSONObject json = new JSONObject();
                StringEntity se = new StringEntity(json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(se);
//                httpPost.setHeader("User-Agent", USER_AGENT);
                httpclient.execute(httpPost, new ResponseHandler<HttpResponse>() {

                    @Override
                    public HttpResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                        Debug.Log("HttpResponse: " + response.toString());
//                        if (Flag == FLAGERROR) {
//                            Flag = 3;
//                            _usingHttpClientgetDatabase(requestCallback, URL_GET_DB_ODOO11, args[0]);
//                        }
                        if (response.getStatusLine().getStatusCode() == HttpsURLConnection.HTTP_OK) {
//                            Flag = FLAGOK;
//                            Debug.Log("HttpResponse OK: " + response.toString());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                            StringBuilder builder = new StringBuilder();
                            for (String line = null; (line = reader.readLine()) != null;) {
                                builder.append(line).append("\n");
                            }
                            JSONTokener tokener = new JSONTokener(builder.toString());
                            try {
                                JSONObject finalResult = new JSONObject(tokener);
                                if(requestCallbackParams != null){
                                    requestCallbackParams.onSuccess(finalResult);
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                                Debug.Log("e1: " + e1.toString());
                            }
                        } else if(response.getStatusLine().getStatusCode() == HttpsURLConnection.HTTP_MOVED_PERM){
                            _usingHttpClientgetDatabase(requestCallbackParams, urlData,Constants.HTTPS, args[0]);
                        }else {
//                            Flag = FLAGERROR;
//                            _usingHttpClientgetDatabase(requestCallbackParams, urlData,protocol, args[0]);
                        }
                        return null;
                    }
                });

            } catch (URISyntaxException e) {
                if (requestCallbackParams != null)
                    requestCallbackParams.onDatabaseIsNotExists();
                e.printStackTrace();
                Debug.Log("URISyntaxException :"+e.toString());
            } catch (ClientProtocolException e) {
                if (requestCallbackParams != null)
                    requestCallbackParams.onDatabaseIsNotExists();
                e.printStackTrace();
                Debug.Log("ClientProtocolException :"+e.toString());
            } catch (IOException e) {
                if (requestCallbackParams != null)
                    requestCallbackParams.onrequesthttps();
                e.printStackTrace();
                Debug.Log("IOException :"+e.toString());
            }
        }
    }

}
