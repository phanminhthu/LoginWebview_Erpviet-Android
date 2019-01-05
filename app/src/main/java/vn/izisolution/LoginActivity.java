package vn.izisolution;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import vn.izisolution.adapter.AppSpinnerAdapter;
import vn.izisolution.constant.Constants;
import vn.izisolution.constant.LoadJson;
//import vn.izisolution.constant.XmlRpcClient;
import vn.izisolution.utils.Debug;
import vn.izisolution.utils.HttpRequestClient;
import vn.izisolution.utils.MyRequestClient;
import vn.izisolution.utils.SharedPref;
import vn.izisolution.utils.ShowPopup;
import vn.izisolution.utils.Utils;
import vn.izisolution.views.FontTextView;
import vn.izisolution.views.ValidateEditText;

import static vn.izisolution.constant.Constants.TAG_SCRIPT_CLOSE;
import static vn.izisolution.constant.Constants.TAG_SCRIPT_OPEN;
import static vn.izisolution.constant.Constants.TAG_SUCCESS;
import static vn.izisolution.constant.Constants.TAG_SUCCESS_1;
import static vn.izisolution.constant.Constants.TAG_VAR_TOKEN;
import static vn.izisolution.constant.Constants.ptrnToken;

/**
 * Created by ToanNMDev on 3/6/2017.
 */
/*update done by TuSinh on 28/12/2018/*/

public class LoginActivity extends Activity implements View.OnClickListener {

    private LinearLayout mainLayout;
    private TextView login, signin;
    private ValidateEditText company, username, password;

    private AppCompatSpinner spinnerLanguage;
    private AppSpinnerAdapter appSpinnerAdapter;

    private Dialog loadingDialog;
    private ShowPopup showPopup;
    PersistentCookieStore myCookieStore; // save cookie on asynchttpclient to sync webview. important !!!
    MyRequestClient myRequestClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myRequestClient = MyRequestClient.getInstance();
//        myRequestClient.cancelAllRequests(true);

//        JSONObject mainJson = new JSONObject();
//        StringEntity entity = new StringEntity(mainJson.toString(), ContentType.APPLICATION_JSON);
//        String url = "https://khangminh.erpviet.vn/web/database/list";
////        String url = "https://erp.vmt.vn/web/database/get_list";
////        String url = "https://postb.in/grskkHxu";
//        LoadJson loadJson = new LoadJson(LoginActivity.this, url, entity, new LoadJson.OnLoadJson() {
//            @Override
//            public void onLoadJsonLoading() {
//
//            }
//
//            @Override
//            public void onLoadJsonSuccess(JSONObject response) {
//                Debug.Log("response loadjson: " + response);
//            }
//
//            @Override
//            public void onLoadJsonError(String error, boolean needToShowErrorDialog) {
//
//            }
//        });
//        loadJson.loadData();


        showPopup = new ShowPopup(LoginActivity.this);

        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(this);

        signin = (FontTextView) findViewById(R.id.signin);
        signin.setOnClickListener(this);
        username = (ValidateEditText) findViewById(R.id.username);
        password = (ValidateEditText) findViewById(R.id.password);
        company = (ValidateEditText) findViewById(R.id.company);


        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Utils.hideKeyBoard(LoginActivity.this);
                boolean c = company.invalidateInput("");
                boolean b = username.invalidateInput("");
                boolean a = password.invalidateInput("");
                if (c && a && b)
                    Selectdatabase(Constants.HTTP, company.getText().toString().replace(" ", ""));
                return true;
            }
        });

        Utils.setupUI(mainLayout, LoginActivity.this);

//        username.setText(SharedPref.getString(LoginActivity.this, SharedPref.LOGIN));
//        password.setText(SharedPref.getString(LoginActivity.this, SharedPref.USER_PASSWORD));

        spinnerLanguage = (AppCompatSpinner) findViewById(R.id.spinner_language);
        String[] arr = getResources().getStringArray(R.array.array_languages);
        appSpinnerAdapter = new AppSpinnerAdapter(LoginActivity.this, arr);
        appSpinnerAdapter.setTextColor(Color.WHITE);
        appSpinnerAdapter.setLayoutType(AppSpinnerAdapter.LAYOUT_TYPE_GRAVITY_CENTER);
        spinnerLanguage.setAdapter(appSpinnerAdapter);
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Configuration config = new Configuration();
                switch (i) {
                    case 0:
//                        config.locale = Locale.ENGLISH;
//                        Language.setLanguage(LoginActivity.this, "vi_VN");
                        break;
                    case 1:
//                        Language.setLanguage(LoginActivity.this, "en_US");
//                        config.locale = Locale.FRENCH;
                        break;
                    default:
//                        Language.setLanguage(LoginActivity.this, "vi_VN");
//                        config.locale = Locale.ENGLISH;
                        break;
                }
//                getResources().updateConfiguration(config, null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Utils.clearNotifications(LoginActivity.this);


        // autologin
        String sDomain = SharedPref.getString(LoginActivity.this, SharedPref.DOMAIN);
        String sProtocol = SharedPref.getString(LoginActivity.this, SharedPref.PROTOCOL);
        String sUserName = SharedPref.getString(LoginActivity.this, SharedPref.LOGIN);
        String sPassword = SharedPref.getString(LoginActivity.this, SharedPref.USER_PASSWORD);
        String sDatabaseName = SharedPref.getString(LoginActivity.this, SharedPref.NAME_DATABASE);
//
        if (!sDomain.equals("") && !sUserName.equals("") && !sPassword.equals("") && !sProtocol.equals("")) {
            company.setText(sDomain);
            username.setText(sUserName);
            password.setText(sPassword);
            request_url_login(
                    sProtocol,
                    sDomain,
                    sDatabaseName,
                    sUserName,
                    sPassword
            );
        }
        // autologin

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                boolean c = company.invalidateInput("");
                boolean b = username.invalidateInput("");
                boolean a = password.invalidateInput("");
                if (c && a && b)
                    Selectdatabase(Constants.HTTP, company.getText().toString().replace(" ", ""));
//                    _startMainActivity("taiphat_erp",Constants.HTTPS);
//                    login(company.getText().toString(),
//                            username.getText().toString(), password.getText().toString());
                break;
            case R.id.signin:
                _startSigninActivity();
                break;
        }
    }

    private void Selectdatabase(String protocol, String domain) {
        JSONObject mainObject = new JSONObject();
        StringEntity entity = new StringEntity(mainObject.toString(), ContentType.APPLICATION_JSON);

        myRequestClient.
                postJson(LoginActivity.this,
                        protocol + domain + Constants.URL_GET_DB_ODOO11,
                        entity,
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                Debug.Log("Selectdatabase->response: " + response.toString());
                                try {
                                    JSONArray result = response.getJSONArray("result");
                                    if (result.length() > 0) {
                                        String[] dataBase = new String[result.length()];
                                        for (int i = 0; i < result.length(); i++) {
                                            dataBase[i] = result.get(i).toString();
//                            Debug.Log("dataBase: " + dataBase[i]);
                                        }

                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setTitle("Chọn database");
                                        builder.setItems(dataBase, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int position) {
//                                                Toast.makeText(LoginActivity.this, dataBase[position], Toast.LENGTH_LONG).show();
                                                request_url_login(
                                                        protocol,
                                                        domain,
                                                        dataBase[position],
                                                        username.getText().toString().replace(" ", ""),
                                                        password.getText().toString().replace(" ", "")
                                                );
                                            }
                                        });
                                        builder.show();


                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                Debug.Log("statusCode onFailure String: " + statusCode);
                                Debug.Log("loadJsonError -> String:" + responseString + "---throwable: " + throwable.toString());

                                if (statusCode == 301) {
                                    Selectdatabase(Constants.HTTPS, domain);
                                }
//                                else if (statusCode == 0) {
//                                    Toast.makeText(LoginActivity.this, "Vui lòng kiểm tra lại tên miền", Toast.LENGTH_LONG).show();
//                                }


                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Debug.Log("statusCode onFailure JSONArray: " + statusCode);
//                                if (statusCode == 0) {
//                                    Toast.makeText(LoginActivity.this, "Vui lòng kiểm tra lại tên miền", Toast.LENGTH_LONG).show();
//                                }
                                Debug.Log("loadJsonError -> JSONArray:" + errorResponse.toString() + "---throwable: " + throwable.toString());

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Debug.Log("statusCode onFailure JSONObject: " + statusCode);
//                                if (statusCode == 0) {
//                                    Toast.makeText(LoginActivity.this, "Vui lòng kiểm tra lại tên miền", Toast.LENGTH_LONG).show();
//                                }
                                Debug.Log("loadJsonError -> JSONObject:" + errorResponse.toString() + "---throwable: " + throwable.toString());

                            }
                        });
    }

    private void request_url_login(String protocol, String domain, String namedb, String username, String password) {
        myCookieStore = new PersistentCookieStore(LoginActivity.this);
        if (myCookieStore != null) {
            myCookieStore.clear();
        }
        myRequestClient.setCookie(myCookieStore);
//        RequestParams requestParams = new RequestParams();
//        requestParams.setHttpEntityIsRepeatable(true);
        myRequestClient.get(protocol + domain + Constants.URL_LOGIN + namedb,null, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Debug.Log("request_url_login->onFailure statusCode: " + statusCode);
                Debug.Log("request_url_login->onFailure error: " + throwable.toString());
                Debug.Log("request_url_login->onFailure responseString: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Debug.Log("request_url_login->onSuccess statuccode: " + statusCode);

//                Debug.Log("request_url_login->onSuccess body: " + body);

                final Matcher matcher = ptrnToken.matcher(responseString);
                if (matcher.find()) {

                    String res = responseString.substring(matcher.start(), matcher.end());
                    res = res
                            .replace(TAG_SCRIPT_OPEN, "").replace(TAG_SCRIPT_CLOSE, "")
                            .replace(" ", "").replace("\n", "");

                    String token = res.replace("\"", "").replace(TAG_VAR_TOKEN, "");
                    Debug.Log("token: " + token);

                    post_login(token, protocol, domain, namedb, username, password);


                }
            }
        });


//        myRequestClient.get(protocol + domain + Constants.URL_LOGIN + namedb, null, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                Debug.Log("request_url_login->onSuccess statuccode: " + statusCode);
//
//                String body = new String(responseBody);
////                Debug.Log("request_url_login->onSuccess body: " + body);
//
//                final Matcher matcher = ptrnToken.matcher(body);
//                if (matcher.find()) {
//
//
//                    String res = body.substring(matcher.start(), matcher.end());
//                    res = res
//                            .replace(TAG_SCRIPT_OPEN, "").replace(TAG_SCRIPT_CLOSE, "")
//                            .replace(" ", "").replace("\n", "");
//
//                    String token = res.replace("\"", "").replace(TAG_VAR_TOKEN, "");
//                    Debug.Log("token: " + token);
//
//
//                    post_login(token, protocol, domain, namedb, username, password);
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                Debug.Log("request_url_login->onFailure statusCode: " + statusCode);
//                Debug.Log("request_url_login->onFailure error: " + error.toString());
//
//            }
//        });
    }

    private void post_login(String token, String protocol, String domain, String namedb, String username, String password) {

        RequestParams requestParams = new RequestParams();
        requestParams.put("login", username);
        requestParams.put("password", password);
        requestParams.put("csrf_token", token);
        Debug.Log("requestparams:" + requestParams.toString());
        StringEntity entity = null;
        try {
            entity = new StringEntity(requestParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        myRequestClient.postLogin(LoginActivity.this,
                protocol + domain + Constants.URL_LOGIN + namedb,
                entity,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Debug.Log("post_login onSuccess statusCode: " + statusCode);
                        String body = new String(responseBody);
                        Debug.Log(" post_login onSuccess body:" + body);
                        String content = body.replace("\n", "").replace(" ", "");

                        if ((content.contains(TAG_SUCCESS) || content.contains(TAG_SUCCESS_1))) {

                            CookieSyncManager.createInstance(getApplicationContext());
                            CookieManager.getInstance().removeAllCookie();
                            CookieManager cookieManager = CookieManager.getInstance();
                            cookieManager.setAcceptCookie(true);
                            cookieManager.removeSessionCookie();

                            List<Cookie> cookies = myCookieStore.getCookies();
                            for (Cookie eachCookie : cookies) {
                                String cookieString = eachCookie.getName() + "=" + eachCookie.getValue();
                                cookieManager.setCookie(eachCookie.getDomain(), cookieString); // important !!!
                                //System.err.println(">>>>> " + "cookie: " + cookieString);
                                Debug.Log("cookie Domain: " + eachCookie.getDomain());
                                Debug.Log("cookie: " + cookieString);
                            }
                            CookieSyncManager.getInstance().sync();


                            _setDataLogin(domain, username, password, namedb, protocol);

                            Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                            intent.putExtra("PROTOCOL", protocol);
                            intent.putExtra("DOMAIN", domain);
                            intent.putExtra("USERNAME", username);
                            intent.putExtra("PASSWORD", password);
                            startActivity(intent);
                            finish();


                        } else {
                            Toast.makeText(LoginActivity.this, "Tên người dùng hoặc mật khẩu không đúng,vui lòng kiểm tra lại.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Debug.Log("post_login->onFailure statusCode: " + statusCode);
                        Debug.Log("post_login->onFailure error: " + error.toString());
                    }
                });
    }


    private void _test() {
//        try {
//            XMLRPCClient client = new XMLRPCClient(new URL(url));
//
//            RequestParams params = new RequestParams();
//            params.put("", "demo68.erpviet.vn");
//            params.put("", "demo68.erpviet.vn");
//            params.put("", "demo68.erpviet.vn");
//
//            List<String> strs = new ArrayList<String>();
//            strs.add("demo68.erpviet.vn");
//            strs.add("admin");
//            strs.add("erpviet123");
//
////            Integer i = (Integer)client.call("add", 5, 10);
//            client.callAsync(new XMLRPCCallback() {
//                @Override
//                public void onResponse(long id, Object result) {
//                    Debug.Log("onResponse result -> " + result);
//                }
//
//                @Override
//                public void onError(long id, XMLRPCException error) {
//                    Debug.Log("onError error -> " + error);
//                }
//
//                @Override
//                public void onServerError(long id, XMLRPCServerException error) {
//                    Debug.Log("onServerError -> " + error);
//                }
//            }, "authenticate", "demo68.erpviet.vn", "admin", "erpviet123", "");
//
////        } catch(XMLRPCServerException ex) {
////            // The server throw an error.
////            Debug.Log("_____XMLRPCServerException -> " + ex.getMessage());
////        } catch(XMLRPCException ex) {
////            // An error occured in the client.
////            Debug.Log("_____XMLRPCException -> " + ex.getMessage());
//        } catch(Exception ex) {
//            // Any other exception
//            Debug.Log("_____Exception -> " + ex.getMessage());
//        }
    }

    private void login(String domain,
                       final String userName, final String password) {


//        _startMainActivity();
//        domain += ".erpviet.vn";
//        String url = String.format("%s/xmlrpc/2/common", Utils.checkDomain(LoginActivity.this, company.getText().toString()).replace(" ", ""));
//        Debug.Log("domain -> " + domain);
//        Debug.Log("url -> " + url);
//
//        XmlRpcClient client = new XmlRpcClient(LoginActivity.this, url, new XmlRpcClient.OnLoad() {
//            @Override
//            public void onLoading() {
//
//            }
//
//            @Override
//            public void onSuccess(Object response) {
//                Debug.Log("response -> " + response);
//                _setDataLogin();
//                _startMainActivity();
//            }
//
//            @Override
//            public void onError(String error, boolean needToShowErrorDialog) {
//                Debug.Log("onError -> " + error);
//            }
//
//            @Override
//            public void onServerError(String error, boolean needToShowErrorDialog) {
//                Debug.Log("onServerError -> " + error);
//
//            }
//        });
//        //"demo68.erpviet.vn" "admin" "erpviet123"
//        client.callAsync("authenticate", domain, userName, password, "");

//        String prefixDomain = getResources().getString(R.string.prefix_domain);
//        if (!domain.endsWith(prefixDomain)) {
//            domain += prefixDomain;
//        }
//
//        JSONObject mainObject = new JSONObject();
//        try {
//            mainObject.put("db_name", domain);
//            mainObject.put("login", userName);
//            mainObject.put("password", password);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        final Dialog loadingDialog = new ShowPopup(LoginActivity.this).loading(false);

//        LoadJson loadJson = new LoadJson(LoginActivity.this, Constants.API_LOGIN,
//                new StringEntity(mainObject.toString(), ContentType.APPLICATION_JSON), new LoadJson.OnLoadJson() {
//            @Override
//            public void onLoadJsonLoading() {
//                loadingDialog.show();
//            }
//
//            @Override
//            public void onLoadJsonSuccess(JSONObject response) {
//                try {
//                    int code = response.getJSONArray("result").getJSONObject(0).getInt("code");
//                    if (code == 1) {
//                        JSONObject d = response.getJSONArray("result").getJSONObject(0)
//                                .getJSONObject("data");
//                        String hasPermissionCrm = d.getJSONObject("roles").getString("role");
//                        String hasPermissionAccount = d.getJSONObject("roles").getString("role_account");
//                        String hasPermissionStock = d.getJSONObject("roles").getString("role_stock");
//                        String hasPermissionHr = d.getJSONObject("roles").getString("role_hr");
//                        _setDataLogin(hasPermissionCrm, hasPermissionAccount, hasPermissionStock, hasPermissionHr);
//                        _startMainActivity();
//                    } else {
//                        String message = response.getJSONArray("result").getJSONObject(0).getString("message");
//                        new ShowPopup(LoginActivity.this).info(message).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                loadingDialog.dismiss();
//            }
//
//            @Override
//            public void onLoadJsonError(String error, boolean needToShowErrorDialog) {
//                loadingDialog.dismiss();
//                if (needToShowErrorDialog)
//                    new ShowPopup(LoginActivity.this).info(error).show();
//            }
//        });
//        loadJson.loadData();
    }

    private void _startMainActivity(String namedb, String protocol) {
        _startMainActivity(
                Utils.checkDomain(LoginActivity.this, company.getText().toString(), protocol).replace(" ", ""),
                username.getText().toString().replace(" ", ""),
                password.getText().toString().replace(" ", ""),
                company.getText().toString(),
                namedb);
    }

    private void _startMainActivity(final String domain, final String userName, final String password, final String company, String namedb) {
        loading();
        HttpRequestClient.getInstance(LoginActivity.this).usingHttpClient(new HttpRequestClient.OnHttpRequestCallback() {
            @Override
            public void onLoading() {
            }

            @Override
            public void onSuccess() {
                _setDataLogin(company, userName, password, namedb);
                Handler mainHandler = new Handler(Looper.getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null)
                            loadingDialog.dismiss();
                    }
                };
                mainHandler.post(myRunnable);
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                Debug.Log("domain intent: " + domain);
                intent.putExtra("DOMAIN", domain);
//                intent.putExtra("DOMAIN", domain);
                intent.putExtra("USERNAME", userName);
                intent.putExtra("PASSWORD", password);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure() {
                if (loadingDialog != null)
                    loadingDialog.dismiss();
            }

            @Override
            public void onDatabaseIsNotExists() {
                showError("Database không đúng, vui lòng thử lại.");
            }

            @Override
            public void onPasswordIncorrect() {
                showError("Tài khoản hoặc mật khẩu chưa chính xác.");
            }
        }, domain, userName, password, namedb);

    }

    private void _startSigninActivity() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
        Utils.killApp(false);
    }

    private void _setDataLogin(String... data) {
        SharedPref.saveString(LoginActivity.this, SharedPref.DOMAIN, data[0]);
        SharedPref.saveString(LoginActivity.this, SharedPref.LOGIN, data[1]);
        SharedPref.saveString(LoginActivity.this, SharedPref.USER_PASSWORD, data[2]);
        SharedPref.saveString(LoginActivity.this, SharedPref.NAME_DATABASE, data[3]);
        SharedPref.saveString(LoginActivity.this, SharedPref.PROTOCOL, data[4]);

//        SharedPref.saveString(LoginActivity.this, SharedPref.HAS_PERMISSION_CRM, data[0]);
//        SharedPref.saveString(LoginActivity.this, SharedPref.HAS_PERMISSION_ACCOUNT, data[1]);
//        SharedPref.saveString(LoginActivity.this, SharedPref.HAS_PERMISSION_STOCK, data[2]);
//        SharedPref.saveString(LoginActivity.this, SharedPref.HAS_PERMISSION_HR, data[3]);
//        Debug.Log("setDataLogin -> data -> " + data);
    }

    private void loading() {
        if (loadingDialog == null)
            loadingDialog = showPopup.loading(false);
        loadingDialog.show();
    }

    private void showError(final String msg) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null)
                    loadingDialog.dismiss();
                showPopup.info(msg).show();
            }
        };
        mainHandler.post(myRunnable);
    }
}
