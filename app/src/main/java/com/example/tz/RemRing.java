package com.example.tz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RemRing extends BroadcastReceiver
{
    String naziv, tekst;

    static Random r = new Random();
    int id = r.nextInt(1000000) + 1;

        @Override
        public void onReceive(Context context, Intent intent)
        {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tz:ringer");
            wl.acquire();

            long[] vibPat = {1000, 500, 1000, 500, 1000, 500};

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "TZK")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(this.naziv)
                    .setContentText(this.tekst)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setVibrate(vibPat);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            notificationManager.notify(this.id, builder.build());

            this.CancelAlarm(context);

            wl.release();
        }

        public void SetAlarm(Context context, long minuta)
        {
            AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, RemRing.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, this.id, i, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), minuta * 60 * 1000, pi);
        }

        public void CancelAlarm(Context context)
        {
            Intent intent = new Intent(context, RemRing.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, this.id, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        }

}
