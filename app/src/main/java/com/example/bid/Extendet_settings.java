package com.example.bid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Extendet_settings extends AppCompatActivity {
    EditText timeBetweenSending;
    EditText pasw;
    View v2;

    private static final int Request_Code=101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extendet_settings);
        timeBetweenSending=(EditText)findViewById(R.id.editText);
        timeBetweenSending.setText(prefs.getString("sendtime",null));
        pasw=(EditText)findViewById(R.id.editText7);
        pasw.setText(prefs.getString("password",null));
        v2=(View)findViewById(R.id.view2);
        final Intent intent = new Intent(this, Settings.class);
            v2.setOnTouchListener(new OnSwipeTouchListener(Extendet_settings.this) {
                public void onSwipeRight() {
                    if(timeBetweenSending.getText().toString().trim().length()==0||
                    pasw.getText().toString().trim().length()==0){
                        Toast.makeText(getApplicationContext(),"Fill the edittext",Toast.LENGTH_SHORT).show();
                    }else
                    if(Integer.parseInt(timeBetweenSending.getText().toString())>=5) {

                    Log.i("lol",timeBetweenSending.getText().toString());
                    System.out.println("haahahahah");
                    System.out.println(timeBetweenSending.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    editor.putString("key","kek");
                    editor.putString("password",pasw.getText().toString());
                    editor.putString("sendtime", timeBetweenSending.getText().toString());
                    editor.apply();
                    }else{
                         Toast.makeText(getApplicationContext(),"The number must be greater than 5",Toast.LENGTH_SHORT).show();
                    }
                }

            });

    }


}
