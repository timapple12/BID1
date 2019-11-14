package com.example.bid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {
    EditText mail;
    EditText mail_text;
    EditText countShake;
    EditText powerAct;
    EditText volumeAct;
    EditText textnull;
    String mailStr;
    String mailStr_text;
    RadioButton r;
    RadioButton r1;
    View v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializeView();
        r=(RadioButton)findViewById(R.id.wthgeo);
        r1=(RadioButton)findViewById(R.id.nogeo);
        mail=(EditText)findViewById(R.id.editText2);
        mail_text=(EditText)findViewById(R.id.editText3);
        countShake=(EditText)findViewById(R.id.editText4);
        powerAct=(EditText)findViewById(R.id.editText5);
        volumeAct=(EditText)findViewById(R.id.editText8);
        r.setChecked(prefs.getBoolean("r",true));
        r1.setChecked(prefs.getBoolean("r1",true));
        volumeAct.setText(prefs.getString("volume",null));
        countShake.setText(prefs.getString("shake",null));
        if(r.isChecked()==true){

        }
       /* r.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                if(isChecked==true){
                    editor.putBoolean("r",true);
                    editor.putBoolean("r1",false);
                    editor.apply();
                }else if(isChecked==false){
                    editor.putBoolean("r",false);
                    editor.putBoolean("r1",true);
                    editor.apply();

                }

            }
        });*/

        mail.setText(prefs.getString("mail",null));
        mail_text.setText(prefs.getString("mail_text",null));

        v.setOnTouchListener(new OnSwipeTouchListener(Settings.this){
                public void onSwipeTop() {
                    Log.i("Settings","top");
                }
                public void onSwipeRight() {

                    Log.i("Settings","right");
                    mailStr=mail.getText().toString();
                    mailStr_text=mail_text.getText().toString();
                    editor.putString("mail",mailStr);
                    editor.putString("mail_text",mailStr_text);
                    editor.putString("shake",countShake.getText().toString());
                    editor.putString("volume",volumeAct.getText().toString());
                    editor.putBoolean("r",r.isChecked());
                    editor.putBoolean("r1",r1.isChecked());
                    editor.apply();
                    if(countShake.getText().toString().trim()==null||volumeAct.getText().toString().trim()==null
                            ||mail_text.getText().toString().trim()==null||mail.getText().toString().trim()==null){
                        Toast.makeText(getApplicationContext(),"Fill in the blank fields",Toast.LENGTH_SHORT).show();
                    }else{
                        openActivity2();
                    }


                }
                public void onSwipeLeft() {
                    Log.i("Settings","left");

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
    }
    public void openExt() {
        Intent intent = new Intent(this,Extendet_settings.class);
        startActivity(intent);
    }
    private void initializeView(){
        v=(View)findViewById(R.id.view);
    }


}
