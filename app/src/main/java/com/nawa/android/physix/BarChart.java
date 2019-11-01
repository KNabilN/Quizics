package com.nawa.android.physix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarChart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        com.github.mikephil.charting.charts.BarChart barChart = findViewById(R.id.barchart);

        int a = getIntent().getIntExtra("A", 10);
        int b = getIntent().getIntExtra("B", 10);
        int c = getIntent().getIntExtra("C", 10);
        int d = getIntent().getIntExtra("D", 10);
        int e = getIntent().getIntExtra("E", 10);

        ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(a, 0));
            entries.add(new BarEntry(b, 1));
            entries.add(new BarEntry(c, 2));
            entries.add(new BarEntry(d, 3));
            entries.add(new BarEntry(e, 4));

        BarDataSet bardataset = new BarDataSet(entries, "Cells");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("A");
        labels.add("B");
        labels.add("C");
        labels.add("D");
        labels.add("E");

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of lables into chart

        barChart.setDescription("Results");  // set the description

        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.animateY(5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
