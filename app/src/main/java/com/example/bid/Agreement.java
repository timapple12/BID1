package com.example.bid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Agreement extends AppCompatActivity {
private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        checkBox=(CheckBox)findViewById(R.id.checkBox);

    }
    public void onButton_BeginClick(View v){
        if(checkBox.isChecked()==true){
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);

        }else
        {
            Toast.makeText(getApplicationContext(), "You must agree with policy first", Toast.LENGTH_SHORT).show();
        }
    }
}
