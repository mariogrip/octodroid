package com.mariogrip.octodroid;

import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neil on 8/7/15.
 */

public class UpdateGraph {
    public enum TempType{
        BedTarget, BedActual, HotEndTarget, HotEndActual
    }
    public static void setupChart(LineChart chart, LineData data, int color) {
        chart.setDrawGridBackground(false);
        chart.setDescription("");
        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setBackgroundColor(color);

        //sets up the legend

        Legend l = chart.getLegend();
        l.setEnabled(true);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setForm(Legend.LegendForm.CIRCLE);


        //sets up axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
/*
        ArrayList<String> xAxisList = new ArrayList<String>();
        xAxisList.add(0, "d");
        xAxisList.add(1, "DD");
        xAxis.setValues(xAxisList);
*/

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(5, true);
        leftAxis.setSpaceTop(15f);

        // chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setEnabled(true);


        chart.setData(data);
        // animate calls invalidate()...
        chart.animateX(2000);
    }

    public static LineData generateData() {


        ArrayList<Entry> BedTempActList = getTempArray(TempType.BedActual);

        ArrayList<Entry> BedTempTarList = getTempArray(TempType.BedTarget);
        ArrayList<Entry> HotEndTempActList = getTempArray(TempType.HotEndActual);
        ArrayList<Entry> HotEndTempTarList = getTempArray(TempType.HotEndTarget);
        ArrayList<String> xVals = getXVals( memory.temp.history.getHistoryArray().size());

        ArrayList<LineDataSet> dataSets = new ArrayList<>();


        LineDataSet BedActTemp = new LineDataSet(BedTempActList, "Bed Temp Actual");
        BedActTemp.setColor(Color.rgb(240, 238, 70));
        BedActTemp.setLineWidth(2.5f);
        BedActTemp.setCircleColor(Color.rgb(240, 238, 70));
        BedActTemp.setCircleSize(5f);
        BedActTemp.setFillColor(Color.rgb(240, 238, 70));
        BedActTemp.setDrawCubic(true);
        BedActTemp.setDrawValues(false);


        LineDataSet HotEndActTemp = new LineDataSet(HotEndTempActList, "Hot End Temp Actual");
        HotEndActTemp.setColor(Color.rgb(127, 255, 0));
        HotEndActTemp.setLineWidth(2.5f);
        HotEndActTemp.setCircleColor(Color.rgb(127, 255, 0));
        HotEndActTemp.setCircleSize(5f);
        HotEndActTemp.setFillColor(Color.rgb(127, 255, 0));
        HotEndActTemp.setDrawCubic(true);
        HotEndActTemp.setDrawValues(false);


       LineDataSet BedTargetTemp = new LineDataSet(BedTempTarList, "Bed Temp Target");
        BedTargetTemp.setColor(Color.rgb(72, 61, 39));
        BedTargetTemp.setLineWidth(2.5f);
        BedTargetTemp.setCircleColor(Color.rgb(240, 238, 70));
        BedTargetTemp.setCircleSize(5f);
        BedTargetTemp.setFillColor(Color.rgb(240, 238, 70));
        BedTargetTemp.setDrawCubic(true);
        BedTargetTemp.setDrawValues(false);

        LineDataSet HotEndTargetTemp = new LineDataSet(HotEndTempTarList, "Hot End Temp Target");
        HotEndTargetTemp.setColor(Color.rgb(255, 69, 0));
        HotEndTargetTemp.setLineWidth(2.5f);
        HotEndTargetTemp.setCircleColor(Color.rgb(240, 238, 70));
        HotEndTargetTemp.setFillColor(Color.rgb(240, 238, 70));
        HotEndTargetTemp.setDrawCubic(true);
        HotEndTargetTemp.setDrawValues(false);



        BedActTemp.setAxisDependency(YAxis.AxisDependency.LEFT);

        dataSets.add(BedActTemp);
        dataSets.add(HotEndActTemp);
        dataSets.add(BedTargetTemp);
        dataSets.add(HotEndTargetTemp);



        LineData data = new LineData(xVals, dataSets);
        return data;

    }

    private static ArrayList<Entry> getTempArray(TempType type) {
        ArrayList<Entry> tempHistory = new ArrayList<>();
        ArrayList<HistoryObject> historyArrayList = memory.temp.history.getHistoryArray();

        int interval = historyArrayList.size()/15;
        Log.i(interval+"","");
        switch (type){
            case BedTarget:
                for(int i =0; i < historyArrayList.size() && historyArrayList.size()!=0; i+= interval){
                   tempHistory.add(new Entry(historyArrayList.get(i).getBedTargetTemp(),i));
                }
                break;
            case BedActual:
                for(int i =0; i < historyArrayList.size() && historyArrayList.size()!=0; i+=interval){
                    tempHistory.add(new Entry(historyArrayList.get(i).getBedActTemp(), i));

                }
                break;
            case HotEndActual:
                for(int i =0; i < historyArrayList.size() && historyArrayList.size()!=0; i+= interval){
                    tempHistory.add(new Entry(historyArrayList.get(i).getHotEndActTemp(),i));
                }
                break;
            case HotEndTarget:
                for(int i =0; i < historyArrayList.size() && historyArrayList.size()!=0; i+= interval){
                    tempHistory.add(new Entry(historyArrayList.get(i).getHotEndTargetTemp(), i));
                }
                break;

        }


        return  tempHistory;
    }

    private static ArrayList<String> getXVals(int length) {
        ArrayList<String> testVals = new ArrayList<>();

        ArrayList<HistoryObject> historyArrayList = memory.temp.history.getHistoryArray();

        int interval = historyArrayList.size()/15;
        int time =0;
        String timeString = "";
        for(int i =length; i > 0; i--){
            time = ((i*interval));
            timeString = time/60 +":" + time %60;
           if(time % 60 < 10){
               timeString = time/60 +":" + time %60 +"0";
           }
            testVals.add("-"+timeString);
       }

    return testVals;
    }

    public static void updateChart(LineChart chart){

        chart.setData(generateData());
        chart.invalidate();
    }
}
