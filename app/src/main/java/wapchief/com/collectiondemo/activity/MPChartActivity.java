package wapchief.com.collectiondemo.activity;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
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
        setData(7,50);

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

    private void setData(int count,float range) {
        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {

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
