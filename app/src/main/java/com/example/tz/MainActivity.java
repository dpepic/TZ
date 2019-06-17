package com.example.tz;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DataSetObserver;
import android.renderscript.Sampler;
import android.util.Log;
import android.util.TimeUtils.*;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class MainActivity extends AppCompatActivity {

    String[] vremenske;
    int[] off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Set<String> testSet = new HashSet<String>();
        //Set je slican vektoru i steku, sa tim da set nece
        //da prihvati vrednosti koje nisu unikante, tj. ne moze
        //ista vrednost da se nadje u setu dva puta
        testSet.add("asd");
        testSet.add("qwe");
        testSet.add("asd");

        /*for (String s: testSet)
        {
            Log.wtf("testSet", s);
        }*/

        vremenske = getResources().getStringArray(R.array.imena);
        off = getResources().getIntArray(R.array.offset);

        Spinner dropDown = findViewById(R.id.TZdd);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, vremenske);


        dropDown.setAdapter(adapter);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                TextView tv = findViewById(R.id.textView);
                tv.setText(String.valueOf(off[i]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        CircularSeekBar cb = findViewById(R.id.cs2);

        cb.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar cs, float progress, boolean fromUser) {
                CircularSeekBar cs1 = findViewById(R.id.cs1);
                cs1.setProgress(cs.getProgress() + 15);
                TextView tv = findViewById(R.id.textView);

                String tst = String.valueOf((int) progress);
                int testSplit = (int) progress;

                int tBase = (testSplit / 15) * 15;
                if (testSplit % 15 > 7)
                    tBase += 15;

                int sati = tBase / 60;
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




    public void klik(View s) {

        CircularSeekBar cs2 = (CircularSeekBar) s;
        cs2.setProgress(10);

        CircularSeekBar cs = findViewById(R.id.cs1);
        cs.setProgress(10);

    }


}
