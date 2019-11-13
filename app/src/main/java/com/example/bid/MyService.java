package com.example.bid;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.media.VolumeProviderCompat;

import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    private MediaSessionCompat mediaSession;
    public boolean a=true;
    private int count = 0;


    public MyService() {
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
                                new VolumeProviderCompat(VolumeProviderCompat.VOLUME_CONTROL_RELATIVE,100,50) {
                                    @Override
                                    public void onAdjustVolume(int direction) {
                                        if(direction==1||direction==-1){
                                            count++;
                                            Log.i("lol",Integer.toString(count));
                                            if(count==Integer.parseInt(prefs.getString("volume",null))){
                                                count=0;
                                                EmailSend();
                                            }

                                        }
                                    }
                                };

                        mediaSession.setPlaybackToRemote(myVolumeProvider);
                        mediaSession.setActive(a);
                        editor.putBoolean("service",a);
                        editor.apply();
                        TimeUnit.MILLISECONDS.sleep(500);

                    } catch (Exception e) {
                    }
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
        a=false;
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
            Toast.makeText(getApplicationContext(),"E-mail with geo has been sent",Toast.LENGTH_SHORT).show();
        }else{
            stopService(new Intent(this, GPS_Service.class));
            String email =prefs.getString("mail",null).trim();
            String subject = "Your child in danger".trim();
            String message = prefs.getString("mail_text",null).trim();

            SendMail sm = new SendMail(this, email, subject, message);
            sm.execute();
            Toast.makeText(getApplicationContext(),"E-mail without geo has been sent",Toast.LENGTH_SHORT).show();

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
                        TimeUnit.MILLISECONDS.sleep(Integer.parseInt(prefs.getString("volume",null))*1000);
                        count=0;
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }
    protected void EmailSend(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (a==true) {
                    try {
                        //TimeUnit.MILLISECONDS.sleep(100000);
                        sendEmail();
                        sleep();

                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }
    public void sleep(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
