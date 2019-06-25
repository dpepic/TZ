package com.example.tz;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class RemWorker extends Worker {

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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "TZK")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("asdasd")
                .setContentText("teeeest")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(10006, builder.build());
        return Result.success();
    }
}

