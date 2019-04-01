package com.example.parkingsystem;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;

public class Draw_chart {
    LineChart linechart;
    PieChart pieChart;
    BarChart barChart;

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

        linechart.setData(data);
        linechart.notifyDataSetChanged();
        linechart.invalidate();
    }

    public void Draw_piechart() {
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(15f);
        pieChart.setHoleRadius(40f);

        Collections.sort(pieData, new EntryXComparator());
        PieDataSet dataSet = new PieDataSet(pieData, "이용횟수");

        dataSet.setSliceSpace(1f);
        dataSet.setSelectionShift(20f);
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLACK);

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

        barChart.setData(data);
        barChart.setFitBars(false);
        barChart.animateXY(1000,1000);
        barChart.invalidate();
    }

}
