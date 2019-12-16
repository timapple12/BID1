package com.example.bid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Password extends AppCompatActivity {
    EditText pasw;
    CheckBox pasword;
    TextView forgot;
    int kostyl;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        pasw=(EditText)findViewById(R.id.pasw2);
        pasword=(CheckBox)findViewById(R.id.checkBox22);
        forgot=(TextView)findViewById(R.id.textView252);

        forgot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ++kostyl;
                if(kostyl==2) {
                    Toast.makeText(getApplicationContext(), "Password sent on "+prefs.getString("mail",null).trim(), Toast.LENGTH_SHORT).show();
                    sendEmail();
                    kostyl=0;
                }
                return true;
            }
        });
        pasword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    pasw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    pasw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });


    }
    public void onButton(View v){
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        if(pasw.getText().toString().equals( prefs.getString("password"," "))){
            Intent intent = new Intent(this, Main_Window.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"Enter right password",Toast.LENGTH_SHORT).show();
        }

    }

    public void sendEmail(){
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);


            String email =prefs.getString("mail",null).trim();
            String subject = "Your password".trim();
            String message = prefs.getString("password",null).trim();

            SendMail sm = new SendMail(this, email, subject, message);
            sm.execute();


    }
}
