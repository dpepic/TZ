package com.example.tz;

import android.app.Notification;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.*;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.util.Hashtable;
import java.util.UUID;

public class RemWorker extends Worker
{
    static int id;
    public static Hashtable<UUID, String[]> tekst = new Hashtable<>();

    public RemWorker(
            Context context,
            WorkerParameters params)
    {
        super(context, params);
    }


    @Override
    public Result doWork()
    {
        Log.wtf("worker", "active");

        Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.zvonozaaplikaciju);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "TZK")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(tekst.get(this.getId())[0])
                .setContentText(tekst.get(this.getId())[1])
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        Notification n = builder.build();
        //n.sound = uri;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(RemWorker.id, n);

        Log.wtf("ajdi", "From worker: " + this.getId());
        return Result.success();
    }

    public static void makeNot(String naziv, String opis)
    {

    }
}

