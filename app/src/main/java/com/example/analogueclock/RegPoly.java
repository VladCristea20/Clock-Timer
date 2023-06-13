package com.example.analogueclock;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class RegPoly {
    // ivars
    private int n;
    private float x0, y0, r;
    private float x[], y[];
    // ivars for drawing
    private Canvas canvas; private Paint paint;

    public RegPoly(int n, float x0, float y0, float r, Canvas canvas, Paint paint) {
        this.n = n;
        this.x0 = x0;
        this.y0 = y0;
        this.r = r;
        this.canvas = canvas;
        this.paint = paint;

        // calculate x, y
        this.x = new float[n];this.y = new float[n];
        for(int i=0;i<n;i++){
            this.x[i] = (float)(this.x0+this.r*Math.cos(2*Math.PI*i/n));
            this.y[i] = (float)(this.y0+this.r*Math.sin(2*Math.PI*i/n));
        }
    }

    public float getX(int i){
        return this.x[i%this.n];
    }

    public float getY(int i){
        return this.y[i%this.n];
    }

    public void drawRadius(int i){
        this.canvas.drawLine(this.x0, this.y0, this.x[i%this.n], this.y[i%this.n], this.paint);
    }

    public void drawNodes(int radius, boolean withText){
        for(int i=0;i<this.n;i++){
            this.canvas.drawCircle(getX(i), getY(i), radius, this.paint);
            if(withText) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(48);
                String text=Integer.toString((i+3));
                if(i+3>12)
                    text=Integer.toString((i-9));
                this.canvas.drawText(text, getX(i), getY(i),paint);
            }
        }
    }
}
