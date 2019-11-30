package com.example.bid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SensorRestarterBroadcastReceiver  extends BroadcastReceiver  {
    private int counter = 0;
    SharedPreferences prefs;
    private static boolean wasScreenOn = true;
    @Override
    public void onReceive(final Context context, final Intent intent) {
         prefs = context.getSharedPreferences("com.example.bid",
                Context.MODE_PRIVATE);
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                ++counter;
                System.out.println(counter);
                wasScreenOn = false;

            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                ++counter;
                System.out.println(counter);
                wasScreenOn = true;

            }
            if (counter ==prefs.getInt("power1",0)) {
                System.out.println("powerAct");


            }
        }


    public void counterToZero(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    counter = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
