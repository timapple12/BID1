package com.example.bid;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Main_Window extends AppCompatActivity {
    ToggleButton toggleButton;
    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onResume() {
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    editor.putString("location",""+intent.getExtras().get("coordinates"));
                    editor.apply();

                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final MyService m=new MyService();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__window);
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        toggleButton=(ToggleButton)findViewById(R.id.toggleButton);
        toggleButton.setChecked(prefs.getBoolean("service",true));
        if(!runtime_permissions())
            enable_btn();

    }
    public void enable_btn(){
        if(toggleButton.isChecked()==true){
            onTogglePressOn();
        }
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                MyService m=new MyService();
                if(isChecked==true){
                    m.a=true;
                    onTogglePressOn();

                }else if(isChecked==false){
                    onTogglePressOff();
                    m.a=false;
                }

            }
        });
    }
    public void onTogglePressOn(){
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        startService(new Intent(this, VibrationService.class));
        startService(new Intent(this, MyService.class));
        if(prefs.getBoolean("r",true)==true){
            startService(new Intent(this, GPS_Service.class));
        }

    }

    public void onTogglePressOff(){
        Intent myService = new Intent(Main_Window.this, MyService.class);
        stopService(myService);
       stopService(new Intent(this, GPS_Service.class));
    }
   public void onButton_Settings(View v){
        Intent intent = new Intent(this,Settings.class);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }
    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_btn();
            }else {
                runtime_permissions();
            }
        }
    }
    }


