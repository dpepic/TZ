package com.example.tz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CircularSeekBar cb = findViewById(R.id.cs2);

        cb.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar cs, float progress, boolean fromUser)
                {
                    CircularSeekBar cs1 = findViewById(R.id.cs1);
                    cs1.setProgress(cs.getProgress() + 15);
                    TextView tv = findViewById(R.id.textView);
                    
                    String tst = String.valueOf((int)progress);
                    int testSplit = (int)progress;

                    int tBase = (testSplit/15) * 15;
                    if (testSplit%15 > 7)
                        tBase += 15;

                    int sati = tBase/60;
                    int minute = tBase % 60;


                    tv.setText(tBase + " --- " + String.valueOf(sati) + " : " + String.valueOf(minute));

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });



    }

    public void klik(View s)
    {

        CircularSeekBar cs2 = (CircularSeekBar) s;
        cs2.setProgress(10);

        CircularSeekBar cs = findViewById(R.id.cs1);
        cs.setProgress(10);

    }



}
