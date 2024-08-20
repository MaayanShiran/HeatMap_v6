package com.classy.heatmap;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
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
        float x = ev.getX();
        float y = ev.getY();
        String componentId = findComponentId(ev);

        // Store touch data
        touchDataList.add(new TouchData(x, y, componentId));

        // Increment touch count for this component
        int currentCount = touchCounts.getOrDefault(componentId, 0);
        touchCounts.put(componentId, currentCount + 1);
    }

    private String findComponentId(MotionEvent ev) {
        View touchedView = findViewAt((int) ev.getX(), (int) ev.getY());
        if (touchedView != null && touchedView.getId() != View.NO_ID) {
            try {
                return touchedView.getResources().getResourceName(touchedView.getId());
            } catch (Resources.NotFoundException e) {
                return "Unknown";
            }
        }
        return "Unknown";
    }

    private View findViewAt(int x, int y) {
        for (int i = 0; i < getChildCount(); i++) {
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

        // Measure each child view
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            ViewGroup.LayoutParams lp = child.getLayoutParams();

            int childWidth;
            int childHeight;

            if (lp instanceof MarginLayoutParams) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) lp;
                childWidth = child.getMeasuredWidth() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
                childHeight = child.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
            } else {
                childWidth = child.getMeasuredWidth();
                childHeight = child.getMeasuredHeight();
            }

            width = Math.max(width, childWidth);
            height = Math.max(height, childHeight);
        }

        // Account for padding
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            int left = getPaddingLeft();
            int top = getPaddingTop();

            ViewGroup.LayoutParams lp = child.getLayoutParams();

            if (lp instanceof MarginLayoutParams) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) lp;
                left += marginLayoutParams.leftMargin;
                top += marginLayoutParams.topMargin;
            }

            // Layout the child view
            child.layout(left, top, left + childWidth, top + childHeight);
        }
    }

    public List<TouchData> getTouchDataList() {
        return touchDataList;
    }

    public Map<String, Integer> getTouchCounts() {
        return touchCounts;
    }
}
