package com.example.tz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ReminderCreate extends AppCompatActivity {

    int vreme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_create);

        vreme = getIntent().getIntExtra("vreme", 0);
        TextView tv = findViewById(R.id.vreme);
        tv.setText(vreme / 60 + " : " + (vreme % 60 == 0 ? "00" : vreme % 60));
    }

    public void nazad(View but) {
        Intent nazad = new Intent(this, MainActivity.class);
        nazad.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(nazad, 0);
    }

    public void snimi(View but) {
        EditText et1 = findViewById(R.id.naslov);
        EditText et2 = findViewById(R.id.opis);


        DatePicker dp = findViewById(R.id.datePicker);

        GregorianCalendar selected = new GregorianCalendar(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(),
                vreme / 60, vreme % 60);

        GregorianCalendar today = new GregorianCalendar();

        int daniRazlike = selected.get(GregorianCalendar.DAY_OF_YEAR) - today.get(GregorianCalendar.DAY_OF_YEAR);
        int minutRazlike = this.vreme - (today.get(GregorianCalendar.HOUR_OF_DAY) * 60 + today.get(GregorianCalendar.MINUTE));
        String ajDi = "0";
        long razlikaMin = daniRazlike * 24 * 60 + minutRazlike;

        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(RemWorker.class)
                    .setInitialDelay(1, TimeUnit.MINUTES)
                    .build();

            WorkManager.getInstance().enqueueUniqueWork(work.getId().toString(), ExistingWorkPolicy.REPLACE, work);
            String[] arr = {et1.getText().toString(), et2.getText().toString()};
            RemWorker.tekst.put(work.getId(), arr);
            ajDi = work.getId().toString();


        String out = selected.get(GregorianCalendar.YEAR) + "--" + selected.get(GregorianCalendar.MONTH) + "--" +
                selected.get(GregorianCalendar.DAY_OF_MONTH) + "--" + vreme + ";true;" + et1.getText() + ";" + et2.getText() + ";" + "id+" + ajDi + "+id";
    Log.wtf("asd", out);
        try {
            FileOutputStream fOut = openFileOutput("reminders.csv",
                    MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(out);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (Exception joj) {
            joj.printStackTrace();
            Log.wtf("io", "Greska pri upisu");
        }

        Intent rem = new Intent(this, Reminder.class);
        startActivity(rem);
    }
}
