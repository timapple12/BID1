package com.example.bid;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.media.VolumeProviderCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.TimeUnit;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks  {

    private MediaSessionCompat mediaSession;
    private MediaPlayer mediaPlayer;
    public static boolean a = true;
    private int count = 0;
    FusedLocationProviderClient fusedLocationProviderClient;
    private Handler handlerList = new Handler();
    private SharedPreferences prefs1;
    double latitude1;
    double longitude1;
    int powerTextView=0;
    private BroadcastReceiver mReceiver;
    private GoogleApiClient googleApiClient;
    private boolean activated;

    public MyService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onCreate() {

        super.onCreate();
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        if(prefs.getString("latitude","1").trim().length()==1){
            notRefreshedData();
        }
        mediaSession = new MediaSessionCompat(this, "PlayerService");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0)
                .build());
       mediaPlayer=MediaPlayer.create(this,R.raw.sound);
        prefs1 = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(prefs.getString("latitude","1").trim().length()==1){
                    notRefreshedData();
                }
                doing();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                filter.addAction(Intent.ACTION_SCREEN_OFF);
                filter.addAction(Intent.ACTION_USER_PRESENT);
                mReceiver = new SensorRestarterBroadcastReceiver();
                registerReceiver(mReceiver, filter);

            }
        }).start();
       if(prefs.getBoolean("r",true)==true){
           startGps();
       }else if(prefs.getBoolean("r",true)==false){
           stopGps();
       }
    }
    public void startGps(){
        startService(new Intent(this,GPS_Service.class));
    }
    public void stopGps(){
        stopService(new Intent(this,GPS_Service.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void doing(){
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        Log.i("gdf","service started");
        powerTextView=Integer.parseInt(prefs.getString("power",null));
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    if(prefs.getString("spinn1","").length()==0) {

                    if(prefs.getString("latitude","1").trim().length()==1){
                        notRefreshedData();
                    }else
                        latitude1=Double.parseDouble(prefs.getString("latitude",null));
                        longitude1=Double.parseDouble(prefs.getString("longitude",null));
                    double angular_distance;
                    angular_distance=(0.88)*1000*111.2 * Math.sqrt( (longitude1 - prefs1.getFloat("longitude2",0))*
                            (longitude1 - prefs1.getFloat("longitude2",0)) + (latitude1-prefs1.getFloat("latitude2",0))*
                            Math.cos(Math.PI*longitude1/180)*(latitude1-prefs1.getFloat("latitude2",0))*Math.cos(Math.PI*longitude1/180));
                    System.out.println(angular_distance);
                    System.out.println(prefs.getFloat("spinn", 0));

                        if (angular_distance > prefs.getFloat("spinn", 0)) {
                            System.out.println("has been went out");
                            sendEmail2();
                        }
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if(prefs.getBoolean("r",true)==true){
                            startGps();
                        }else if(prefs.getBoolean("r",true)==false){
                            stopGps();
                        }
                        if(Integer.parseInt(prefs1.getString("count",null))==Integer.parseInt(prefs1.getString("power",null))){
                            handlerList.postDelayed(sendEmail_inThread, 1);
                            editor.putString("count","0");
                           editor.apply();
                        }


                        TimeUnit.MILLISECONDS.sleep(500);

                    } catch (Exception e) {
                    }
                    editor.putBoolean("service",a);
                    editor.apply();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                                        activated=false;
                                        handlerList.postDelayed(sendEmail_inThread, 1);
                                    }

                                }
                            }
                        };

                mediaSession.setPlaybackToRemote(myVolumeProvider);
                mediaSession.setActive(a);
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
        if(activated==false){
            sendEmail3();
        }
        Log.i("lol","stop service");
        a=false;
        mediaPlayer.stop();
        handlerList.removeCallbacks(sendEmail_inThread);
        handlerList.removeCallbacks(sendEmail_inThread2);
        if(mReceiver!=null)
        {
            unregisterReceiver(mReceiver);
        }
        stopSelf();

    }

    public void sendEmail2(){
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);

        String email =prefs.getString("mail",null).trim();
        String subject = "Your child have been leaved the zone".trim();
        String message = "Your child have been leaved the zone".trim();

        SendMail sm = new SendMail(this, email, subject, message);
        sm.execute();


    }
    public void sendEmail3(){
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);


        String email =prefs.getString("mail",null).trim();
        String subject = "The security mode have been disabled".trim();
        String message = "I'm in safe".trim();

        SendMail sm = new SendMail(this, email, subject, message);
        sm.execute();


    }

    public void sendEmail(){
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        if(prefs.getString("latitude",null)==null){
            editor.putString("latitude",Double.toString(latitude1));
            editor.putString("longitude",Double.toString(longitude1));
            editor.apply();
        }

        if(prefs.getBoolean("r",true)==true){
            String email =prefs.getString("mail",null).trim();
            String subject = "Your child in danger".trim();
            final String message = prefs.getString("mail_text",null).trim();
            final String msg ;

                msg = message + "\n" + "coordinates: " + prefs.getString("latitude", null) + "  "
                        + prefs.getString("longitude", null)
                        + "\n" + "https://www.google.com/maps/place/" + prefs.getString("latitude", null)
                        + "N" + prefs.getString("longitude", null)
                        + "E";

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(200);
        }
    }
    private Runnable sendEmail_inThread2=new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public void run() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                doing();
            }
        }
    };
    //  Написати логіку збереження даних з edittext час між відсиланням повідомлення
    private Runnable sendEmail_inThread=new Runnable() {


        @Override
        public void run() {
            System.out.println(prefs1.getString("count",null));
            if(prefs1.getString("latitude","1").trim().length()==1){
                notRefreshedData();
            }
            if(Integer.parseInt(prefs1.getString("sendtime", null))>=5) {
                sendEmail();
                mediaPlayer.start();
                if(prefs1.getBoolean("r",true)==true){
                    String email =prefs1.getString("mail",null).trim();
                    String subject = "Your child in danger".trim();
                    final String message = prefs1.getString("mail_text",null).trim();
                    String msg ;

                    msg = message + "\n" + "coordinates: " + prefs1.getString("latitude", null) + "  "
                            + prefs1.getString("longitude", null)
                            + "\n" + "https://www.google.com/maps/place/" + prefs1.getString("latitude", null)
                            + "N" + prefs1.getString("longitude", null)
                            + "E";

                    sendSMS(prefs1.getString("numb",null),msg);

                }else{
                    sendSMS(prefs1.getString("numb",null),prefs1.getString("mail_text",null));

                }

                handlerList.postDelayed(sendEmail_inThread,
                        Integer.parseInt(prefs1.getString("sendtime", null)) * 1000);
            }else{
                Toast.makeText(getApplicationContext(),"Enter time of resending e-mail above 5",Toast.LENGTH_SHORT).show();
            }
        }
    };
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
    protected void notRefreshedData(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        Task<Location> task=fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    editor.putString("latitude",Double.toString(location.getLatitude()));
                    editor.putString("longitude",Double.toString(location.getLongitude()));
                    editor.apply();
                }
            }
        });
    }

    public void sendSMS(String phoneNo, String msg) {                                  //SMS sending
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
            latitude1=lat;
            longitude1=lon;

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


}
