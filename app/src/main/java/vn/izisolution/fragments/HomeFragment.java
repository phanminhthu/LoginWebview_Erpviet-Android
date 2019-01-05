package vn.izisolution.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener;
import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.util.ArrayList;
import java.util.List;

import vn.izisolution.R;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;

    private static final String[] xAxisArr = {"", "", "", "", "", ""};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        testAnimatePie(root);

        _initBarChart(root);

        return root;
    }

    private class MyObject extends Object {

    }

    public class Test implements IPieInfo {

        @Override
        public double getValue() {
            return 0.5;
        }

        @Override
        public int getColor() {
            return Color.WHITE;
        }

        @Override
        public String getDesc() {
            return "Chi tiết";
        }
    }

    private void testAnimatePie(View root) {
        AnimatedPieView mAnimatedPieView = (AnimatedPieView) root.findViewById(R.id.animatedPieView);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90)
                .addData(new SimplePieInfo(17, Color.parseColor("#F44336"), "HMS01"))
                .addData(new SimplePieInfo(10.2, Color.parseColor("#9C27B0"), "HMS02"))
                .addData(new SimplePieInfo(12.8, Color.parseColor("#3F51B5"), "HMS03"))
                .addData(new SimplePieInfo(20.5, Color.parseColor("#009688"), "HMS04"))
                .addData(new SimplePieInfo(6.8, Color.parseColor("#FFEB3B"), "HMS05"))
                .addData(new SimplePieInfo(3.6, Color.parseColor("#795548"), "HMS06"))
                .addData(new SimplePieInfo(31.1, Color.parseColor("#607D8B"), "Khác"))
                .duration(2000);

        config.animOnTouch(true)
//                .addData(IPieInfo info, boolean autoDesc)
                .floatExpandAngle(15f)
                .floatShadowRadius(18f)
                .floatUpDuration(500)
                .floatDownDuration(500)
                .floatExpandSize(45)
                .strokeMode(true)
                .strokeWidth(35)
                .duration(2500)
                .startAngle(-90f)
                .selectListener(new OnPieSelectListener<IPieInfo>() {
                    @Override
                    public void onSelectPie(@NonNull IPieInfo pieInfo, boolean isFloatUp) {
                        if (isFloatUp)
                            Toast.makeText(getActivity(), pieInfo.getDesc() + " doanh thu " + pieInfo.getValue(), Toast.LENGTH_LONG).show();
                    }
                })
                .drawText(true)
                .textSize(26)
                .textMargin(8)
                .pieRadiusRatio(0.8f)
                .guidePointRadius(2)
                .guideLineWidth(4)
                .guideLineMarginStart(8)
                .textGravity(AnimatedPieViewConfig.ABOVE)
                .canTouch(true)
                .splitAngle(0)
                .focusAlphaType(AnimatedPieViewConfig.FOCUS_WITH_ALPHA_REV)
                .interpolator(new DecelerateInterpolator())
                .focusAlpha(150);

        mAnimatedPieView.applyConfig(config);
        mAnimatedPieView.start();
    }

    private void set(String... arg) {

    }

    private <T> void genericAdd(T value, List<T> list) {
        Log.e("ToanNM", "genericAdd -> " + value + ", " + list);
    }

    public interface OnActionListener<T extends Object> extends OnActionListener_Extend {
        abstract void onActionListener();
    }

    public interface OnActionListener_Extend<T extends Object> {
        abstract void onActionListenerExtend();
    }

    public <T extends Object> void setOnActionListener(OnActionListener<T> listener) {

    }

    public interface OnDialogAction {
        void onAccept();

        void onDismiss();
    }

    private void _initBarChart(View root) {
        BarChart chart = (BarChart) root.findViewById(R.id.chart);
//        getXAxisValues(),
        BarData data = new BarData(getDataSet());
        chart.setData(data);
        Description description = new Description();
        description.setText("Doanh thu theo Brand");
        chart.setDescription(description);
        chart.animateXY(2000, 2000);

        // new customer
        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(true);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        chart.setDrawValueAboveBar(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxisArr[(int) value];
            }
        });

        chart.getAxisLeft().setDrawGridLines(false);

        chart.getLegend().setEnabled(true);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        chart.setFitBars(true);

        chart.invalidate();
    }

    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(1, 110.000f); // Jan
        valueSet1.add(v1e1);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(2, 150.000f); // Jan
        valueSet2.add(v2e1);

        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        BarEntry v3e1 = new BarEntry(3, 90.000f); // Feb
        valueSet3.add(v3e1);

        ArrayList<BarEntry> valueSet4 = new ArrayList<>();
        BarEntry v4e1 = new BarEntry(4, 120.000f); // Mar
        valueSet4.add(v4e1);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "HMS01");
        barDataSet1.setColor(Color.rgb(155, 155, 155));
//        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "HMS02");
        barDataSet2.setColor(Color.rgb(0, 155, 0));
//        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "HMS03");
        barDataSet3.setColor(Color.rgb(0, 0, 155));
//        barDataSet3.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet4 = new BarDataSet(valueSet4, "HMS04");
        barDataSet4.setColor(Color.rgb(155, 0, 0));
//        barDataSet4.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);
        dataSets.add(barDataSet4);

        return dataSets;
    }

}
