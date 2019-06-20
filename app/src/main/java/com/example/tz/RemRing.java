package com.example.tz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class RemRing extends BroadcastReceiver
{
        @Override
        public void onReceive(Context context, Intent intent)
        {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tz:ringer");
            wl.acquire();

            Log.wtf("alaram", "zvoni");
            Toast.makeText(context, "Test, test, 1, 2, 3...", Toast.LENGTH_LONG).show();

            wl.release();
        }

        public void SetAlarm(Context context)
        {
            Log.wtf("alarm", "Set!");
            AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, RemRing.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10, pi);
        }

        public void CancelAlarm(Context context)
        {
            Intent intent = new Intent(context, RemRing.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        }

}
