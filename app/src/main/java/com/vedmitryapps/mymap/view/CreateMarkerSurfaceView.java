package com.vedmitryapps.mymap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CreateMarkerSurfaceView extends View {
    private Paint paint = new Paint();
    private Path path = new Path();
    private boolean chooseModeflag;
    private float xTouch;
    private float yTouch;
    public CreateMarkerSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(25f);
        setBackgroundColor(Color.TRANSPARENT);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
        if(chooseModeflag) {
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
        if(chooseModeflag){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xTouch = (int) event.getX(0);
                    yTouch = (int) event.getY(0);
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
                return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                path.lineTo(eventX, eventY);
                break;
            default:
                return false;
        }
        // Schedules a repaint.
        invalidate();
        return true;
    }

    public void setStrokeWidth(int width){
        paint.setStrokeWidth(width);
    }

    public void nextStep() {
        chooseModeflag = true;
    }
}