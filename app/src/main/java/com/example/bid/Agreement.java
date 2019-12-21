package com.example.bid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Agreement extends AppCompatActivity {
    private boolean first_opening=true;
    private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("start");

        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();


        first_opening=prefs.getBoolean("firstOpening",true);
        if(first_opening==true)
        {
            setContentView(R.layout.activity_agreement);
            checkBox=(CheckBox)findViewById(R.id.checkBox);
           /* editor.putString("mail", "");
            editor.putString("mail_text", "");
            editor.putString("volume", "");
            editor.putString("numb", "");
            editor.putString("power", "");*/
            editor.putString("latitude","1");
            editor.putString("longitude","1");
            editor.putString("password","");
            editor.putString("password1","");
            editor.putString("sendtime","30");

            editor.putBoolean("r", true);
            editor.putBoolean("r1", false);

            editor.apply();
        }else
        if (first_opening == false) {
            if(prefs.getString("password","").trim().length()==0||
                    prefs.getString("password1","").trim().length()==0||
                    Integer.parseInt(prefs.getString("volume","0").trim())<5||
                    Integer.parseInt(prefs.getString("power","0").trim())<5||
                    Integer.parseInt(prefs.getString("numb","0").trim())<5||
                    prefs.getString("mail","").trim().length()==0||
                    prefs.getString("mail_text","").trim().length()==0){
                Toast.makeText(getApplicationContext(),"Fill all fields to correct work",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,Settings.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
           Intent intent = new Intent(this, Password.class);
            startActivity(intent);
            }
        }

    }
    public void onButton_BeginClick(View v){
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        if(checkBox.isChecked()==true){
            editor.putBoolean("firstOpening",false);
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }else
        {
            Toast.makeText(getApplicationContext(), "You must agree with policy first", Toast.LENGTH_SHORT).show();

        }
    }
}
