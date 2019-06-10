package com.demo.awesomeledger.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.util.DisplayMetrics;
import com.demo.awesomeledger.R;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.components.Description;

import java.util.ArrayList;

public class IncomeFragment extends Fragment{
    private PieChart IncomeChart;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        IncomeChart = view.findViewById(R.id.Pie);
        PieData IncomeData = getIncomePieData();
        initIncomePieChart(IncomeData);
        return view;
    }
    private void initIncomePieChart(PieData OutgoingData) {
        IncomeChart.setHoleRadius(60f);  //半径
        IncomeChart.setTransparentCircleRadius(25f); // 半透明圈
        IncomeChart.setHoleRadius(20);  //实心圆

        IncomeChart.setDrawCenterText(true);  //饼状图中间可以添加文字
        IncomeChart.setDrawHoleEnabled(true);
        IncomeChart.setRotationAngle(90); // 初始旋转角度
        IncomeChart.setUsePercentValues(true);//设置显示百分比
        IncomeChart.setRotationEnabled(false); // 可以手动旋转
        IncomeChart.setUsePercentValues(true);  //显示成百分比
        IncomeChart.setCenterText("收入");  //饼状图中间的文字
        //设置右下角描述
        Description description = new Description();
        description.setText("收入数据");
        description.setTextSize(16);
        description.setPosition(250,100);
        IncomeChart.setDescription(description);
        //设置数据
        IncomeChart.setData(OutgoingData);
        //设置下方比例图信息
        Legend mLegend = IncomeChart.getLegend();  //设置比例图
        mLegend.setForm(Legend.LegendForm.SQUARE);  //设置比例图的形状，默认是方形 SQUARE
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(7f);
        mLegend.setWordWrapEnabled(true);
        mLegend.setFormSize(22f);
        IncomeChart.animateXY(1000, 1000);  //设置动画
        IncomeChart.invalidate(); // 重新更新显示
    }
    private PieData getIncomePieData() {
        int[] yy = {6, 6, 6, 6, 9, 9, 10, 10, 14, 14, 5, 5};
        /**
         * 将一个饼形图分成六十二部分， 各个部分的数值比例如上
         * 所以 6代表的百分比就是6%
         * TODO:在具体的实现过程中，这里应获取实际数据
         */
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();  //yVals用来表示封装每个饼块的实际数据
        for (int i = 0; i < 12; i++) {
            yValues.add(new PieEntry(yy[i], "类别"));
        }

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues,"");
        pieDataSet.setSliceSpace(5f); //设置个饼状图之间的距离
        pieDataSet.setSelectionShift(5f);
        // 饼图颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.rgb(255, 48, 48));
        colors.add(Color.rgb(255, 20, 147));
        colors.add(Color.rgb(0, 0, 255));
        colors.add(Color.rgb(139, 102, 139));
        colors.add(Color.rgb(99, 184, 255));
        colors.add(Color.rgb(224, 102, 255));
        colors.add(Color.rgb(0, 206, 209));
        colors.add(Color.rgb(0, 100, 0));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(0, 255, 127));
        colors.add(Color.rgb(238, 220, 130));
        colors.add(Color.rgb(139, 134, 78));
        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度
        PieData piedata = new PieData(pieDataSet);
        piedata.setValueTextSize(12f);
        ArrayList<Integer> Text_colors = new ArrayList<Integer>();
        Text_colors.add(Color.rgb(255, 255,255));
        Text_colors.add(Color.rgb(255, 255,255));
        Text_colors.add(Color.rgb(255, 255,255));
        Text_colors.add(Color.rgb(255, 255,255));
        Text_colors.add(Color.rgb(255, 255,255));
        Text_colors.add(Color.rgb(255, 255,255));
        Text_colors.add(Color.rgb(255, 255,255));
        Text_colors.add(Color.rgb(255, 255,255));
        Text_colors.add(Color.rgb(255, 255,255));
        Text_colors.add(Color.rgb(255, 255,255));
        Text_colors.add(Color.rgb(255, 255,255));
        Text_colors.add(Color.rgb(255, 255,255));
        piedata.setValueTextColors(Text_colors);
        return piedata;
    }
}
