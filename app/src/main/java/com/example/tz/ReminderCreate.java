package com.example.tz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.GregorianCalendar;

public class ReminderCreate extends AppCompatActivity {

    int vreme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_create);

        vreme = getIntent().getIntExtra("vreme", 0);
        TextView tv = findViewById(R.id.vreme);
        tv.setText(vreme/60 + " : " + vreme % 60);
    }

    public void test(View but)
    {

    }
    public void nazad(View but)
    {
        Intent test = new Intent(this, MainActivity.class);
        test.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(test, 0);
    }

    public void snimi(View but)
    {
        EditText et1 = findViewById(R.id.naslov);
        EditText et2 = findViewById(R.id.opis);
        CheckBox ch = findViewById(R.id.alarm);

        DatePicker dp = findViewById(R.id.datePicker);

        GregorianCalendar selected = new GregorianCalendar(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
        int idRem = 0;

            GregorianCalendar today = new GregorianCalendar();

            int daniRazlike = selected.get(GregorianCalendar.DAY_OF_YEAR) - today.get(GregorianCalendar.DAY_OF_YEAR);
            int minutRazlike = this.vreme - (today.get(GregorianCalendar.HOUR_OF_DAY) * 60 + today.get(GregorianCalendar.MINUTE));

            long razlikaMin = daniRazlike * 24 * 60 + minutRazlike;

        if (ch.isChecked() && selected.after(today)) {
            RemRing test = new RemRing();
            test.SetAlarm(getApplicationContext(), 5);
            idRem = test.id;
        }

        String out = selected.get(GregorianCalendar.YEAR) + "--" +selected.get(GregorianCalendar.MONTH) + "--" +
                selected.get(GregorianCalendar.DAY_OF_MONTH)+ "--" + vreme + ";" + ch.isChecked() + ";" + et1.getText() + ";" + et2.getText() + ";" + "id+" + idRem + "+id";

        try
        {
            FileOutputStream fOut = openFileOutput("test.csv",
                    MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(out);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (Exception joj)
        {
            joj.printStackTrace();
            Log.wtf("io", "Greska pri upisu");
        }



        Intent rem = new Intent(this, Reminder.class);
        startActivity(rem);

    }
}
