package com.example.bid;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Main_Window extends AppCompatActivity {
    SharedPreferences prefs1;
    Context context;
    ToggleButton toggleButton;
    private int RequestCode=100;
    private PopupWindow mPopupWindow;
    private Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*if(prefs1.getString("password","111111").trim().length()==6||
                prefs1.getString("password1","111111").trim().length()==6||
                Integer.parseInt(prefs1.getString("volume","0").trim())<=5||
                Integer.parseInt(prefs1.getString("power","0").trim())<=5||
                Integer.parseInt(prefs1.getString("numb","0").trim())<=5||
                prefs1.getString("mail","111111").trim().length()==6||
                prefs1.getString("mail_text","111111").trim().length()==6){
            Toast.makeText(getApplicationContext(),"Fill all fields to correct work",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,Settings.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }*/
        prefs1 = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);

                setContentView(R.layout.activity_main__window);

        toggleButton=(ToggleButton)findViewById(R.id.toggleButton);

        if(!runtime_permissions())
        {
            enable_btn();
        }

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
        if(toggleButton.isChecked()==false)
        {
            onTogglePressOff();
        }
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
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

        startService(new Intent(getBaseContext(), MyService.class));
        if(prefs.getBoolean("r",true)==true){
            startService(new Intent(getBaseContext(), GPS_Service.class));
        }

    }


    public void onBtnHelpClick(View v){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"babyindangerapp@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
    public void btn(View v){
    mContext = getApplicationContext();
    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

    // Inflate the custom layout/view
    View customView = inflater.inflate(R.layout.custom,null);

    mPopupWindow = new PopupWindow(
            customView,
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
    );
}

    public void onTogglePressOff(){
        MyService m =new MyService();
        m.stopSelf();
        Intent myService = new Intent(Main_Window.this, MyService.class);
        stopService(myService);
       stopService(new Intent(this, GPS_Service.class));
    }
   public void onButton_Settings(View v){
        Intent intent = new Intent(this,Password2.class);
        startActivity(intent);
       overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},RequestCode);

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


