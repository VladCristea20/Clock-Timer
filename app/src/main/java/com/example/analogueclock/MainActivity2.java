package com.example.analogueclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity implements ITimeListener,IColorChanger {

    private TimerSurfaceView timerSurfaceView;
    private Button changePageButton,startTimerButton,clearTimerButton,color1,color2,color3;
    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity2);
        timerSurfaceView=findViewById(R.id.timerSurfaceView);
        timerSurfaceView.setOnTimerFinishedListener(this);
        timerSurfaceView.setOnColorChangedListener(this);
        editText=findViewById(R.id.editTextNumber);
        textView=findViewById(R.id.textview);
        changePageButton=findViewById(R.id.button2);
        startTimerButton=findViewById(R.id.buttonStart);
        clearTimerButton=findViewById(R.id.buttonClear);
        color1=findViewById(R.id.buttonColor1);
        color2=findViewById(R.id.buttonColor2);
        color3=findViewById(R.id.buttonColor3);

        changePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem();
            }
        });
        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStart();
            }
        });
        clearTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickClear();
            }
        });
        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorClick(v);
            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorClick(v);
            }
        });
        color3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorClick(v);
            }
        });

    }

    public void onClickItem() {

        Intent intent = new Intent(MainActivity2.this, MainActivity.class);

        // start activity
        startActivity(intent);
    }

    public void onClickStart() {
        timerSurfaceView.setSecs(Integer.parseInt(editText.getText().toString()));
        timerSurfaceView.onResumeClock();
    }

    public void onClickClear() {
        timerSurfaceView.onPauseClock();
        timerSurfaceView.setSecs(0);
        timerSurfaceView.onResumeClock();
    }

    public void onColorClick(View view) {
        switch (view.getId()) {
            case R.id.buttonColor1:
                if (timerSurfaceView instanceof IColorChanger) {
                    ((IColorChanger) this).onColorChanged(Color.RED);
                }
                break;
            case R.id.buttonColor2:
                if (timerSurfaceView instanceof IColorChanger) {
                    ((IColorChanger) this).onColorChanged(Color.GREEN);
                }
                break;
            case R.id.buttonColor3:
                if (timerSurfaceView instanceof IColorChanger) {
                    ((IColorChanger) this).onColorChanged(ContextCompat.getColor(this, R.color.purple_500));
                }
                break;
            default:
                if (timerSurfaceView instanceof IColorChanger) {
                    ((IColorChanger) this).onColorChanged(Color.WHITE);
                }
                break;
        }
        if(timerSurfaceView.getSecs()==0)
        {
            timerSurfaceView.onResumeClock();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        timerSurfaceView.onPauseClock();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerSurfaceView.onResumeClock();
    }
    @Override
    public void onTimerFinished() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText("Timer is up!");
            }
        });
    }
    public void onTimerClear() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText("");
            }
        });
    }

    @Override
    public int onColorChanged(int color) {
        return color;
    }
}