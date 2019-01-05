package vn.izisolution;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import vn.izisolution.constant.LoadJson;
import vn.izisolution.utils.Debug;
import vn.izisolution.views.ValidateEditText;

public class WebViewActivity_BAK extends AppCompatActivity {

    private static final String BASE_URL = "http://toannm.odooviet.vn";
    private static final String URL = "http://toannm.odooviet.vn/web/login";
    private static final String URL_SIGNOUT = "http://toannm.odooviet.vn/web/logout";

    private static final String TAG_SCRIPT = "<script";
    private static final String TAG_SCRIPT_OPEN = "type=\"text/javascript\">";
    private static final String TAG_SCRIPT_CLOSE = "</script>";

    private static final String TAG_ODOO_OPEN = "odoo.define('web.csrf',function(require){";
    private static final String TAG_ODOO_CLOSE = "});";

    private static final String TAG_VAR_TOKEN = "vartoken=";

    //(\\S+) (.+?)
    private static final Pattern ptrnToken = Pattern.compile(TAG_SCRIPT_OPEN + ".*?" + TAG_SCRIPT_CLOSE);

    private WebView webView;
    private String token, data;
    private boolean sentToken = false;

    private Button login;
    private ValidateEditText input_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webView);

//        webView.getSettings().setJavaScriptEnabled(true); // enable javascript

//        webView.setWebViewClient(new WebViewClient() {
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(WebViewActivity_BAK.this, description, Toast.LENGTH_SHORT).show();
//            }
//
//            @TargetApi(android.os.Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
//                // Redirect to deprecated method, so you can use it in all SDK versions
//                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
//            }
//        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Debug.Log("shouldOverrideUrlLoading");
                return false;
            }
        });

        login = (Button) findViewById(R.id.login);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                initWebView();
            }
        };
        Thread t = new Thread(r);
        t.start();

//        webView.loadUrl(URL);

        input_token = (ValidateEditText) findViewById(R.id.input_token);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testtt(input_token.getText().toString());
            }
        });

    }

    private void initWebView() {
        initWebView(false);
    }

    private void login() {
//        RequestParams params = new RequestParams();
//        params.put("db", "toannm.odooviet.vn");
//        params.put("login", "admin");
//        params.put("password", "1");
//        params.put("csrf_token", token);
//
//        Debug.Log("login -> params -> " + params);
//
//        LoadJson loadJson = new LoadJson(WebViewActivity_BAK.this, URL,
//                params, new LoadJson.OnLoadJson() {
//            @Override
//            public void onLoadJsonLoading() {
//            }
//
//            @Override
//            public void onLoadJsonSuccess(JSONObject response) {
//                Debug.Log("WebViewActivity_BAK onLoadJsonSuccess -> " + response);
//            }
//
//            @Override
//            public void onLoadJsonError(String error, boolean needToShowErrorDialog) {
//                Debug.Log("WebViewActivity_BAK onErrorr -> " + error);
//            }
//        });
//        loadJson.loadData();


//        try {
//            String postData = "login=" + URLEncoder.encode("admin", "UTF-8")
//                    + "&password=" + URLEncoder.encode("1", "UTF-8")
//                    + "&db=" + URLEncoder.encode("toannm.odooviet.vn", "UTF-8")
//                    + "&csrf_token=" + URLEncoder.encode(input_token.getText().toString(), "UTF-8");
//
//            Debug.Log("postData -> " + postData);
//
//            webView.postUrl(URL, EncodingUtils.getBytes(postData, "BASE64")); //postData.getBytes()
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }


    }

    private void testtt(final String token) {
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(URL);
//
//                try {
//                    // Add your data
//                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                    nameValuePairs.add(new BasicNameValuePair("db", "toannm.odooviet.vn"));
//                    nameValuePairs.add(new BasicNameValuePair("login", "crm"));
//                    nameValuePairs.add(new BasicNameValuePair("password", "1"));
//                    nameValuePairs.add(new BasicNameValuePair("csrf_token", token));
//
//                    Debug.Log("testtt nameValuePairs -> " + nameValuePairs);
//
//                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    // Execute HTTP Post Request
//                    HttpResponse response = httpclient.execute(httppost);
//
//                    String strResponse = "";
//
//                    HttpEntity resEntity = response.getEntity();
//                    BufferedReader br = new BufferedReader(new InputStreamReader(
//                            resEntity.getContent(), "UTF-8"));
//                    String line;
//                    while (((line = br.readLine()) != null)) {
//                        strResponse = strResponse + line + "";
//                    }
//
//                    Debug.Log("response staus -> " + response.getStatusLine().getStatusCode());
//
//                } catch (ClientProtocolException e) {
//
//                } catch (IOException e) {
//
//                }
//            }
//        };
//        Thread t = new Thread(r);
//        t.start();

        RequestParams params = new RequestParams();
        params.put("db", "toannm.odooviet.vn");
        params.put("login", "admin");
        params.put("password", "1");
        params.put("csrf_token", token);

        LoadJson loadJson = new LoadJson(WebViewActivity_BAK.this, URL, params, new LoadJson.OnLoadJson() {
            @Override
            public void onLoadJsonLoading() {

            }

            @Override
            public void onLoadJsonSuccess(JSONObject response) {

            }

            @Override
            public void onLoadJsonError(String error, boolean needToShowErrorDialog) {

            }
        });
        loadJson.loadData();
    }


    private void initWebView(final boolean hasToken) {
        try {
//            postData = "login=" + URLEncoder.encode("admin", "UTF-8")
//                    + "&password=" + URLEncoder.encode("1", "UTF-8")
//                    + "&db_name=" + URLEncoder.encode("toannm.odooviet.vn", "UTF-8");
//            Debug.Log("postData -> " + postData);

//            RequestParams params = new RequestParams();
//            params.put("db", "toannm.odooviet.vn");
//            params.put("login", "admin");
//            params.put("password", "1");
//            if (sentToken)
//                params.put("csrf_token", token);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("db", "toannm.odooviet.vn"));
            nameValuePairs.add(new BasicNameValuePair("login", "crm"));
            nameValuePairs.add(new BasicNameValuePair("password", "1234"));
            if (hasToken) {
                nameValuePairs.add(new BasicNameValuePair("csrf_token", token));
            }

            Debug.Log("params -> " + nameValuePairs.toString());

//            StringEntity entity = new StringEntity(params.toString(),
//                    ContentType.APPLICATION_FORM_URLENCODED
//            );
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL);
            httppost.setHeader(HTTP.CONTENT_TYPE,
                    "application/x-www-form-urlencoded;charset=UTF-8");
//            httppost.setEntity(entity);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            httpclient.execute(httppost, new ResponseHandler<HttpResponse>() {
                @Override
                public HttpResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    Debug.Log("response -> " + response.getStatusLine().getStatusCode());
                    String line = "";
                    StringBuilder contentBuilder = new StringBuilder();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    while ((line = rd.readLine()) != null) {
                        contentBuilder.append(line);
                        if (sentToken)
                            Debug.Log(line);
                        else
                            Debug.Log("KHONG LOGGGGGGGGGG");
                    }
                    if (!sentToken)
                        data = contentBuilder.toString();

                    if (!sentToken) {
                        final String content = contentBuilder.toString();
                        content.replace("\n", "").replace(" ", "");

                        String[] contents = content.split(TAG_SCRIPT);

                        for (String c : contents) {
                            c = c.replace(" ", "").replace("\n", "");
                            final Matcher matcher = ptrnToken.matcher(c);

                            while (matcher.find()) {
                                String res = c.substring(matcher.start(), matcher.end());
                                res = res
                                        .replace(TAG_SCRIPT_OPEN, "").replace(TAG_SCRIPT_CLOSE, "")
                                        .replace(" ", "").replace("\n", "");

                                String[] results = res.replace(TAG_ODOO_OPEN, "").replace(TAG_ODOO_CLOSE, "")
                                        .split(";");

                                for (String r : results) {
                                    if (r.contains(TAG_VAR_TOKEN)) {
                                        token = r.replace("\"", "").replace(TAG_VAR_TOKEN, "");
                                        Debug.Log("result ->>> " + token);
                                        if (!sentToken) {
                                            Debug.Log("before initWebView");
                                            sentToken = true;

//                                            Handler mainHandler = new Handler(Looper.getMainLooper());
//                                            Runnable myRunnable = new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    login();
//                                                }
//                                            };
//                                            mainHandler.post(myRunnable);

                                            initWebView(true);
                                        }
                                    }

                                }
                            }

                        }
                    }

                    if (hasToken)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Debug.Log("runOnUiThread loadDataWithBaseURL");
                                webView.loadDataWithBaseURL(URL, data, "text/html", "UTF-8", null);
//                                webView.loadData(data, "text/html", "UTF-8");
//                                webView.loadUrl(URL);
                            }
                        });

                    return null;
                }
            });

            //webView.postUrl(URL, postData.getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Debug.Log("UnsupportedEncodingException -> ex -> " + e.getMessage());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Debug.Log("ClientProtocolException -> ex -> " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Debug.Log("IOException -> ex -> " + e.getMessage());
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals(URL_SIGNOUT)) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

//    private ArrayList pullLinks(String html) {
//        ArrayList links = new ArrayList();
//        Elements srcs = Jsoup.parse(html).select("[" + TAG_SCRIPT_OPEN + "]"); //get All tags containing "src"
//        Debug.Log("srcs -> " + srcs.toString());
//        for (int i = 0; i < srcs.size(); i++) {
//            links.add(srcs.get(i).attr("abs:" + TAG_SCRIPT_OPEN)); // get links of selected tags
//        }
//        return links;
//    }

}
