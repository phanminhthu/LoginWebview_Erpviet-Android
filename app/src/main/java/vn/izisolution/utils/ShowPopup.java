package vn.izisolution.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IntRange;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import vn.izisolution.R;
import vn.izisolution.SignupActivity;
import vn.izisolution.constant.Constants;
import vn.izisolution.constant.LoadJson;
//import vn.izisolution.views.codeinput.CodeInput;

import com.dpizarro.pinview.library.PinView;
import com.dpizarro.pinview.library.PinViewSettings;
import com.mukesh.OtpView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ToanNMDev on 3/13/2017.
 */

public class ShowPopup {

    private static final int maxTryTime = 3;

    public Context context;

    @IntRange(from = 0, to = maxTryTime)
    public int tryTimes = 0;

    public Dialog popupDialog;
    public int isAlert = 0;
    private LoadJson loadJson;

    public ShowPopup(Context context) {
        this.context = context;
        popupDialog = new Dialog(context);
    }

    /* interface */
    public interface OnPopupActionListener {
        public void onCancel();

        public void onAccept();
    }

    public Dialog info(String info, final OnPopupActionListener listener) {
        return info(info, "", listener);
    }

    public Dialog loading(boolean cancelable) {
        int w = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.7f);
        _initPopupDialog(cancelable, R.layout.dialog_loading, w);

        return popupDialog;
    }

    public Dialog info(String info) {
        return info(info, "", null);
    }

    public Dialog info(String info, String title) {
        return info(info, title, null);
    }

    public Dialog info(String info, String txtTitle, final OnPopupActionListener listener) {
        int w = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.7f);
        _initPopupDialog(false, R.layout.dialog_info, w);
        Constants.noInternetDialog = popupDialog;
        if (isAlert == 1)
            popupDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        TextView title = (TextView) popupDialog.findViewById(R.id.title);
        title.setText(txtTitle);

        title.setVisibility(txtTitle != null && !txtTitle.equals("") ? View.VISIBLE : View.GONE);

        TextView content = (TextView) popupDialog.findViewById(R.id.content);
        content.setText(info);

        TextView close = (TextView) popupDialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onAccept();
                popupDialog.dismiss();
            }
        });

        return popupDialog;
    }

    public Dialog OTP() {
        int w = (int) (context.getResources().getDisplayMetrics().widthPixels * 1f);
        int h = (int) (context.getResources().getDisplayMetrics().heightPixels * 1f);
        _initPopupDialog(false, R.layout.dialog_pin_number, w, h);

        PinView pinView = (PinView) popupDialog.findViewById(R.id.pinView);

        PinViewSettings pinViewSettings = new PinViewSettings.Builder()
                .withPinTitles(null)
                .withMaskPassword(false)
                .withDeleteOnClick(true)
                .withKeyboardMandatory(false)
                .withSplit("-")
                .withNumberPinBoxes(6)
                .withNativePinBox(false)
                .build();

        pinView.setSettings(pinViewSettings);

        pinView.setOnCompleteListener(new PinView.OnCompleteListener() {
            @Override
            public void onComplete(boolean completed, final String pinResults) {
                //Do what you want
                if (completed) {
                    final Dialog loadingDialog = new ShowPopup(context).loading(false);
                    loadingDialog.show();

                    JSONObject mainObject = new JSONObject();
                    try {
                        mainObject.put("access_token", "LDT1Aose0cu0AHUVHurhf6ZCU0SE");
                        mainObject.put("otp", pinResults);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Debug.Log("requestOTP params -> " + mainObject.toString());

                    LoadJson loadJson = new LoadJson(context, "http://main-test.odooviet.vn/izi_saas/validate_request",
                            new StringEntity(mainObject.toString(), ContentType.APPLICATION_JSON),
                            new LoadJson.OnLoadJson() {
                                @Override
                                public void onLoadJsonLoading() {

                                }

                                @Override
                                public void onLoadJsonSuccess(JSONObject response) {
                                    popupDialog.dismiss();
                                    loadingDialog.dismiss();

                                    new ShowPopup(context).info("Cảm ơn bạn đã đăng ký sử dụng ứng dụng ErpViet\n \n" +
                                            "Chúng tôi sẽ liên hệ với bạn trong thời gian sớm nhất", new OnPopupActionListener() {
                                        @Override
                                        public void onCancel() {

                                        }

                                        @Override
                                        public void onAccept() {
                                            SignupActivity activity = (SignupActivity) context;
                                            if (activity != null)
                                                activity.finish();
                                        }
                                    }).show();

                                }

                                @Override
                                public void onLoadJsonError(String error, boolean needToShowErrorDialog) {
                                    popupDialog.dismiss();
                                    loadingDialog.dismiss();
                                }
                            });
                }
            }
        });


//        OtpView otpView = (OtpView) popupDialog.findViewById(R.id.otpView);
//        otpView.setTextColor(R.color.colorAccent)
//                .setHintColor(R.color.colorAccent)
//                .setCount(7)
//                .setInputType(InputType.TYPE_CLASS_NUMBER)
//                .setViewsPadding(16)
//                .setListener(new OTPListener() {
//                    @Override
//                    public void otpFinished(String otp) {
//
//                    }
//                })
//                .fillLayout();

//        CodeInput cInput = (CodeInput) popupDialog.findViewById(R.id.pairing);
//        cInput.setCodeReadyListener(new CodeInput.codeReadyListener() {
//            @Override
//            public void onCodeReady(Character[] code) {
//                // Code has been entered ....
//
//            }
//        });

        return popupDialog;

    }

    private void _initPopupDialog(boolean isCancelable, int resID) {
        _initPopupDialog(isCancelable, resID, 0, 0);
    }

    private void _initPopupDialog(boolean isCancelable, int resID, int w) {
        _initPopupDialog(isCancelable, resID, w, 0);
    }

    private void _initPopupDialog(boolean isCancelable, int resID, int w, int h) {
        if (popupDialog != null)
            popupDialog.dismiss();
        popupDialog = new Dialog(context);
        popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupDialog.setContentView(resID);
        popupDialog.setCancelable(isCancelable);
        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupDialog.getWindow().getAttributes());
        if (w != 0)
            lp.width = w;
        if (h != 0)
            lp.height = h;
        lp.gravity = Gravity.CENTER;
        popupDialog.getWindow().setAttributes(lp);

    }

}
