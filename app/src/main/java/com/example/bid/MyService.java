package com.example.bid;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.media.VolumeProviderCompat;

import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    private MediaSessionCompat mediaSession;
    public static boolean a=true;
    private int count = 0;
    private int count1 = 0;

    public MyService() {
        count1++;
        count1--;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        doing();

    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        startService(new Intent(this,MyService.class));
    }
    public static void realStopService(){
        a=false;
    }
    public void doing(){
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        Log.i("gdf","service started");
        mediaSession = new MediaSessionCompat(this, "PlayerService");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0)
                .build());
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {

                        VolumeProviderCompat myVolumeProvider =
                                new VolumeProviderCompat(VolumeProviderCompat.VOLUME_CONTROL_RELATIVE,0,0) {
                                    @Override
                                    public void onAdjustVolume(int direction) {
                                        if(a==false){
                                            return;
                                        }
                                        if(direction==1||direction==-1){
                                            count++;
                                            Log.i("lol",Integer.toString(count));
                                            if(count==Integer.parseInt(prefs.getString("volume",null))){
                                                count=0;
                                                sendEmail();
                                            }

                                        }
                                    }
                                };

                        mediaSession.setPlaybackToRemote(myVolumeProvider);
                        mediaSession.setActive(a);

                        TimeUnit.MILLISECONDS.sleep(500);

                    } catch (Exception e) {
                    }
                    editor.putBoolean("service",a);
                    editor.apply();
                }
            }
        }).start();
        thread();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("lol","stop service");
        //a=false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, MyService.class));
        }
        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);


    }
    public void sendEmail(){
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);


        if(prefs.getBoolean("r",true)==true){
            String email =prefs.getString("mail",null).trim();
            String subject = "Your child in danger".trim();
            String message = prefs.getString("mail_text",null).trim();
            String msg=message+"\n"+prefs.getString("location",null);
            SendMail sm = new SendMail(this, email, subject, msg);
            sm.execute();
            vibration();

        }else{
            stopService(new Intent(this, GPS_Service.class));
            String email =prefs.getString("mail",null).trim();
            String subject = "Your child in danger".trim();
            String message = prefs.getString("mail_text",null).trim();

            SendMail sm = new SendMail(this, email, subject, message);
            sm.execute();
            vibration2();
        }
    }
    public void vibration2(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(200);
        }
    }
    public void vibration(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }
    public  void thread(){
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(Integer.parseInt(prefs.getString("volume",null))*4000);
                        count=0;
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }
}



