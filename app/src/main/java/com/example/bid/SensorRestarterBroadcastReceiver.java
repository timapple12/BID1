package com.example.bid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SensorRestarterBroadcastReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        context.startService(new Intent(context, VibrationIntentService.class));
        Log.i("It","1st service started ");
       // context.startService(new Intent(context, MyService.class));
        //Log.i("It","2nd service started ");

    }
}
