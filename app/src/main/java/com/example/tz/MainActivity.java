package com.example.tz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.PowerManager;
import android.renderscript.Sampler;
import android.util.Log;
import android.util.TimeUtils;
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
import android.widget.Toast;

import java.lang.reflect.Field;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class MainActivity extends AppCompatActivity {

    String[] vremenske;
    int[] off;
    int offsetThumb = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RemRing test = new RemRing();
        test.SetAlarm(this);

        CircularSeekBar sbar = findViewById(R.id.cs2);

        GregorianCalendar vreme = new GregorianCalendar();
        sbar.setProgress(vreme.get(GregorianCalendar.HOUR_OF_DAY) * 60
                          + vreme.get(GregorianCalendar.MINUTE));

        int[] v = minutiUsate((int)sbar.getProgress(), 15);
        sbar.setProgress(v[0] * 60 + v[1]);
        TextView tv = findViewById(R.id.textView);
        tv.setText(String.valueOf(v[0] == 0 ? "00" : v[0]) + " : " + String.valueOf(v[1] == 0 ? "00" : v[1]));

        sbar = findViewById(R.id.cs1);
        v = minutiUsate((int)sbar.getProgress(), 15);
        sbar.setProgress(v[0] * 60 + v[1]);
        tv = findViewById(R.id.textView2);
        tv.setText(String.valueOf(v[0] == 0 ? "00" : v[0]) + " : " + String.valueOf(v[1] == 0 ? "00" : v[1]));

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

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(dropDown);

            // Set popupWindow height to 500px
            popupWindow.setHeight(350);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, vremenske);

        dropDown.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                TimeZone tz = TimeZone.getDefault();
                offsetThumb = off[i] - (int)TimeUnit.MINUTES.convert(tz.getRawOffset(), TimeUnit.MILLISECONDS);
                CircularSeekBar cs1 = findViewById(R.id.cs1);
                CircularSeekBar cs = findViewById(R.id.cs2);
                cs1.setProgress(cs.getProgress() + offsetThumb);
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

                cs1.setProgress(cs.getProgress() + offsetThumb);
                TextView tv = findViewById(R.id.textView);

                int[] v = minutiUsate((int)progress, 15);
                tv.setText(String.valueOf(v[0] == 0 ? "00" : v[0]) + " : " + String.valueOf(v[1] == 0 ? "00" : v[1]));
                tv = findViewById(R.id.textView2);

                v = minutiUsate((int)cs1.getProgress(), 15);
                tv.setText(String.valueOf(v[0] == 0 ? "00" : v[0]) + " : " + String.valueOf(v[1] == 0 ? "00" : v[1]));

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
    }

    int[] minutiUsate(int minuta, int podela)
    {
        int tBase = (minuta / podela) * podela;
        if (minuta % podela > 7)
            tBase += podela;

        int[] vreme = new int[2];
        vreme[0] = tBase / 60;
        vreme[1] = tBase % 60;
        return vreme;
    }

    public void remind(View but)
    {
        Intent namera = new Intent(getApplicationContext(), ReminderCreate.class);
        CircularSeekBar cb = findViewById(R.id.cs2);
        int[] min = minutiUsate((int)cb.getProgress(), 15);
        namera.putExtra("vreme", min[0]*60 + min[1]);
        startActivity(namera);
    }
}
