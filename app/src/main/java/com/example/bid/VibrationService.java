package com.example.bid;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.squareup.seismic.ShakeDetector;

public class VibrationService extends IntentService implements ShakeDetector.Listener {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    Handler handlerList = new Handler();
    protected Handler handler;
    private boolean a = true;


    public VibrationService(String name) {
        super(name);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    @Override
    public int onStartCommand(Intent intent,
                              int flags, int startId) {
       super.onStartCommand(intent, flags, startId);
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
       on();
        return android.app.Service.START_STICKY;

    }

    private Runnable sendEmail_inThread = new Runnable() {
        @Override
        public void run() {
            on();
        }
    };

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        on();
    }

    public void on() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);
    }

    public void vibration() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(300);
            if (a == false) {
                v.cancel();
            }
        }
    }

    public void hearShake() {
        vibration();
        Log.i("l", "dude, lol, this shit  workkk");

    }

  /*  @Override
    public void onDestroy() {
        startService(new Intent(getBaseContext(), VibrationService.class));
        super.onDestroy();
        Log.i("It","it's pretty cool ");
        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);
        sendBroadcast(broadcastIntent);
        /*a=false;
        stopSelf();
        Log.i("It","service stopped in five's seconds before planing of family holiday present, is's all was a ");
       // startService(new Intent(this,VibrationService.class));
    }*/
}