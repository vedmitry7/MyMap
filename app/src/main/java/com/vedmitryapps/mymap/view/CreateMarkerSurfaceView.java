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
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
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

}