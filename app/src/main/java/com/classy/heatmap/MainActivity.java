package com.classy.heatmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.classy.heatmaplibrary.GradientHeatmapView;
import com.classy.heatmaplibrary.HeatmapLayout;
import com.classy.heatmaplibrary.TouchData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 1; // Declare this constant

    private HeatmapLayout heatmapLayout;
    private Button btnSaveGraph, btnSaveHeatmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the UI elements
        heatmapLayout = findViewById(R.id.heatmap_layout);
        btnSaveGraph = findViewById(R.id.btn_save_graph);
        btnSaveHeatmap = findViewById(R.id.btn_save_heatmap);

        // Check if permission is not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

        // Save the bar chart as an image when the button is clicked
        btnSaveGraph.setOnClickListener(v -> saveBarChartAsImage());

        // Save the heatmap as an image
        btnSaveHeatmap.setOnClickListener(v -> saveHeatmapAsImage());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveHeatmapAsImage() {
        // Create the heatmap view with the touch data
        List<TouchData> touchDataList = heatmapLayout.getTouchDataList();
        GradientHeatmapView heatmapView = new GradientHeatmapView(this, touchDataList);

        // Generate the bitmap for the heatmap
        Bitmap bitmap = heatmapView.generateHeatmapBitmap(heatmapLayout.getWidth(), heatmapLayout.getHeight());

        // Save the heatmap bitmap to a file
        saveBitmapToFile(bitmap, "heatmap_");
    }

    private void saveBarChartAsImage() {
        // Create a BarChart programmatically
        BarChart barChart = new BarChart(this);

        // Prepare the data for the chart
        Map<String, Integer> touchCounts = heatmapLayout.getTouchCounts();
        List<BarEntry> entries = new ArrayList<>();
        List<String> componentNames = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Integer> entry : touchCounts.entrySet()) {
            String componentName = entry.getKey();
            int touchCount = entry.getValue();

            // Add an entry for the chart
            entries.add(new BarEntry(index++, touchCount));
            componentNames.add(componentName);
        }

        // Set up the data set and the chart
        BarDataSet dataSet = new BarDataSet(entries, "Touch Counts");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(dataSet);
        barData.setValueTextSize(8f);  // Set the size of the value text
        barChart.setData(barData);

        // Customize the X-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(componentNames));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(-45); // Rotate labels to fit them better
        xAxis.setTextColor(Color.BLACK); // Set X-axis label color
        xAxis.setTextSize(8f); // Reduce the text size for the X-axis labels

        // Customize the Y-axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setTextColor(Color.BLACK); // Set Y-axis label color
        barChart.getAxisRight().setEnabled(false); // Disable right Y-axis

        // Set the chart's background color
        barChart.setBackgroundColor(Color.WHITE);

        // Increase chart height to avoid cropping the data
        int chartHeight = 800; // Increase this value as needed
        barChart.measure(
                View.MeasureSpec.makeMeasureSpec(600, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(chartHeight, View.MeasureSpec.EXACTLY));
        barChart.layout(0, 0, barChart.getMeasuredWidth(), barChart.getMeasuredHeight());

        // Create the bitmap and draw the chart on it
        Bitmap bitmap = Bitmap.createBitmap(barChart.getWidth(), barChart.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        barChart.draw(canvas);

        // Save the bitmap to a file
        saveBitmapToFile(bitmap, "bar_chart_");
    }

    private void saveBitmapToFile(Bitmap bitmap, String fileNamePrefix) {
        String fileName = fileNamePrefix + System.currentTimeMillis() + ".png";
        File directory = new File(Environment.getExternalStorageDirectory(), "Charts_001");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            Toast.makeText(this, "File saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
