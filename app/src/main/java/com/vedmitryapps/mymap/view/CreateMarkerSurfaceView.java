package com.vedmitryapps.mymap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CreateMarkerSurfaceView extends View {
    private Paint paint = new Paint();
    private Path path = new Path();
    private int color;
    private float strokeWidth;
    private boolean chooseModeFlag;
    private float xTouch;
    private float yTouch;

    private ArrayList<Pair<Path, Paint>> paths;


    public CreateMarkerSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPant();
        setBackgroundColor(Color.TRANSPARENT);

        paths = new ArrayList<>();
    }
    @Override
    protected void onDraw(Canvas canvas) {

        for (Pair<Path, Paint> p:paths
             ) {
            canvas.drawPath(p.first, p.second);
        }
        Log.i("TAG21", "size path = " + paths.size());


        canvas.drawPath(path, paint);
        if(chooseModeFlag) {
            Paint p = new Paint();
            int radius = 50;
            p.setAntiAlias(true);
            p.setColor(Color.DKGRAY);
            p.setStyle(Paint.Style.FILL);
            canvas.drawCircle(xTouch, yTouch, radius, p);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionIndex = event.getActionIndex();
        float eventX = event.getX();
        float eventY = event.getY();
        if(chooseModeFlag){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xTouch = (int) event.getX(0);
                    yTouch = (int) event.getY(0);
                    getPositionMarker();
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    xTouch = (int) event.getX();
                    yTouch = (int) event.getY();
                    invalidate();
                    break;
                default:
                    break;
            }
            return true;
        }


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
                Pair<Path, Paint> pair = new Pair<>(path, paint);
                paths.add(pair);
                color = paint.getColor();
                strokeWidth = paint.getStrokeWidth();
                initPant();
                paint.setColor(color);
                paint.setStrokeWidth(strokeWidth);
                path = new Path();
                break;
            default:
                return false;
        }
        // Schedules a repaint.
        invalidate();
        return true;
    }

    private void initPant() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(25f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setStrokeWidth(int width){
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(width);
    }

    public float[] getPositionMarker(){
        float x = xTouch/this.getWidth();
        float y = yTouch/this.getHeight();
        float[] points = {x,y};
        Log.i("TAG21", "Create Anchor - " + String.valueOf(points[0]) +"  :  "+ String.valueOf(points[1]));
        return points;
    }

    public void nextStep() {
        chooseModeFlag = true;
        xTouch = 0;
        yTouch = 0;
    }

    public void prepareToScreen(){
        chooseModeFlag = false;
        invalidate();
    }

    public void setColor(int color) {
        paint.setColor(color);
    }
}