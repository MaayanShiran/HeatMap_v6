package com.classy.heatmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class HeatMapView extends View {

    private Map<String, Integer> interactionFrequencyMap = new HashMap<>();
    private Paint paint;
    private float spotRadius = 30f;

    public HeatMapView(Context context) {
        super(context);
        init();
    }

    public HeatMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeatMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    public void addInteractionData(InteractionData data) {
        String key = data.getX() + "," + data.getY();
        interactionFrequencyMap.put(key, interactionFrequencyMap.getOrDefault(key, 0) + 1);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Map.Entry<String, Integer> entry : interactionFrequencyMap.entrySet()) {
            String[] parts = entry.getKey().split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int frequency = entry.getValue();

            int colorIntensity = Math.min(255, frequency * 10); // Adjust multiplier as needed
            paint.setColor(Color.argb(colorIntensity, 255, 0, 0));

            canvas.drawCircle(x, y, spotRadius, paint);
        }
    }
}
