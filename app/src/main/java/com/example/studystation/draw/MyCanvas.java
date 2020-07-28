package com.example.studystation.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

public class MyCanvas extends View {

    Paint paint;
    Path path;

    public MyCanvas(Context context) {
        super(context);
        paint = new Paint();
        path = new Path();

        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAlpha(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);

    }

    public MyCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path , paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN : {
                path.moveTo(xPos, yPos);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                path.lineTo(xPos , yPos);
                break;
            }
            case MotionEvent.ACTION_UP:{
                return false;
            }
        }
        postInvalidate();
        return true;
    }
}
