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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class Reminder extends AppCompatActivity
{
    boolean promena = false;
    Vector<String> rem = new Vector<String>();
    Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        try
        {
            FileInputStream fi = openFileInput("reminders.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(fi));

            LinearLayout l = findViewById(R.id.lejout);

            while (br.ready())
            {
                rem.add(br.readLine());
                String[] chk = rem.lastElement().split(";");
                CheckBox cb = new CheckBox(this);
                cb.setText(chk[2] + " --- " + chk[3]);
                cb.setChecked(chk[1].equals("true") ? true:false);
                cb.setLongClickable(true);
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                    {
                        LinearLayout l = findViewById(R.id.lejout);
                        String red = rem.elementAt(l.indexOfChild(compoundButton));
                        red = red.replace(String.valueOf(!b), String.valueOf(b));
                        if (!b)
                        {
                            String rep = red.split(";")[4];
                            Log.wtf("asd", red);
                            Log.wtf("asd", rep.split("\\+")[1]);
                            WorkManager.getInstance().cancelWorkById(UUID.fromString(rep.split("\\+")[1]));
                            RemWorker.tekst.remove(rep.split("\\+")[1]);
                            Log.wtf("id", rep);
                            red = red.replace(rep, "id+0+id");
                        } else
                        {
                            GregorianCalendar now = new GregorianCalendar();
                            String[] gregTime = red.split(";")[0].split("--");
                            GregorianCalendar alarm = new GregorianCalendar(Integer.parseInt(gregTime[0]), Integer.parseInt(gregTime[1]), Integer.parseInt(gregTime[2]),
                                    Integer.parseInt(gregTime[3])/60 , Integer.parseInt(gregTime[3]) % 60);

                            int daniRazlike = alarm.get(GregorianCalendar.DAY_OF_YEAR) - now.get(GregorianCalendar.DAY_OF_YEAR);
                            int minutRazlike = Integer.parseInt(gregTime[3]) - (now.get(GregorianCalendar.HOUR_OF_DAY) * 60 + now.get(GregorianCalendar.MINUTE));

                            long razlikaMin = daniRazlike * 24 * 60 + minutRazlike;

                            if (alarm.after(now))
                            {
                                RemWorker.id = r.nextInt(100000000) + 1;
                                OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(RemWorker.class)
                                        .setInitialDelay(1, TimeUnit.MINUTES)
                                        .build();

                                WorkManager.getInstance().enqueueUniqueWork(work.getId().toString(), ExistingWorkPolicy.REPLACE, work);
                                red = red.replace("id+0+id", "id+" + work.getId().toString() + "+id");
                                String[] txt = {red.split(";")[2], red.split(";")[3]};
                                RemWorker.tekst.put(work.getId(), txt);
                            }
                        }
                        rem.remove(l.indexOfChild(compoundButton));
                        rem.insertElementAt(red, l.indexOfChild(compoundButton));
                        promena = true;
                    }
                });

                cb.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View view)
                    {
                        LinearLayout l = findViewById(R.id.lejout);

                        String rep = rem.get(l.indexOfChild(view));
                        if (!rep.split(";")[4].equals("id+0+id"))
                        {
                            Log.wtf("asd", "brisem...");
                            WorkManager.getInstance().cancelWorkById(UUID.fromString(rep.split("\\+")[1]));
                            Log.wtf("asd", rep.split("\\+")[1]);
                            String replace = rep.split(";")[4];
                            RemWorker.tekst.remove(rep.split("\\+")[1]);
                            rep = rep.replace(replace, "id+0+id");
                        }
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
                FileOutputStream fOut = openFileOutput("reminders.csv",
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

    public void klik(View but)
    {
        Intent nazad = new Intent(Reminder.this, MainActivity.class);
        nazad.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(nazad, 0);
    }
}
