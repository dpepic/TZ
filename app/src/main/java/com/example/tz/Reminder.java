package com.example.tz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.util.Vector;

public class Reminder extends AppCompatActivity
{
    boolean promena = false;
    Vector<String> rem = new Vector<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        try
        {
            FileInputStream fi = openFileInput("test.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(fi));

            LinearLayout l = findViewById(R.id.lejout);

            while (br.ready())
            {
                rem.add(br.readLine());
                String[] chk = rem.lastElement().split(";");
                CheckBox cb = new CheckBox(this);
                cb.setText(chk[0] + " --- " + chk[2] + " --- " + chk[3]);
                cb.setChecked(chk[1].equals("true") ? true:false);
                cb.setLongClickable(true);
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                    {
                        LinearLayout l = findViewById(R.id.lejout);
                        String asd = rem.elementAt(l.indexOfChild(compoundButton));
                        Log.wtf("ch", asd);
                        asd = asd.replace(String.valueOf(!b), String.valueOf(b));
                        Log.wtf("ch", asd);
                        rem.remove(l.indexOfChild(compoundButton));
                        rem.insertElementAt(asd, l.indexOfChild(compoundButton));
                        promena = true;
                    }
                });
                cb.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View view)
                    {
                        LinearLayout l = findViewById(R.id.lejout);
                        rem.remove(l.indexOfChild(view));
                        l.removeView(view);
                        promena = true;
                        return true;
                    }
                });
                l.addView(cb);
            }

        } catch (Exception joj)
        {
            Log.wtf("io", "nema fajla");
        }


    }

    @Override
    protected void onStop()
    {
        if (promena)
        {
            try {
                FileOutputStream fOut = openFileOutput("test.csv",
                        MODE_PRIVATE);
                OutputStreamWriter osw = new OutputStreamWriter(fOut);
                BufferedWriter bw = new BufferedWriter(osw);
                for (String str: rem)
                {
                    bw.write(str);
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            } catch (Exception joj) {}
        }
        super.onStop();
    }

    public void citaj(View but)
    {


    }

    public void pisi(View but)
    {

    }
    public void klik(View but)
    {
        Intent test = new Intent(Reminder.this, MainActivity.class);
        test.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(test, 0);
    }
}
