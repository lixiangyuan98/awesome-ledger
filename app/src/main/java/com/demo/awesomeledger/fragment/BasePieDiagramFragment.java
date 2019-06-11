package com.demo.awesomeledger.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.demo.awesomeledger.R;
import com.demo.awesomeledger.bean.Item;
import com.demo.awesomeledger.dao.ItemDao;
import com.demo.awesomeledger.type.ItemKind;
import com.demo.awesomeledger.type.ItemType;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

abstract class BasePieDiagramFragment extends Fragment {

    private PieChart pieChart;
    private ArrayList<Integer> colors;
    ItemType type;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_diagram, container, false);
        pieChart= view.findViewById(R.id.Pie);
        setColors();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initPieChart();
    }

    private void initPieChart() {
        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(25f); // 半透明圈
        pieChart.setHoleRadius(20);  //实心圆

        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90); // 初始旋转角度
        pieChart.setUsePercentValues(true);//设置显示百分比
        pieChart.setRotationEnabled(false); // 可以手动旋转
        pieChart.setUsePercentValues(true);  //显示成百分比
        pieChart.setCenterText(type.getType());  //饼状图中间的文字
        Description description = new Description();
        description.setText("");
        description.setPosition(0,0);
        pieChart.setDescription(description);
        //设置数据
        pieChart.setData(getPieData());
        //设置下方比例图信息
        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setForm(Legend.LegendForm.SQUARE);  //设置比例图的形状，默认是方形 SQUARE
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(7f);
        mLegend.setWordWrapEnabled(true);
        mLegend.setFormSize(22f);
        mLegend.setYOffset(20);
        pieChart.animateXY(1000, 1000);  //设置动画
        pieChart.invalidate(); // 重新更新显示
    }

    private PieData getPieData() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> pieColors = new ArrayList<>();
        Bundle bundle = getArguments();
        Calendar month = Calendar.getInstance();
        month.setTime(new Date(bundle.getLong("month")));
        for (int i = 0; i < 12; i++) {
            List<Item> itemList = ItemDao.getInstance(getContext())
                    .getItemsOfMonthAndKind(month, type, ItemKind.values()[i]);
            float totalMoney = 0f;
            if (itemList != null) {
                for (Item item: itemList) {
                    totalMoney += item.getMoney();
                }
                pieEntries.add(new PieEntry(totalMoney, ItemKind.values()[i].getKind()));
                pieColors.add(colors.get(i));
            }
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries,"");
        pieDataSet.setSliceSpace(5f); //设置个饼状图之间的距离
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(pieColors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度
        PieData piedata = new PieData(pieDataSet);
        piedata.setValueTextSize(12f);
        ArrayList<Integer> Text_colors = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Text_colors.add(Color.rgb(255, 255, 255));
        }
        piedata.setValueTextColors(Text_colors);
        return piedata;
    }

    private void setColors() {
        colors = new ArrayList<>();
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
    }
}
