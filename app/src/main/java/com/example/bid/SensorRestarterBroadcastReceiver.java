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
        final SharedPreferences.Editor editor = prefs.edit();
        new Thread((new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter = 0;
            }
        })).start();
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                ++counter;
                editor.putString("count",Integer.toString(counter));
                editor.apply();
                System.out.println(counter);
                wasScreenOn = false;

            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                ++counter;
                System.out.println(counter);
            }
            if (counter ==prefs.getInt("power1",0)) {
                System.out.println("powerAct");
                counter=0;


            }

        }

}
