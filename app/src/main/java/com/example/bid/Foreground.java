package com.example.bid;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class Foreground extends IntentService {

    public Foreground(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent2 = new Intent(this,VibrationService.class);
        startActivity(intent2);
    }
}
