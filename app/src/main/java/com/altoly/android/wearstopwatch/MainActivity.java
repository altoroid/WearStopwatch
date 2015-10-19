package com.altoly.android.wearstopwatch;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView timeText;
    Button startButton, resetButton;
    long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    boolean isPaused = true; // toggle flag start or pause
    int secs = 0;
    int mins = 0;
    int milliseconds = 0;
    Handler customHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeText = (TextView) findViewById(R.id.textTimer);
        startButton = (Button) findViewById(R.id.start_button);
        resetButton = (Button) findViewById(R.id.reset_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isPaused) {
                    startButton.setText(getString(R.string.pause));
                    startButton.setTextColor(Color.RED);
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    isPaused = false;
                } else {
                    startButton.setText(getString(R.string.start));
                    startButton.setTextColor(Color.WHITE);
                    timeText.setTextColor(Color.WHITE);
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);
                    isPaused = true;
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startTime = 0L;
                timeSwapBuff = 0L;
                timeInMilliseconds = 0L;
                isPaused = true;
                secs = 0;
                mins = 0;
                milliseconds = 0;
                startButton.setText(getString(R.string.start));
                startButton.setTextColor(Color.WHITE);
                customHandler.removeCallbacks(updateTimerThread);
                timeText.setText(getString(R.string.start_time));
            }
        });

    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timeText.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }
    };
}
