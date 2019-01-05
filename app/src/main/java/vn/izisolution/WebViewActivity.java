package vn.izisolution;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import vn.izisolution.utils.Debug;
import vn.izisolution.utils.SharedPref;
import vn.izisolution.utils.ShowPopup;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String token, domain, userName, password, protocol;

    private Dialog loadingDialog;
    private ShowPopup showPopup;
    boolean isLoadWeb = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        domain = getIntent().getStringExtra("DOMAIN");
        userName = getIntent().getStringExtra("USERNAME");
        password = getIntent().getStringExtra("PASSWORD");
        protocol = getIntent().getStringExtra("PROTOCOL");

        showPopup = new ShowPopup(WebViewActivity.this);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setAppCacheEnabled(true); //
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Debug.Log("WebViewActivity loading url: " + url);
                if (url.equals(protocol+domain + "/web/session/logout")) {
                    Debug.Log("implement code logout 1");
                    SharedPref.saveString(WebViewActivity.this, SharedPref.DOMAIN, "");
                    SharedPref.saveString(WebViewActivity.this, SharedPref.LOGIN, "");
                    SharedPref.saveString(WebViewActivity.this, SharedPref.USER_PASSWORD, "");
                    SharedPref.saveString(WebViewActivity.this, SharedPref.NAME_DATABASE, "");
                    SharedPref.saveString(WebViewActivity.this, SharedPref.PROTOCOL, "");

                    Intent intent = new Intent(WebViewActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Debug.Log("implement code logout 2");
                    finish();
                    return true;
                } else {
                    Debug.Log("load url");
                    view.loadUrl(url);
                    return false;
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Debug.Log("WebViewActivity onPageFinished url: " + url);
                if (loadingDialog != null)
                    loadingDialog.dismiss();
//                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Debug.Log("WebViewActivity onPageStarted url: " + url);
                loading();
//                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                // this will ignore the Ssl error and will go forward to your site
                Debug.Log("SslErrorHandler : " + handler.toString());
                Debug.Log("SslError : " + error.toString());
                handler.proceed();
                error.getCertificate();

            }

        });
//        webView.getSettings().setDomStorageEnabled(true);

        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(true);
        settings.setBuiltInZoomControls(true);
//        settings.setBuiltInZoomControls(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        Debug.Log("protocol,domain -> " + protocol + "," + domain);
//        webView.loadUrl(domain); // + "/web#menu_id=1&action="
        webView.loadUrl(protocol + domain + "/web");
//        if (!isLoadWeb) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    isLoadWeb = true;
//                    webView.loadUrl(domain + "/web");
//                }
//            }, 1000);
//        }
    }

    private void loading() {
        if (loadingDialog == null)
            loadingDialog = showPopup.loading(false);
        loadingDialog.show();
    }

}
