package com.example.studystation.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

public class MyCanvas extends View {

    Paint paint;
    Path path;
    float xPos = 50;
    float yPos = 50;
    float xInit = 0;
    float yInit = 0;
    ShapeDrawable shapeDrawable;


    public MyCanvas(Context context) {
        super(context);
        paint = new Paint();
        path = new Path();

        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeJoin(Paint.Join.ROUND);
//        paint.setAlpha(1);
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
//        canvas.drawLine(xInit , yInit , xPos , yPos , paint);
//        shapeDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        xPos = event.getX();
        yPos = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN : {
                path.moveTo(xPos, yPos);
                xInit = xPos;
                yInit = yPos;
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
        invalidate();
        return true;
    }
}
