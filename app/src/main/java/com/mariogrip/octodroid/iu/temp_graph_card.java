package com.mariogrip.octodroid.iu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.mariogrip.octodroid.R;

import java.util.ArrayList;

/**
 * Created by tom on 7/12/15.
 */
public class temp_graph_card extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.card_graph, container, false);
        LineChart chart = (LineChart) v.findViewById(R.id.chart);

        setupChart(chart, generateData(), getResources().getColor(R.color.graph_backGround));


        return v;
    }


    private void setupChart(LineChart chart, LineData data, int color) {


        chart.setDescription("");
        chart.setNoDataTextDescription("You need to provide data for the chart.");

        chart.setDrawGridBackground(false);

        chart.setTouchEnabled(true);


        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);


        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.setBackgroundColor(color);


        chart.setViewPortOffsets(10, 0, 10, 0);


        chart.setData(data);


        Legend l = chart.getLegend();
        l.setEnabled(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setEnabled(false);

        // animate calls invalidate()...
        chart.animateX(2000);
    }

    public LineData generateData() {
        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            xVals.add(Integer.toString(i));
        }
        ArrayList<Entry> numbers = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Entry entry = new Entry(i, i);
            numbers.add(entry);
        }
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        LineDataSet setComp1 = new LineDataSet(numbers, "Bed Temp");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        dataSets.add(setComp1);

        LineData data = new LineData(xVals, dataSets);
        return data;

    }

}
