package vn.izisolution;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import vn.izisolution.adapter.AppSpinnerAdapter;
import vn.izisolution.constant.Constants;
import vn.izisolution.constant.LoadJson;
import vn.izisolution.model.App;
import vn.izisolution.utils.Debug;
import vn.izisolution.utils.HttpRequestClient;
import vn.izisolution.utils.ResponseCode;
import vn.izisolution.utils.SharedPref;
import vn.izisolution.utils.ShowPopup;
import vn.izisolution.utils.Utils;
import vn.izisolution.views.FontTextView;
import vn.izisolution.views.ValidateEditText;

/**
 * Created by ToanNMDev on 3/6/2017.
 */

public class SignupActivity extends Activity implements View.OnClickListener {

    private LinearLayout mainLayout;
    private ValidateEditText inputCompany, inputEmail, inputPhone, inputDomain; //inputScale, inputField,
    private AppCompatSpinner spinnerField, spinnerScale;

    private AppSpinnerAdapter fieldAdapter, scaleAdapter;

    private AppCompatCheckBox checkBoxPocily;

    private FontTextView signup;
    private Dialog loadingDialog;

    private App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        spinnerField = (AppCompatSpinner) findViewById(R.id.spinner_field);
        spinnerScale = (AppCompatSpinner) findViewById(R.id.spinner_scale);

        inputCompany = (ValidateEditText) findViewById(R.id.input_company);
        inputEmail = (ValidateEditText) findViewById(R.id.input_email);
        inputPhone = (ValidateEditText) findViewById(R.id.input_phone);
        inputDomain = (ValidateEditText) findViewById(R.id.input_domain);

//        inputDomain.setText("toannm");
//        inputCompany.setText("ToanNM");
//        inputPhone.setText("0985942382");
//        inputEmail.setText("toannm.jsc@gmail.com");

        checkBoxPocily = (AppCompatCheckBox) findViewById(R.id.checkbox_policy);
        signup = (FontTextView) findViewById(R.id.signup);
        signup.setOnClickListener(this);

        Utils.setupUI(mainLayout, SignupActivity.this);

        loadingDialog = new ShowPopup(SignupActivity.this).loading(false);
        app = new App();
        app.getDefaultData(SignupActivity.this, new App.OnLoadDefaultDataListener() {
            @Override
            public <T extends App> void onSuccess(ArrayList<T> cpSizes, ArrayList<T> apps) {
                loadingDialog.dismiss();

                String[] fields = new String[apps.size() + 1];
                fields[0] = getResources().getString(R.string.signup_hint_field);
                for (App app : apps) {
                    fields[apps.indexOf(app) + 1] = app.name;
                }
                fieldAdapter = new AppSpinnerAdapter(SignupActivity.this, fields, cpSizes);

                String[] scales = new String[cpSizes.size() + 1];
                scales[0] = getResources().getString(R.string.signup_hint_scale);
                for (App app : cpSizes) {
                    scales[cpSizes.indexOf(app) + 1] = app.name;
                }
                scaleAdapter = new AppSpinnerAdapter(SignupActivity.this, scales, apps);

                spinnerField.setAdapter(fieldAdapter);
                spinnerScale.setAdapter(scaleAdapter);

                spinnerField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                            fieldAdapter.setEnabled(false);
                        } else
                            fieldAdapter.setEnabled(true);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                spinnerScale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                            scaleAdapter.setEnabled(false);
                        } else
                            scaleAdapter.setEnabled(true);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onLoadJsonLoading() {
                loadingDialog.show();
            }

            @Override
            public void onLoadJsonSuccess(JSONObject response) {
                loadingDialog.dismiss();
            }

            @Override
            public void onLoadJsonError(String error, boolean needToShowErrorDialog) {
                loadingDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup:
                boolean a = inputDomain.invalidateInput("");
                boolean b = inputPhone.invalidateInput("");
                boolean c = inputEmail.invalidateInput("");
                boolean d = inputCompany.invalidateInput("");
                boolean e = spinnerField.getSelectedItemPosition() != 0;
                boolean f = spinnerScale.getSelectedItemPosition() != 0;

                if (spinnerField.getSelectedItemPosition() == 0) {
//                    spinnerField.requestFocus();
                    spinnerField.performClick();
                }
                if (e && spinnerScale.getSelectedItemPosition() == 0) {
//                    spinnerScale.requestFocus();
                    spinnerScale.performClick();
                }
                if (!checkBoxPocily.isChecked())
                    checkBoxPocily.setError("");
                else
                    checkBoxPocily.setError(null);

                if (a && b && c && d && e && f && checkBoxPocily.isChecked())
                    signup();
                break;
        }
    }

    private void signup() {
        if (loadingDialog == null)
            loadingDialog = new ShowPopup(SignupActivity.this).loading(false);
        loadingDialog.show();

        final String url = "http://ad5d9188-54cf-11e7-main.erpviet.vn/saas_portal/izi_create_new_database";
//        final String url = "http://main-test.odooviet.vn/saas_portal/izi_create_new_database";

        RequestParams params = new RequestParams();
        params.put("access_token", "LDT1Aose0cBALq9Ra8LkrEbNhw6Ek90BKO0WxTtCdHWPsJu0AHUVHurhf6ZCU0SE"); //LDT1Aose0cu0AHUVHurhf6ZCU0SE
        params.put("brand_name", inputDomain.getText().toString());
        params.put("db_name", inputDomain.getText().toString());
        params.put("partner_name", inputCompany.getText().toString());
        params.put("partner_phone", inputPhone.getText().toString());
        params.put("partner_mail", inputEmail.getText().toString());
        params.put("plan_code", "PLAN_CODE");//1
        params.put("scale", String.valueOf(fieldAdapter.getApps().get(spinnerField.getSelectedItemPosition() - 1).name));
        params.put("plan_id", String.valueOf(scaleAdapter.getApps().get(spinnerScale.getSelectedItemPosition() - 1).id));

//        JSONObject mainObject = new JSONObject();
//        try {
//            mainObject.put("access_token", "LDT1Aose0cBALq9Ra8LkrEbNhw6Ek90BKO0WxTtCdHWPsJu0AHUVHurhf6ZCU0SE"); //LDT1Aose0cu0AHUVHurhf6ZCU0SE
//            mainObject.put("brand_name", inputDomain.getText().toString());
//            mainObject.put("db_name", inputDomain.getText().toString());
//            mainObject.put("partner_name", inputCompany.getText().toString());
//            mainObject.put("partner_phone", inputPhone.getText().toString());
//            mainObject.put("partner_mail", inputEmail.getText().toString());
//            mainObject.put("plan_code", "PLAN_CODE");//1
//            mainObject.put("scale", String.valueOf(fieldAdapter.getApps().get(spinnerField.getSelectedItemPosition() - 1).name));
//            mainObject.put("plan_id", String.valueOf(scaleAdapter.getApps().get(spinnerScale.getSelectedItemPosition() - 1).id));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
        Debug.Log("signup params -> " + params.toString());

        LoadJson loadJson = new LoadJson(SignupActivity.this, url,
                    params,
//                new StringEntity(mainObject.toString(), ContentType.APPLICATION_JSON),
                new LoadJson.OnLoadJson() {
                    @Override
                    public void onLoadJsonLoading() {
                    }

                    @Override
                    public void onLoadJsonSuccess(JSONObject response) {
                        Debug.Log("signup response -> " + response);
                        loadingDialog.dismiss();

                        try {
//                            int code = response.getJSONObject("result").getInt("code");

                            int code = response.getInt("code");
                            switch (code) {
                                case ResponseCode.CODE_309:
                                    showError(ResponseCode.MESSAGE_CODE_309 + "\n" + inputDomain.getText().toString());
                                    break;
                                case ResponseCode.CODE_402:
                                    showError(ResponseCode.MESSAGE_CODE_402 + "\n" + inputDomain.getText().toString());
                                    break;
                                case ResponseCode.CODE_400:
                                    showError(ResponseCode.MESSAGE_CODE_400);
                                    break;
                                case ResponseCode.CODE_401:
                                    showError(ResponseCode.MESSAGE_CODE_401);
                                    break;
                                case ResponseCode.CODE_500:
                                    showError(ResponseCode.MESSAGE_CODE_500);
                                    break;
                                case ResponseCode.CODE_273:
                                    showError(ResponseCode.MESSAGE_CODE_500);
                                    break;
                                case ResponseCode.CODE_OK:
                                    _startMainActivity(Utils.checkDomain(SignupActivity.this, inputDomain.getText().toString(),Constants.HTTP),
                                            "admin",
                                            "erpviet123",
                                            inputDomain.getText().toString());
//                                    new ShowPopup(SignupActivity.this).info("Đăng ký thành công, tài khoản và mật khẩu sẽ được gửi tới email của bạn trong giây lát!",
//                                            new ShowPopup.OnPopupActionListener() {
//                                                @Override
//                                                public void onCancel() {
//
//                                                }
//
//                                                @Override
//                                                public void onAccept() {
////                                                    new ShowPopup(SignupActivity.this).OTP().show();
//                                                }
//                                            }).show();
                                    break;
                                default:
                                    showError(ResponseCode.MESSAGE_CODE_500);
                                    break;
                            }

                        } catch (JSONException e) {
                            showError(ResponseCode.MESSAGE_CODE_500);
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onLoadJsonError(String error, boolean needToShowErrorDialog) {
                        showError(ResponseCode.MESSAGE_CODE_500);
                        loadingDialog.dismiss();

                    }
                });
        loadJson.loadData();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startMainActivity();
//            }
//        }, 1000);

    }

    private void loading() {
        if (loadingDialog == null)
            loadingDialog = new ShowPopup(SignupActivity.this).loading(false);
        loadingDialog.show();
    }

    private void _setDataLogin(String... data) {
        SharedPref.saveString(SignupActivity.this, SharedPref.DOMAIN, data[0]);
        SharedPref.saveString(SignupActivity.this, SharedPref.LOGIN, data[1]);
        SharedPref.saveString(SignupActivity.this, SharedPref.USER_PASSWORD, data[2]);

//        SharedPref.saveString(SignupActivity.this, SharedPref.HAS_PERMISSION_CRM, data[0]);
//        SharedPref.saveString(SignupActivity.this, SharedPref.HAS_PERMISSION_ACCOUNT, data[1]);
//        SharedPref.saveString(SignupActivity.this, SharedPref.HAS_PERMISSION_STOCK, data[2]);
//        SharedPref.saveString(SignupActivity.this, SharedPref.HAS_PERMISSION_HR, data[3]);
//        Debug.Log("setDataLogin -> data -> " + data);
    }

    private void _startMainActivity(final String domain, final String userName, final String password, final String company) {
        loading();
        Debug.Log("domain -> " + domain + ", userName -> " + userName + ", password -> " + password);
        HttpRequestClient.getInstance(SignupActivity.this).usingHttpClient(new HttpRequestClient.OnHttpRequestCallback() {
            @Override
            public void onLoading() {
            }

            @Override
            public void onSuccess() {
                _setDataLogin(company, userName, password);

                Handler mainHandler = new Handler(Looper.getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null)
                            loadingDialog.dismiss();
                    }
                };
                mainHandler.post(myRunnable);
                Intent intent = new Intent(SignupActivity.this, WebViewActivity.class);
                intent.putExtra("DOMAIN", Utils.checkDomain(SignupActivity.this, domain,Constants.HTTP));
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
                showErrorWithRunnable("Database không đúng, vui lòng thử lại.");
            }

            @Override
            public void onPasswordIncorrect() {
                showErrorWithRunnable("Tài khoản hoặc mật khẩu chưa chính xác.");
            }
        }, domain, userName, password);

    }

    private void showError(String msg) {
        new ShowPopup(SignupActivity.this)
                .info(msg)
                .show();
    }

    private void showErrorWithRunnable(String msg) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null)
                    loadingDialog.dismiss();
                new ShowPopup(SignupActivity.this).info(msg).show();
            }
        };
        mainHandler.post(myRunnable);
    }

    private void startMainActivity() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private <T extends LoadJson> void makeLoadJson(T loadJson, Context context) {

    }

    private <T extends LoadJson> T getLoadJson(T loadJson) {
        return loadJson;
    }

    private class LoadJson_Extend extends LoadJson {

//        public LoadJson_Extend(Context context) {
////            super(context, "", null, null);
//        }

        public LoadJson_Extend(Context context, String url, StringEntity entity, OnLoadJson listener) {
            super(context, url, entity, listener);
        }
    }


}
