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


        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();


        first_opening=prefs.getBoolean("firstOpening",true);
        if(first_opening==true)
        {
            setContentView(R.layout.activity_agreement);
            checkBox=(CheckBox)findViewById(R.id.checkBox);
            editor.putString("mail", " ");
            editor.putString("mail_text", " ");
            editor.putString("volume", "0");
            editor.putString("numb", "0");
            editor.putString("power", "0");
            editor.putString("latitude","");
            editor.putString("longitude","");
            editor.putString("password"," ");
            editor.putString("password1"," ");
            editor.putString("sendtime","60");

            editor.putBoolean("r", true);
            editor.putBoolean("r1", false);

            editor.apply();
        }
        if (first_opening == false) {
            if (prefs.getString("key", " ").length() == 3) {
                Intent intent = new Intent(this, Password.class);
                startActivity(intent);
            } else {

           Intent intent = new Intent(this, Extendet_settings.class);
            startActivity(intent);
            }
        }

    }
    public void onButton_BeginClick(View v){
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        if(checkBox.isChecked()==true){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            editor.putBoolean("firstOpening",false);
            editor.apply();
        }else
        {
            Toast.makeText(getApplicationContext(), "You must agree with policy first", Toast.LENGTH_SHORT).show();
        }
    }
}
