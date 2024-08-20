package com.classy.heatmap;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class InteractionTracker {

    private HeatMapView heatMapView;

    public InteractionTracker(Activity activity) {
        heatMapView = new HeatMapView(activity);
        ((ViewGroup) activity.findViewById(android.R.id.content)).addView(heatMapView);

        trackViews(activity.findViewById(android.R.id.content));
    }

    private void trackViews(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                trackViews(viewGroup.getChildAt(i));
            }
        } else {
            view.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    int x = location[0] + (int) event.getX();
                    int y = location[1] + (int) event.getY();
                    heatMapView.addInteractionData(new InteractionData(x, y, InteractionData.InteractionType.TAP));
                }
                return false;
            });

            view.setOnLongClickListener(v -> {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                heatMapView.addInteractionData(new InteractionData(x, y, InteractionData.InteractionType.LONG_PRESS));
                return false;
            });

            view.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    int x = location[0] + (int) event.getX();
                    int y = location[1] + (int) event.getY();
                    heatMapView.addInteractionData(new InteractionData(x, y, InteractionData.InteractionType.SWIPE));
                }
                return false;
            });
        }
    }
}
