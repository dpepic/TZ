package com.example.tz;

import android.app.Notification;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Hashtable;
import java.util.UUID;

public class RemWorker extends Worker
{
    static int id;
    public static Hashtable<UUID, String[]> tekst = new Hashtable<>();

    public RemWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params)
    {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        Log.wtf("worker", "active");

        Uri uri = Uri.parse("android.resource://com.example.tz/" + R.raw.zvonozaaplikaciju);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "TZK")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(tekst.get(this.getId())[0])
                .setContentText(tekst.get(this.getId())[1])
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(uri)
                .setDefaults(Notification.DEFAULT_ALL);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(RemWorker.id, builder.build());

        Log.wtf("ajdi", "From worker: " + this.getId());
        return Result.success();
    }

    public static void makeNot(String naziv, String opis)
    {

    }
}

