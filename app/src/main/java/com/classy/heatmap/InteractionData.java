package com.classy.heatmap;


public class InteractionData {
    private int x;
    private int y;
    private InteractionType type;



    public enum InteractionType {
        TAP,
        SWIPE,
        LONG_PRESS
    }

    public InteractionData(int x, int y, InteractionType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public InteractionType getType() {
        return type;
    }
}
