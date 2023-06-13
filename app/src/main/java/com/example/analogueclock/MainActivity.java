package com.example.analogueclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private ClockSurfaceView clock;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        clock=findViewById(R.id.clockSurfaceView);
        button=findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem();
            }
        });

    }

    public void onClickItem() {

        Intent intent = new Intent(MainActivity.this, MainActivity2.class);

        // start activity
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        clock.onPauseClock();
    }

    @Override
    protected void onResume() {
        super.onResume();
        clock.onResumeClock();
    }
}