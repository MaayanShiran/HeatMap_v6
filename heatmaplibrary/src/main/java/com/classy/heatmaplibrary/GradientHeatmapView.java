package com.classy.heatmaplibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GradientHeatmapView extends View {

    private List<TouchData> touchDataList;
    private final int radius = 50; // Radius for overlap calculation

    public GradientHeatmapView(Context context, List<TouchData> touchDataList) {
        super(context);
        this.touchDataList = touchDataList;

        // Calculate overlaps
        calculateOverlaps();

        // Sort by overlap count
        Collections.sort(touchDataList, Comparator.comparingInt(TouchData::getOverlapCount));
    }

    private void calculateOverlaps() {
        for (int i = 0; i < touchDataList.size(); i++) {
            TouchData data = touchDataList.get(i);
            for (int j = 0; j < touchDataList.size(); j++) {
                if (i != j) {
                    TouchData otherData = touchDataList.get(j);
                    if (data.isNearby(otherData.getX(), otherData.getY(), radius)) {
                        data.incrementOverlapCount();
                    }
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Create a new Bitmap and Canvas to draw the heatmap
        Bitmap bitmap = generateHeatmapBitmap(getWidth(), getHeight());
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    public Bitmap generateHeatmapBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        // Define color thresholds
        int greenThreshold = touchDataList.size() / 3;
        int yellowThreshold = 2 * greenThreshold;

        for (int i = 0; i < touchDataList.size(); i++) {
            TouchData data = touchDataList.get(i);
            if (i < greenThreshold) {
                paint.setColor(Color.GREEN);
            } else if (i < yellowThreshold) {
                paint.setColor(Color.YELLOW);
            } else {
                paint.setColor(Color.RED);
            }
            paint.setAlpha(150); // Adjust transparency
            canvas.drawCircle(data.getX(), data.getY(), radius, paint);
        }

        return bitmap;
    }

}
