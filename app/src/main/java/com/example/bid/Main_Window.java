package com.example.bid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Main_Window extends AppCompatActivity {
    public double latn;
    public double lon;

    SharedPreferences prefs1;

    Context context;
    ToggleButton toggleButton;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs1 = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);

      //  power=Integer.parseInt(prefs.getString("power",null));
        super.onCreate(savedInstanceState);

                setContentView(R.layout.activity_main__window);

        toggleButton=(ToggleButton)findViewById(R.id.toggleButton);
        if(!runtime_permissions())
        {
            enable_btn();
        }

    }
    public int power;
    public void callingPower(){

        power=prefs1.getInt("power1",0);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void enable_btn()
    {
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        toggleButton.setChecked(prefs.getBoolean("service",true));
        final SharedPreferences.Editor editor = prefs.edit();
       // toggleButton.setChecked(prefs.getBoolean("toggle",true));
        if(toggleButton.isChecked()==true)
        {
            onTogglePressOn();
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                MyService m=new MyService();
                if(isChecked==true){
                    m.a=true;
                    editor.putBoolean("toggle",isChecked);
                    editor.apply();
                    onTogglePressOn();


                }else if(isChecked==false){
                   // m.stopSelf();
                    onTogglePressOff();
                    editor.putBoolean("toggle",isChecked);
                    editor.apply();
                    m.a=false;
                }

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onTogglePressOn(){
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
       // startService(new Intent(getBaseContext(),VibrationIntentService.class));
        startService(new Intent(getBaseContext(), MyService.class));
        if(prefs.getBoolean("r",true)==true){
            startService(new Intent(getBaseContext(), GPS_Service.class));
        }

    }


    public void onTogglePressOff(){
        MyService m =new MyService();
      //  m.stopSelf();
        Intent myService = new Intent(Main_Window.this, MyService.class);
        stopService(myService);
       // stopService(new Intent(this, VibrationService.class));
       stopService(new Intent(this, GPS_Service.class));
    }
   public void onButton_Settings(View v){
        Intent intent = new Intent(this,Settings.class);
        startActivity(intent);
       overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);



    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
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


