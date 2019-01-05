package vn.izisolution.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import vn.izisolution.constant.LoadJson;
import vn.izisolution.utils.Debug;
import vn.izisolution.utils.Utils;

/**
 * Created by ToanNMDev on 5/2/2017.
 */

public class App implements Serializable {

//    private static final String API_GET_DEFAULT_DATA = "http://main.erpviet.vn/izi_saas/get_default_data";
    private static final String API_GET_DEFAULT_DATA = "http://main.erpviet.vn/izi_saas/get_default_data";

    public int id = -1;
    public String name = "", code = "", rank = "";
    public boolean isSelected = false;

    // account_period
    public String startDate, stopDate;

    public App() {
    }

    public App(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public App(int id, String name, String code, String rank) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.rank = rank;
    }

    public App(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * for getting default data from @ API_GET_DEFAULT_DATA
     */
    public interface OnLoadDefaultDataListener extends LoadJson.OnLoadJson {
        public <T extends App> void onSuccess(ArrayList<T> cpSizes, ArrayList<T> apps);
    }

    public void getDefaultData(Context context, final OnLoadDefaultDataListener onLoadDefaultDataListener) {
        JSONObject mainObject = new JSONObject();
        try {
            mainObject.put("access_token", "LDT1KO0WxTtCdHWP1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity entity = new StringEntity(mainObject.toString(), ContentType.APPLICATION_JSON);

        LoadJson loadJson = new LoadJson(context, API_GET_DEFAULT_DATA, entity, new LoadJson.OnLoadJson() {
            @Override
            public void onLoadJsonLoading() {
                if (onLoadDefaultDataListener != null)
                    onLoadDefaultDataListener.onLoadJsonLoading();
            }

            @Override
            public void onLoadJsonSuccess(JSONObject response) {
                Debug.Log("appppp response -> " + response);
                ArrayList<App> cpSize = new ArrayList<App>();
                ArrayList<App> apps = new ArrayList<App>();

                try {
                    JSONObject d = response.getJSONObject("result").getJSONObject("data");
                    JSONObject cp = d.getJSONObject("cp_size");

                    if (cp.length() > 0) {
                        Iterator<String> keys = cp.keys();
                        while (keys.hasNext()) {
                            String keyValue = keys.next();
                            String valueString = cp.getString(keyValue);
                            cpSize.add(new App(0, valueString, keyValue));
                        }
                    }

//                    Collections.sort(cpSize);
                    // Sorting
                    Collections.sort(cpSize, new Comparator<App>() {
                        @Override
                        public int compare(App fruit2, App fruit1) {
                            return fruit2.code.compareTo(fruit1.code);
                        }
                    });

                    JSONArray app = d.getJSONArray("app");

                    if (app.length() > 0) {
                        for (int i = 0; i < app.length(); i++) {
                            JSONObject a = app.getJSONObject(i);
                            apps.add(new App(Utils.checkIntEqualFalse(a.getString("id")),
                                    a.getString("name"), a.getString("name")));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (onLoadDefaultDataListener != null)
                    onLoadDefaultDataListener.onSuccess(cpSize, apps);

            }

            @Override
            public void onLoadJsonError(String error, boolean needToShowErrorDialog) {
                if (onLoadDefaultDataListener != null)
                    onLoadDefaultDataListener.onLoadJsonError(error, needToShowErrorDialog);

            }
        });
        loadJson.loadData();
    }

}
