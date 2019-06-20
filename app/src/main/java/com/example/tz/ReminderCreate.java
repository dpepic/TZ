package com.example.tz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

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
        String out = vreme + ";" + String.valueOf(ch.isChecked()) + ";" + et1.getText() + ";" + et2.getText();
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
            Intent rem = new Intent(this, Reminder.class);
            startActivity(rem);
        } catch (Exception joj)
        {
            joj.printStackTrace();
            Log.wtf("io", "Greska pri upisu");
        }
    }
}
