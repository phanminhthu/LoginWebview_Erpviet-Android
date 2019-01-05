package vn.izisolution.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.izisolution.R;
import vn.izisolution.model.App;

/**
 * Created by ToanNMDev on 3/21/2017.
 */

public class AppSpinnerAdapter extends BaseAdapter {

    public static final int LAYOUT_TYPE_NORMAL = 0;
    public static final int LAYOUT_TYPE_GRAVITY_RIGHT = 1;
    public static final int LAYOUT_TYPE_GRAVITY_CENTER = 2;

    private Context context;
    private String[] data;
    private boolean isEnabled = true;
    private int layoutType = LAYOUT_TYPE_NORMAL;

    private ArrayList<? extends App> apps;

    private int textColor;

    public AppSpinnerAdapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
        textColor = ContextCompat.getColor(context, R.color.color_text);
    }

    public AppSpinnerAdapter(Context context, String[] data, ArrayList<? extends App> apps) {
        this.context = context;
        this.data = data;
        this.apps = apps;
        textColor = ContextCompat.getColor(context, R.color.color_text);
    }

    public void setTextColor(int textColor){
        this.textColor = textColor;
    }

    public ArrayList<? extends App> getApps(){
        return apps;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public void notifyDataSetChanged(String[] data) {
        this.data = data;
        super.notifyDataSetChanged();
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner, parent, false);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        if (layoutType == LAYOUT_TYPE_NORMAL)
            title.setGravity(Gravity.LEFT);
        else if (layoutType == LAYOUT_TYPE_GRAVITY_RIGHT)
            title.setGravity(Gravity.RIGHT);
        else if (layoutType == LAYOUT_TYPE_GRAVITY_CENTER)
            title.setGravity(Gravity.CENTER);

        title.setText(data[position]);
        title.setTextColor(isEnabled
                ? textColor
                : ContextCompat.getColor(context, R.color.color_text_hint));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_dropdown, parent, false);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(data[position]);

        return convertView;
    }
}
