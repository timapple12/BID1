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


        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);


        first_opening=prefs.getBoolean("firstOpening",true);
        if(first_opening==true)
        {
            setContentView(R.layout.activity_agreement);
            checkBox=(CheckBox)findViewById(R.id.checkBox);
        }
        if (first_opening == false) {
            if (prefs.getString("key", null).length() == 3) {
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
      /*  if(checkBox.isChecked()==true){
            startActivity(new Intent(this,MainActivity.class));
        }else{
            Toast.makeText(getApplicationContext(),"lol",Toast.LENGTH_SHORT).show();
        }*/

    }
}
