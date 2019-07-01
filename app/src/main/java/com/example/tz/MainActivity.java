package com.example.tz;

import androidx.appcompat.app.AppCompatActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import me.tankery.lib.circularseekbar.CircularSeekBar;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    String[] vremenske;
    int[] off;
    int offsetThumb = 0;

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TZkanal";
            String description = "kanal za notifikacije TZ";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("TZK", name, importance);
            channel.setDescription(description);

            Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.zvonozaaplikaciju);
            channel.setSound(uri, new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.createNotificationChannel();

        CircularSeekBar sbar = findViewById(R.id.cs2);

        GregorianCalendar vreme = new GregorianCalendar();
        sbar.setProgress(vreme.get(GregorianCalendar.HOUR_OF_DAY) * 60
                          + vreme.get(GregorianCalendar.MINUTE));

        int[] v = minutiUsate((int)sbar.getProgress(), 15);
        sbar.setProgress(v[0] * 60 + v[1]);
        TextView tv = findViewById(R.id.textView);
        tv.setText("Local: " + String.valueOf(v[0] == 0 ? "00" : v[0]) + " : " + String.valueOf(v[1] == 0 ? "00" : v[1]));

        sbar = findViewById(R.id.cs1);
        v = minutiUsate((int)sbar.getProgress(), 15);
        sbar.setProgress(v[0] * 60 + v[1]);
        tv = findViewById(R.id.textView2);
        tv.setText(String.valueOf(v[0] == 0 ? "00" : v[0]) + " : " + String.valueOf(v[1] == 0 ? "00" : v[1]));

        vremenske = getResources().getStringArray(R.array.imena);
        off = getResources().getIntArray(R.array.offset);

        Spinner dropDown = findViewById(R.id.TZdd);

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(dropDown);

            popupWindow.setHeight(350);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) { }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, vremenske);

        dropDown.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                TimeZone tz = TimeZone.getDefault();
                offsetThumb = off[i] - (int)TimeUnit.MINUTES.convert(tz.getRawOffset(), TimeUnit.MILLISECONDS);
                CircularSeekBar cs1 = findViewById(R.id.cs1);
                CircularSeekBar cs = findViewById(R.id.cs2);
                cs1.setProgress(cs.getProgress() + offsetThumb);

                TextView tv = findViewById(R.id.textView);

                CircularSeekBar cs2 = findViewById(R.id.cs2);
                int[] v = minutiUsate((int)cs2.getProgress(), 15);
                tv.setText("Local: " + String.valueOf(v[0] == 0 ? "00" : v[0]) + " : " + String.valueOf(v[1] == 0 ? "00" : v[1]));
                tv = findViewById(R.id.textView2);

                v = minutiUsate((int)cs1.getProgress(), 15);
                tv.setText(String.valueOf(v[0] == 0 ? "00" : v[0]) + " : " + String.valueOf(v[1] == 0 ? "00" : v[1]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        CircularSeekBar cb = findViewById(R.id.cs2);

        cb.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar cs, float progress, boolean fromUser) {
                CircularSeekBar cs1 = findViewById(R.id.cs1);

                cs1.setProgress(cs.getProgress() + offsetThumb);
                TextView tv = findViewById(R.id.textView);

                int[] v = minutiUsate((int)progress, 15);
                tv.setText("Local: " + String.valueOf(v[0] == 0 ? "00" : v[0]) + " : " + String.valueOf(v[1] == 0 ? "00" : v[1]));
                tv = findViewById(R.id.textView2);

                v = minutiUsate((int)cs1.getProgress(), 15);
                tv.setText(String.valueOf(v[0] == 0 ? "00" : v[0]) + " : " + String.valueOf(v[1] == 0 ? "00" : v[1]));

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
    }

    int[] minutiUsate(int minuta, int podela)
    {
        int tBase = (minuta / podela) * podela;
        if (minuta % podela > 7)
            tBase += podela;

        int[] vreme = new int[2];
        vreme[0] = tBase / 60;
        vreme[1] = tBase % 60;
        return vreme;
    }

    public void remind(View but)
    {
        Intent namera = new Intent(getApplicationContext(), ReminderCreate.class);
        CircularSeekBar cb = findViewById(R.id.cs2);
        int[] min = minutiUsate((int)cb.getProgress(), 15);
        namera.putExtra("vreme", min[0]*60 + min[1]);
        startActivity(namera);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item1:
                if (!this.getClass().equals(MainActivity.class)) {
                    Intent drugaAktivnost = new Intent(this, MainActivity.class);
                    startActivity(drugaAktivnost);
                }
                return true;

            case R.id.item2:
                if (!this.getClass().equals(Reminder.class)) {
                    Intent trecaAktivnost = new Intent(this, Reminder.class);
                    startActivity(trecaAktivnost);
                }
                return true;
        }
        return false;
    }
}
