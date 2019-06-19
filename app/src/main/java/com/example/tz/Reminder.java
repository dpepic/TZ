package com.example.tz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Reminder extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        int minuti = getIntent().getIntExtra("vreme", 0);
        TextView tv = findViewById(R.id.textView4);
        tv.setText(String.valueOf(minuti));




    }

    public void citaj(View but)
    {
        try
        {
            FileInputStream fi = openFileInput("test.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(fi));

            LinearLayout l = findViewById(R.id.lejout);

            while (br.ready())
            {
                String[] chk = br.readLine().split(";");
                CheckBox cb = new CheckBox(this);
                cb.setText(chk[0]);
                cb.setChecked(chk[1].equals("on") ? true:false);
                l.addView(cb);
            }

        } catch (Exception joj)
        {
            Log.wtf("io", "nema fajla");
        }

    }

    public void pisi(View but)
    {
        try
        {
            FileOutputStream fOut = openFileOutput("test.csv",
                    MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write("jedan;off");
            bw.newLine();
            bw.write("dva;on");
            bw.newLine();
            bw.write("tri;off");
            bw.flush();
            bw.close();
        } catch (Exception joj)
        {
            joj.printStackTrace();
            Log.wtf("io", "Greska pri upisu");
        }
    }
    public void klik(View but)
    {
        Intent test = new Intent(Reminder.this, MainActivity.class);
        test.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(test, 0);
    }
}
