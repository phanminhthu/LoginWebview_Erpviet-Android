//package vn.izisolution.constant;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import vn.izisolution.R;
//import vn.izisolution.utils.Debug;
//import vn.izisolution.utils.ShowPopup;
//import vn.izisolution.utils.Utils;
//import vn.izisolution.xmlrpc.axmlrpc.XMLRPCCallback;
//import vn.izisolution.xmlrpc.axmlrpc.XMLRPCClient;
//import vn.izisolution.xmlrpc.axmlrpc.XMLRPCException;
//import vn.izisolution.xmlrpc.axmlrpc.XMLRPCServerException;
//import vn.izisolution.xmlrpc.axmlrpc.XMLRPCTimeoutException;
//
///**
// * Created by ToanNMDev on 3/26/2018.
// */
//
//public class XmlRpcClient { // no need to extend XMLRPCClient
//
//    private static String MESSAGE_TIME_OUT = "Kết nối quá thời hạn, mất quá nhiều thời gian để nhận phản hồi\n Xin vui lòng thử lại sau";
//
//    private Context context;
//    private XMLRPCClient client;
//    private OnLoad listener;
//    private String url;
//
//    private long callId = -1; // id for cancelling a httpConnection
//
//    private ShowPopup.OnPopupActionListener noInternerListener;
//
//    private boolean needToShowErrorDialog = true;
//
//    public boolean showTimeOutDialog = true;
//    long loadingTime = 0;
//
//    public interface OnLoad {
//        void onLoading();
//
//        void onSuccess(Object response);
//
//        void onError(String error, boolean needToShowErrorDialog);
//
//        void onServerError(String error, boolean needToShowErrorDialog);
//    }
//
//    /**
//     * Create a new Caller for asynchronous use.
//     *
//     * @param context
//     * @param url
//     * @param listener
//     */
//    public XmlRpcClient(@NonNull Context context, @NonNull String url,
//                        OnLoad listener) {
//        this.context = context;
//        this.url = url;
//        this.listener = listener;
//
//        _initMessages();
//        try {
//            client = new XMLRPCClient(new URL(url));
//            client.setTimeout(Constants.TIME_OUT);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            Debug.Log("client init error -> " + e.getMessage());
//        }
//    }
//
//    private void _initMessages() {
//        MESSAGE_TIME_OUT = context.getResources().getString(R.string.message_time_out);
//    }
//
//    public void setOnNoInternetListener(ShowPopup.OnPopupActionListener noInternerListener) {
//        this.noInternerListener = noInternerListener;
//    }
//
//    /**
//     * @param methodName The method name to call
//     * @param args       String params for sending to sever.
//     **/
//    public void callAsync(String methodName, String... args) {
//        if (callId != -1)
//            client.cancel(callId);
//
//        loadingTime = System.currentTimeMillis();
//        if (!Utils.isNetworkConnected(context)) {
//            if (Constants.noInternetDialog != null)
//                Constants.noInternetDialog.dismiss();
//
//            new ShowPopup(context).info("Không có kết nối internet,\n Xin vui lòng thử lại",
//                    noInternerListener).show();
//            if (listener != null)
//                listener.onError("", false);
//        } else {
//            if (listener != null)
//                listener.onLoading();
//
//            if (client != null) {
//                needToShowErrorDialog = true;
//                callId = client.callAsync(new XMLRPCCallback() {
//                    @Override
//                    public void onResponse(long id, Object result) {
//                        if (!Constants.checkDuplicateAccessToken(null, context))
//                            if (listener != null)
//                                listener.onSuccess(result);
//                        loadingTime = System.currentTimeMillis() - loadingTime;
//                    }
//
//                    @Override
//                    public void onError(long id, XMLRPCException error) {
//                        loadingTime = System.currentTimeMillis() - loadingTime;
//                        if (error instanceof XMLRPCTimeoutException) {
//                            if (showTimeOutDialog)
//                                new ShowPopup(context).info(MESSAGE_TIME_OUT).show();
//                            needToShowErrorDialog = false;
//                        } else if (listener != null)
//                            listener.onError(error.getMessage(), needToShowErrorDialog);
//                    }
//
//                    @Override
//                    public void onServerError(long id, XMLRPCServerException error) {
//                        loadingTime = System.currentTimeMillis() - loadingTime;
//                        listener.onServerError(error.getMessage(), needToShowErrorDialog);
//                    }
//                }, methodName, args);
//            } else {
//                new ShowPopup(context).info(context.getString(R.string.contact_admin)).show();
//            }
//        }
//    }
//}
