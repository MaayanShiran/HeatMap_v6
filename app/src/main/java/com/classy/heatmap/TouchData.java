package com.classy.heatmap;

public class TouchData {
    private float x;
    private float y;
    private String componentId;
    private int overlapCount = 0;  // New field to track the number of overlaps

    public TouchData(float x, float y, String componentId) {
        this.x = x;
        this.y = y;
        this.componentId = componentId;
    }

    public void incrementOverlapCount() {
        this.overlapCount++;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getComponentId() {
        return componentId;
    }

    public int getOverlapCount() {
        return overlapCount;
    }

    public boolean isNearby(float x, float y, float radius) {
        float distance = (float) Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
        return distance <= radius; // Consider touch nearby if within the radius
    }
}
