package wapchief.com.collectiondemo.activity;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.framework.BaseActivity;

/**
 * Created by wapchief on 2018/1/10.
 */

public class MPChartActivity extends BaseActivity {

    @BindView(R.id.chartview)
    LineChart mChartview;
    @BindView(R.id.pie_chart)
    PieChart mPieChart;

    //饼数
    private int count = 2;
    //饼总长度
    private int range = 100;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);
        initViewLine();
        initViewPie();
    }

    /*圆形*/
    private void initViewPie() {

        //中间圆盘的半径%,用来决定饼边框的大小
        mPieChart.setHoleRadius(80);
        mPieChart.setDrawCenterText(true);
        String sum = 25 + "";
        String s= sum+"%";
        mPieChart.setCenterText(
                Html.fromHtml("<font color='#FF4055'><big><big><big><big><big>"+s+"</big></big></big></big></big></font><br><font color='#9b9b9b'>超过全站</font>"));
        //描述信息
        mPieChart.setDescription(null);
        setDataPie(2,100);
//        Highlight[] highlights = {new Highlight(0,, 0, 0),new Highlight(0, 0, 0)};
//        mPieChart.highlightValue(new Highlight(0, 0, 0));
//        mPieChart.needsHighlight(1);
//        LogUtils.e(mPieChart.getX()+"");
        mPieChart.highlightValue(new Highlight(1,0,4),false);

        //比例块
        Legend mLegend = mPieChart.getLegend();
        mLegend.setEnabled(false);
    }


    private void setDataPie(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        //添加数据
        entries.add(new PieEntry(100-25));
        entries.add(new PieEntry(25));

        PieDataSet dataSet = new PieDataSet(entries, "");


        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(5f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

            colors.add(getResources().getColor(R.color.colorPrimary));

            colors.add(getResources().getColor(R.color.red));


        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        //轮盘显示的%
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(0);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(mTfLight);
        mPieChart.setData(data);

        // undo all highlights
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }


    /*线性*/
    private void initViewLine() {
        //禁用触摸事件
        mChartview.setTouchEnabled(false);
        //禁用拖动事件
        mChartview.setDragEnabled(false);
        //禁用缩放事件
        mChartview.setScaleEnabled(false);
        //禁用双击缩放事件
        mChartview.setDoubleTapToZoomEnabled(false);
        //描述信息
        mChartview.setDescription(null);
        /*x轴*/
        XAxis xAxis = mChartview.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //无网格线
        xAxis.setDrawGridLines(false);
        /*y轴*/
        YAxis yAxis = mChartview.getAxisLeft();
        //无轴线
        yAxis.setDrawAxisLine(false);
        //从0开始
//        yAxis.setAxisMinimum(0f);

        //无右轴
        mChartview.getAxisRight().setEnabled(false);
        //图标数据源
        setData(7, 50);

        List<ILineDataSet> sets = mChartview.getData().getDataSets();
        for (ILineDataSet iSet : sets) {
            LineDataSet set = (LineDataSet) iSet;
            //设置带弧度
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            //设置数值不可见
            set.setDrawValues(false);
            //设置偏移点不可见
            set.setDrawCircles(false);
        }
        Legend le = mChartview.getLegend();
        le.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        mChartview.invalidate();
    }

    private void setData(int count, float range) {
        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            //随机数
            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.ic_message)));
        }

        LineDataSet set1;

        if (mChartview.getData() != null &&
                mChartview.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChartview.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChartview.getData().notifyDataChanged();
            mChartview.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "");

            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
//            set1.enableDashedLine(10f, 5f, 0f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.RED);
            set1.setCircleColor(Color.YELLOW);
            set1.setLineWidth(3f);
            //设置圆形值指示器的大小（半径
            set1.setCircleRadius(5f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
//            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(0f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChartview.setData(data);

        }
    }
}
