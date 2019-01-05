package vn.izisolution;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.izisolution.adapter.AppSpinnerAdapter;
import vn.izisolution.constant.Constants;
import vn.izisolution.utils.Debug;
import vn.izisolution.utils.HttpRequestClient;
import vn.izisolution.utils.SharedPref;
import vn.izisolution.utils.ShowPopup;
import vn.izisolution.utils.Utils;
import vn.izisolution.views.FontTextView;
import vn.izisolution.views.ValidateEditText;

/**
 * Created by ToanNMDev on 3/6/2017.
 */

public class LoginActivity_old extends Activity implements View.OnClickListener {

    private LinearLayout mainLayout;
    private TextView login, signin;
    private ValidateEditText company, username, password;

    private AppCompatSpinner spinnerLanguage;
    private AppSpinnerAdapter appSpinnerAdapter;

    private Dialog loadingDialog;
    private ShowPopup showPopup;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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


        showPopup = new ShowPopup(LoginActivity_old.this);

        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(this);

        signin = (FontTextView) findViewById(R.id.signin);
        signin.setOnClickListener(this);
        username = (ValidateEditText) findViewById(R.id.username);
        password = (ValidateEditText) findViewById(R.id.password);
        company = (ValidateEditText) findViewById(R.id.company);
        company.setText(SharedPref.getString(LoginActivity_old.this, SharedPref.DOMAIN));

        company.setText("taiphat.erpviet.vn");
        username.setText("system");
        password.setText("4skq3ZdTWDzz6xWk");

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Selectdatabase();
//                login(company.getText().toString(), username.getText().toString(), password.getText().toString());
                return true;
            }
        });

        Utils.setupUI(mainLayout, LoginActivity_old.this);

//        username.setText(SharedPref.getString(LoginActivity.this, SharedPref.LOGIN));
//        password.setText(SharedPref.getString(LoginActivity.this, SharedPref.USER_PASSWORD));

        spinnerLanguage = (AppCompatSpinner) findViewById(R.id.spinner_language);
        String[] arr = getResources().getStringArray(R.array.array_languages);
        appSpinnerAdapter = new AppSpinnerAdapter(LoginActivity_old.this, arr);
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

        Utils.clearNotifications(LoginActivity_old.this);

//        String sDomain = Utils.checkDomain(LoginActivity.this, SharedPref.getString(LoginActivity.this, SharedPref.DOMAIN), Constants.HTTP);
////        String sDomain = SharedPref.getString(LoginActivity.this, SharedPref.DOMAIN);
//        String sUserName = SharedPref.getString(LoginActivity.this, SharedPref.LOGIN);
//        String sPassword = SharedPref.getString(LoginActivity.this, SharedPref.USER_PASSWORD);
//        String sDatabaseName = SharedPref.getString(LoginActivity.this, SharedPref.NAME_DATABASE);
//
//        if (!sDomain.equals("")
//                && !sUserName.equals("")
//                && !sPassword.equals("")) {
//            _startMainActivity(sDomain, sUserName, sPassword, company.getText().toString(), sDatabaseName);
//        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                boolean c = company.invalidateInput("");
                boolean b = username.invalidateInput("");
                boolean a = password.invalidateInput("");
                if (c && a && b)
                    Selectdatabase();
//                    _startMainActivity("taiphat_erp",Constants.HTTPS);
//                    login(company.getText().toString(),
//                            username.getText().toString(), password.getText().toString());
                break;
            case R.id.signin:
                _startSigninActivity();
                break;
        }
    }

    private void Selectdatabase() {
        HttpRequestClient.getInstance(LoginActivity_old.this).usingHttpClientgetDatabase(new HttpRequestClient.OnHttpRequestCallbackParams() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onSuccess(JSONObject response) {
                Debug.Log("response: " + response.toString());
                try {
                    JSONArray result = response.getJSONArray("result");
                    if (result.length() > 0) {
                        String[] dataBase = new String[result.length()];
                        for (int i = 0; i < result.length(); i++) {
                            dataBase[i] = result.get(i).toString();
//                            Debug.Log("dataBase: " + dataBase[i]);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity_old.this);
                                builder.setTitle("Chọn database");
                                builder.setItems(dataBase, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int position) {
                                        _startMainActivity(dataBase[position], Constants.HTTP);
                                    }
                                });
                                builder.show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Debug.Log("ex: " + e.toString());
                }
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onDatabaseIsNotExists() {

            }

            @Override
            public void onPasswordIncorrect() {

            }

            @Override
            public void onrequesthttps() {
//                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
////                String url = "https://khangminh.erpviet.vn/web/database/list";
//                String url = Utils.checkDomain1(company.getText().toString(), Constants.HTTPS) + HttpRequestClient.URL_GET_DB_ODOO11;
//                Debug.Log("url onrequeshttps: " + url);
//                Map<String, String> params = new HashMap<String, String>();
//                JSONObject jsonObj = new JSONObject(params);
//                JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Debug.Log("voley response: " + response.toString());
//                        try {
//                            JSONArray result = response.getJSONArray("result");
//                            if (result.length() > 0) {
//                                String[] dataBase = new String[result.length()];
//                                for (int i = 0; i < result.length(); i++) {
//                                    dataBase[i] = result.get(i).toString();
////                            Debug.Log("dataBase: " + dataBase[i]);
//                                }
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                                        builder.setTitle("Chọn database");
//                                        builder.setItems(dataBase, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int position) {
//                                                _startMainActivity(dataBase[position], Constants.HTTPS);
//                                            }
//                                        });
//                                        builder.show();
//                                    }
//                                });
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Debug.Log("ex: " + e.toString());
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Debug.Log("voley error: " + error.toString());
//                    }
//                })
//                        ;
//                queue.add(sr);
            }
        }, HttpRequestClient.URL_GET_DB_ODOO11, Constants.HTTP, company.getText().toString().replace(" ", ""));
//        Utils.checkDomain(LoginActivity.this, company.getText().toString(), Constants.HTTP).replace(" ", "")

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
                Utils.checkDomain(LoginActivity_old.this, company.getText().toString(), protocol).replace(" ", ""),
                username.getText().toString().replace(" ", ""),
                password.getText().toString().replace(" ", ""),
                company.getText().toString(),
                namedb);
    }

    private void _startMainActivity(final String domain, final String userName, final String password, final String company, String namedb) {
        loading();
        HttpRequestClient.getInstance(LoginActivity_old.this).usingHttpClient(new HttpRequestClient.OnHttpRequestCallback() {
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
                Intent intent = new Intent(LoginActivity_old.this, WebViewActivity.class);
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
        Intent intent = new Intent(LoginActivity_old.this, SignupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
        Utils.killApp(false);
    }

    private void _setDataLogin(String... data) {
        SharedPref.saveString(LoginActivity_old.this, SharedPref.DOMAIN, data[0]);
        SharedPref.saveString(LoginActivity_old.this, SharedPref.LOGIN, data[1]);
        SharedPref.saveString(LoginActivity_old.this, SharedPref.USER_PASSWORD, data[2]);
        SharedPref.saveString(LoginActivity_old.this, SharedPref.NAME_DATABASE, data[3]);

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
