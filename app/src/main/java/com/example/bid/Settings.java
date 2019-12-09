package com.example.bid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Settings extends AppCompatActivity {
    EditText mail;
    EditText mail_text;
    EditText powerAct;
    EditText volumeAct;
    String mailStr;
    String mailStr_text;
    RadioButton r;
    RadioButton r1;
    View v,v2;
    EditText phoneNumb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
        }
        initializeView();
        phoneNumb=(EditText)findViewById(R.id.editText6);
        r=(RadioButton)findViewById(R.id.wthgeo);
        r1=(RadioButton)findViewById(R.id.nogeo);
        mail=(EditText)findViewById(R.id.editText2);
        mail_text=(EditText)findViewById(R.id.editText3);
        powerAct=(EditText)findViewById(R.id.editText5);
        volumeAct=(EditText)findViewById(R.id.editText8);
        r.setChecked(prefs.getBoolean("r",true));
        r1.setChecked(prefs.getBoolean("r1",true));
        volumeAct.setText(prefs.getString("volume",null));

        powerAct.setText(prefs.getString("power",null));
        phoneNumb.setText(prefs.getString("numb",null));



        mail.setText(prefs.getString("mail",null));
        mail_text.setText(prefs.getString("mail_text",null));

        v.setOnTouchListener(new OnSwipeTouchListener(Settings.this){
                public void onSwipeTop() {
                    Log.i("Settings","top");
                }
                public void onSwipeRight() {
                    if(mail.getText().toString().trim().length()==0||
                        mail_text.getText().toString().trim().length()==0||
                        phoneNumb.getText().toString().trim().length()==0||
                        volumeAct.getText().toString().trim().length()==0||
                        powerAct.getText().toString().trim().length()==0||
                        prefs.getString("sendtime",null).trim().length()==0)
                    {
                        Toast.makeText(getApplicationContext(),"Enter all values first",Toast.LENGTH_SHORT).show();
                    }else if(Integer.parseInt(volumeAct.getText().toString())<5||Integer.parseInt(volumeAct.getText().toString())>20){
                        Toast.makeText(getApplicationContext(),"Enter 'click on power' grather than 5 and below 20",Toast.LENGTH_SHORT).show();
                    }else if(Integer.parseInt(powerAct.getText().toString())<5||Integer.parseInt(powerAct.getText().toString())>10){
                        Toast.makeText(getApplicationContext(),"Enter 'click on power' grather than 5 and below 10",Toast.LENGTH_SHORT).show();

                    }else
                    {
                        Log.i("Settings", "right");
                        mailStr = mail.getText().toString().trim();
                        mailStr_text = mail_text.getText().toString().trim();
                        editor.putString("mail", mailStr.trim());
                        editor.putString("mail_text", mailStr_text.trim());
                        editor.putString("volume", volumeAct.getText().toString().trim());
                        editor.putString("numb", phoneNumb.getText().toString().trim());
                        editor.putString("power", powerAct.getText().toString().trim());
                        editor.putBoolean("r", r.isChecked());
                        editor.putBoolean("r1", r1.isChecked());
                        editor.putInt("power1",Integer.parseInt(powerAct.getText().toString()));
                        editor.apply();
                        openActivity2();
                    }
                }
                public void onSwipeLeft() {
                    Log.i("Settings","left");
                    mailStr = mail.getText().toString().trim();
                    mailStr_text = mail_text.getText().toString().trim();
                    editor.putString("mail", mailStr.trim());
                    editor.putString("mail_text", mailStr_text.trim());
                    editor.putString("volume", volumeAct.getText().toString().trim());
                    editor.putString("numb", phoneNumb.getText().toString().trim());
                    editor.putString("power", powerAct.getText().toString().trim());
                    editor.putBoolean("r", r.isChecked());
                    editor.putBoolean("r1", r1.isChecked());
                    editor.apply();
                    openExt();
                }
                public void onSwipeBottom() {
                    Log.i("Settings","bottom");
                }
            });

    }


    public void openActivity2() {
        Intent intent = new Intent(this,Main_Window.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    public void openExt() {
        Intent intent = new Intent(this,Extendet_settings.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void initializeView(){
        v=(View)findViewById(R.id.view);
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

            }else {
                runtime_permissions();
            }
        }
    }

}
