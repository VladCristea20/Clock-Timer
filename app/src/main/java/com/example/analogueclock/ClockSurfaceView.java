package com.example.analogueclock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.time.LocalTime;

public class ClockSurfaceView extends SurfaceView implements Runnable{

    private Thread thread;
    private boolean isRunning = false;
    private float length;
    private Context context;
    private SurfaceHolder holder;

    public ClockSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SurfaceView,
                0, 0);

        try {
            this.length = a.getFloat(R.styleable.SurfaceView_length, 0);
        } finally {
            a.recycle();
        }

        this.context = context;
        this.holder = getHolder();
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSPARENT);
    }

    // methods to manage thread
    public void onResumeClock(){
        thread = new Thread(this);
        thread.start();
        isRunning = true;
    }

    public void onPauseClock(){
        isRunning = false;
        boolean reEntry = true;
        while(reEntry){
            try {
                thread.join();
                reEntry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run(){
        LocalTime time = LocalTime.now();

        int seconds = time.getSecond();
        int minutes = time.getMinute();
        int hour = time.getHour();

        int sec = seconds,min=minutes*60, hr = (hour % 12) * 60 * 60 + minutes * 60 + seconds;
        while(isRunning){
            if(holder.getSurface().isValid()){
                // we are ready to draw
                Canvas canvas = holder.lockCanvas();

                // clear background
                Paint paint = new Paint();paint.setColor(Color.WHITE);
                canvas.drawPaint(paint);

                // draw sec marks
                paint.setColor(Color.BLUE);
                paint.setStrokeWidth(5);
                RegPoly secMarks = new RegPoly(60, getWidth()/2, getHeight()/2, this.length,
                        canvas, paint);
                secMarks.drawNodes(10,false);

                paint.setColor(Color.BLACK);
                RegPoly hourMarks = new RegPoly(12, getWidth()/2, getHeight()/2, this.length,
                        canvas, paint);
                hourMarks.drawNodes(15,false);

                paint.setColor(Color.TRANSPARENT);
                RegPoly hourDigits = new RegPoly(12, getWidth()/2, getHeight()/2, this.length+100,
                        canvas, paint);
                hourDigits.drawNodes(100,true);

                // draw the sec hand
                paint.setColor(Color.RED);
                paint.setStrokeWidth(10);
                RegPoly secHand = new RegPoly(60, getWidth()/2, getHeight()/2, this.length-50,
                        canvas, paint);
                secHand.drawRadius(sec+45);

                paint.setColor(Color.GREEN);
                RegPoly minHand = new RegPoly(3600, getWidth()/2, getHeight()/2, this.length-90,
                        canvas, paint);
                minHand.drawRadius(min+45*60);

                paint.setColor(Color.MAGENTA);
                RegPoly hourHand = new RegPoly(43200, getWidth()/2, getHeight()/2, this.length-160,
                        canvas, paint);
                hourHand.drawRadius(hr+45*60*12);

                // sleep
                try{thread.sleep(1000);}
                catch(Exception e){}
                sec++;
                min++;
                hr++;

                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

}
