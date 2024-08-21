package com.classy.heatmaplibrary;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeatmapLayout extends ViewGroup {
    private List<TouchData> touchDataList = new ArrayList<>();
    private Map<String, Integer> touchCounts = new HashMap<>();  // Map to count touches per component

    public HeatmapLayout(Context context) {
        super(context);
    }

    public HeatmapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeatmapLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        trackTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void trackTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) { // Only count ACTION_DOWN
            float x = ev.getX();
            float y = ev.getY();
            String componentId = findComponentId(ev);

            // If the component ID is "Unknown", consider it as a background touch
            if (componentId == null || componentId.equals("Unknown")) {
                componentId = "Background";  // Use "Background" as the identifier for background touches
            }

            // Store touch data
            touchDataList.add(new TouchData(x, y, componentId));

            // Increment touch count for this component
            int currentCount = touchCounts.getOrDefault(componentId, 0);
            touchCounts.put(componentId, currentCount + 1);

            // Debugging log
            Log.d("HeatmapLayout", "Touch count for " + componentId + ": " + (currentCount + 1));
        }
    }

    private String findComponentId(MotionEvent ev) {
        View touchedView = findViewAt((int) ev.getX(), (int) ev.getY());
        if (touchedView != null && touchedView.getId() != View.NO_ID) {
            try {
                return touchedView.getResources().getResourceName(touchedView.getId());
            } catch (Resources.NotFoundException e) {
                // Fallback to a custom tag or another identifier if available
                if (touchedView.getTag() != null) {
                    return touchedView.getTag().toString();
                }
                return "Unknown";
            }
        }
        return "Unknown";
    }

    private View findViewAt(int x, int y) {
        for (int i = getChildCount() - 1; i >= 0; i--) { // Iterate from top-most view to bottom
            View child = getChildAt(i);
            if (x >= child.getLeft() && x <= child.getRight() &&
                    y >= child.getTop() && y <= child.getBottom()) {
                return child;
            }
        }
        return null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;

        // Measure each child view, including margins
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            width = Math.max(width, childWidth);
            height += childHeight;
        }

        // Account for padding
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = getPaddingTop(); // Start at the top, considering the padding

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();

                int left = getPaddingLeft() + lp.leftMargin;
                int childTop = top + lp.topMargin;

                // Layout the child view
                child.layout(left, childTop, left + childWidth, childTop + childHeight);

                // Update top for the next child view
                top += childHeight + lp.topMargin + lp.bottomMargin;
            }
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public List<TouchData> getTouchDataList() {
        return touchDataList;
    }

    public Map<String, Integer> getTouchCounts() {
        return touchCounts;
    }
}
