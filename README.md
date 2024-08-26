[![](https://jitpack.io/v/MaayanShiran/HeatMap_v6.svg)](https://jitpack.io/#MaayanShiran/HeatMap_v6)

# Heat Map Library 

This library tracks where users touch the screen and then creates a heatmap that visually shows which parts of the screen were touched the most. The heatmap uses colors to indicate how often different areas were touched: green for less frequent touches, yellow for medium, and red for the most frequent touches.

## GradientHeatmapView Class

This class generates the heatmap drawing:

List<TouchData> touchDataList: This is a list where each item represents a touch event (where someone touched the screen).

radius: This is a number that defines how close two touches have to be to be considered "overlapping."

GradientHeatmapView(Context context, List<TouchData> touchDataList): This is a constructor. It sets up the view with the list of touch data and then calculates overlaps (where touches are close together).

calculateOverlaps(): This method checks each touch point against all other touch points to see if they are within a certain distance (the radius). If they are, it counts them as overlapping.

onDraw(Canvas canvas): This method is called when it's time to draw the heatmap on the screen. It generates a bitmap (a kind of image) and draws it.

generateHeatmapBitmap(int width, int height): This method creates a picture (bitmap) that represents the heatmap. It uses colors (green, yellow, red) to show how many times different areas of the screen were touched. Green areas were touched less, red areas were touched more.

## HeatmapLayout Class

This class is a custom layout that tracks touch events on the screen.

List<TouchData> touchDataList: A list of all the touch points.

Map<String, Integer> touchCounts: A map that keeps track of how many times each part of the screen was touched.

dispatchTouchEvent(MotionEvent ev): This method is called every time the user touches the screen. It calls trackTouchEvent to record the touch.

trackTouchEvent(MotionEvent ev): This method records where the user touched the screen. It also figures out which part of the screen was touched and updates the count for that part.

findComponentId(MotionEvent ev): This method tries to find the ID of the screen component that was touched (like a button or an image). If it can't find one, it labels the touch as happening in the "Background."

findViewAt(int x, int y): This method checks which part of the screen was touched by looking at the coordinates (x, y) of the touch.

onMeasure(int widthMeasureSpec, int heightMeasureSpec) and onLayout(boolean changed, int l, int t, int r, int b): These methods handle measuring and laying out the child views (like buttons, images, etc.) within this custom layout.

## TouchData Class

This class represents a single touch on the screen.

float x, y: These store the coordinates (position) of the touch.

String componentId: This stores the ID of the screen component that was touched.

int overlapCount: This tracks how many other touches are close to this one.

incrementOverlapCount(): This method increases the count of overlaps for this touch.

isNearby(float x, float y, float radius): This method checks if another touch is close enough (within the radius) to be considered overlapping.
