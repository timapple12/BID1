package com.example.bid;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.media.VolumeProviderCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private MediaSessionCompat mediaSession;
    public static boolean a=true;
    private int count = 0;
    FusedLocationProviderClient fusedLocationClient;
    private Handler handlerList=new Handler();
    private SharedPreferences prefs1;
    double latitude1;
    double longitude1;

    private GoogleApiClient googleApiClient;

    public MyService() {
    }
   @Override
    public void onCreate() {

       if (googleApiClient != null) {
           googleApiClient.connect();
       }
        super.onCreate();
       final SharedPreferences prefs = this.getSharedPreferences(
               "com.example.bid", Context.MODE_PRIVATE);
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
                        if(prefs.getBoolean("r",true)==true){
                            startGps();
                        }else if(prefs.getBoolean("r",true)==false){
                            stopGps();
                        }
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

                                                handlerList.postDelayed(sendEmail_inThread, 1);
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
        a=false;
        handlerList.removeCallbacks(sendEmail_inThread);
       // googleApiClient.disconnect();
        stopSelf();

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

    //  Написати логіку збереження даних з edittext час між відсиланням повідомлення
    private Runnable sendEmail_inThread=new Runnable() {


        @Override
        public void run() {
            
            if(Integer.parseInt(prefs1.getString("sendtime", null))>=5) {
                sendEmail();
                sendSMS(prefs1.getString("numb",null),prefs1.getString("mail_text",null));
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

    @Override
    public void onStart(Intent intent, int startId) {
        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
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
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // super.onStartCommand(intent, flags, startId);
        prefs1 = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        doing();
        return START_STICKY;
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
