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

public class Password2 extends AppCompatActivity {
    EditText pasw;
    CheckBox pasword;
    TextView forgot;
    int kostyl;
    public void onClickBtn(View v){
        startActivity(new Intent(this,Main_Window.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password2);
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
        if(pasw.getText().toString().equals( prefs.getString("password1",""))){
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else{
            Toast.makeText(getApplicationContext(),"Enter the right password",Toast.LENGTH_SHORT).show();
        }
    }
    public void sendEmail(){
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);


        String email =prefs.getString("mail",null).trim();
        String subject = "Someone wanna  enter to settings".trim();
        String message = "Parent's password\n"+prefs.getString("password1","").trim();

        SendMail sm = new SendMail(this, email, subject, message);
        sm.execute();


    }
}

