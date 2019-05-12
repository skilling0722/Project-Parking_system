package com.example.parkingsystem;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;

public class Draw_chart {
    LineChart linechart;
    PieChart pieChart;
    BarChart barChart;
    HorizontalBarChart horizontalbarchart;

    ArrayList<Entry> lineData;
    ArrayList<PieEntry> pieData;
    ArrayList<BarEntry> barData;

    public Draw_chart() {

    }

    public LineChart getLinechart() {
        return linechart;
    }

    public void setLinechart(LineChart linechart) {
        this.linechart = linechart;
    }

    public PieChart getPieChart() {
        return pieChart;
    }

    public void setPieChart(PieChart pieChart) {
        this.pieChart = pieChart;
    }

    public BarChart getBarChart() {
        return barChart;
    }

    public void setBarChart(BarChart barChart) {
        this.barChart = barChart;
    }

    public HorizontalBarChart getHorizontalbarchart() {
        return horizontalbarchart;
    }

    public void setHorizontalbarchart(HorizontalBarChart horizontalbarchart) {
        this.horizontalbarchart = horizontalbarchart;
    }

    public ArrayList<Entry> getLineData() {
        return lineData;
    }

    public void setLineData(ArrayList<Entry> lineData) {
        this.lineData = lineData;
    }

    public ArrayList<PieEntry> getPieData() {
        return pieData;
    }

    public void setPieData(ArrayList<PieEntry> pieData) {
        this.pieData = pieData;
    }

    public ArrayList<BarEntry> getBarData() {
        return barData;
    }

    public void setBarData(ArrayList<BarEntry> barData) {
        this.barData = barData;
    }

    public void Draw_linechart() {
        linechart.getDescription().setEnabled(false);
        Collections.sort(lineData, new EntryXComparator());
        LineDataSet line_dataset = new LineDataSet(lineData, "이용횟수");

        Legend legend = linechart.getLegend();
        legend.setTextColor(Color.WHITE);

        XAxis xAxis = linechart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);

        YAxis yaxisleft = linechart.getAxisLeft();
        yaxisleft.setTextColor(Color.WHITE);
        YAxis yaxisright = linechart.getAxisRight();
        yaxisright.setDrawLabels(false);
        yaxisright.setDrawAxisLine(false);
        yaxisright.setDrawGridLines(false);

        LineData data = new LineData(line_dataset);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(13f);
        data.setValueFormatter(new DefaultValueFormatter(0));

        linechart.setData(data);
        linechart.notifyDataSetChanged();
        linechart.invalidate();
    }

    public void Draw_piechart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        //pieChart.setHoleRadius(40f);

        Collections.sort(pieData, new EntryXComparator());

        Description description = new Description();
        description.setText("날씨별 이용수"); //라벨
        description.setTextSize(15);
        description.setTextColor(Color.WHITE);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션
//        예제 데이터
//        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
//
//        yValues.add(new PieEntry(34f,"Clean"));
//        yValues.add(new PieEntry(20f,"Clouds"));
//        yValues.add(new PieEntry(14f,"Rain"));
//        yValues.add(new PieEntry(35f,"Snow"));
//        yValues.add(new PieEntry(40f,"Mist"));
//        yValues.add(new PieEntry(40f,"Dust"));

        /*차트내 색깔, 설정 및 데이터 넣기*/
        PieDataSet dataSet = new PieDataSet(pieData, "날씨");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        /* 차트내에 %퍼센트 색 */
        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }

    public void Draw_barchart() {
        barChart.getDescription().setEnabled(false);

        Collections.sort(barData, new EntryXComparator());
        BarDataSet dataSet = new BarDataSet(barData, "이용횟수");

        Legend legend = barChart.getLegend();
        legend.setTextColor(Color.WHITE);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);

        YAxis yaxisleft = barChart.getAxisLeft();
        yaxisleft.setTextColor(Color.WHITE);

        YAxis yaxisright = barChart.getAxisRight();
        yaxisright.setDrawLabels(false);
        yaxisright.setDrawAxisLine(false);
        yaxisright.setDrawGridLines(false);

        BarData data = new BarData(dataSet);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(13f);
        data.setValueFormatter(new DefaultValueFormatter(0));

        barChart.setData(data);
        barChart.setFitBars(false);
        barChart.notifyDataSetChanged();
        barChart.animateXY(1000,1000);
        barChart.invalidate();
    }

    public void Draw_horizontalbarchart() {
        final String[] week = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        horizontalbarchart.getDescription().setEnabled(false);
        Collections.sort(barData, new EntryXComparator());
        BarDataSet dataSet = new BarDataSet(barData, "이용횟수");

        Legend legend = horizontalbarchart.getLegend();
        legend.setTextColor(Color.WHITE);

        XAxis xAxis = horizontalbarchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float day, AxisBase axis) {
                int intValue = (int) day - 1;
                if (week.length > intValue && intValue >= 0) {
                    return week[intValue];
                }
                return "";
            }
        });

        YAxis yaxisleft = horizontalbarchart.getAxisLeft();
        yaxisleft.setTextColor(Color.WHITE);

        YAxis yaxisright = horizontalbarchart.getAxisRight();
        yaxisright.setDrawLabels(false);
        yaxisright.setDrawAxisLine(false);
        yaxisright.setDrawGridLines(false);

        BarData data = new BarData(dataSet);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(13f);
        data.setValueFormatter(new DefaultValueFormatter(0));
        horizontalbarchart.setData(data);
        horizontalbarchart.setFitBars(false);
        horizontalbarchart.notifyDataSetChanged();
        horizontalbarchart.animateXY(1000,1000);
        horizontalbarchart.invalidate();
    }

}

