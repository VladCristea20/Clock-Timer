package com.example.analogueclock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TimerSurfaceView extends SurfaceView implements Runnable{

    private Thread thread;
    private boolean isRunning = false;

    private ITimeListener onTimerFinishedListener;
    private IColorChanger onColorChangedListener;

    public void setOnColorChangedListener(IColorChanger listener) {
        this.onColorChangedListener = listener;
    }

    public void setOnTimerFinishedListener(ITimeListener onTimerFinishedListener) {
        this.onTimerFinishedListener = onTimerFinishedListener;
    }

    private float length;
    private Context context;
    private SurfaceHolder holder;
    private int secs=0;

    public int getSecs() {
        return secs;
    }

    public void setSecs(int secs) {
        this.secs = secs;
    }

    public TimerSurfaceView(Context context , AttributeSet attrs) {
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
        holder = getHolder();
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
        int sec = 0;
        if (onTimerFinishedListener != null) {
            onTimerFinishedListener.onTimerClear();
        }
        while(isRunning){
            if(holder.getSurface().isValid()) {

                // check sec
                if (sec > secs) {
                    isRunning = false;
                    if (onTimerFinishedListener != null) {
                        onTimerFinishedListener.onTimerFinished();
                    }
                }

                // we are ready to draw
                Canvas canvas = holder.lockCanvas();

                // clear background
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                canvas.drawPaint(paint);

                // draw sec marks
                //paint.setColor(onColorChangedListener != null ? onColorChangedListener.onColorChanged(Color.BLUE) : Color.BLUE);
                paint.setColor(Color.BLUE);

                paint.setStrokeWidth(5);
                RegPoly secMarks = new RegPoly(60, getWidth() / 2, getHeight() / 2, this.length,
                        canvas, paint);
                secMarks.drawNodes(10, false);

                paint.setColor(Color.BLACK);
                //paint.setColor(onColorChangedListener != null ? onColorChangedListener.onColorChanged(Color.BLUE) : Color.BLUE);
                RegPoly hourMarks = new RegPoly(12, getWidth() / 2, getHeight() / 2, this.length,
                        canvas, paint);
                hourMarks.drawNodes(15, false);

                // draw the sector corresponding to sec
                if (this.secs > 0){
                    paint.setColor(Color.MAGENTA);
                    ///paint.setColor(onColorChangedListener != null ? onColorChangedListener.onColorChanged(Color.BLUE) : Color.BLUE);
                paint.setStrokeWidth(10);
                RectF rectF = new RectF(getWidth() / 2 - length + 50, getHeight() / 2 - length + 50,
                        getWidth() / 2 + length - 50, getHeight() / 2 + length - 50);
                float start = -90f, end = (float) (360.0 * sec / secs);
                canvas.drawArc(rectF, start, end, true, paint);
            }

                // sleep
                try{Thread.sleep(1000);}
                catch(Exception e){}
                if(secs!=0)
                sec++;
                else {
                    isRunning = false;
                }
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

}

