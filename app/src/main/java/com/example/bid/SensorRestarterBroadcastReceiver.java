package com.example.bid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SensorRestarterBroadcastReceiver  extends BroadcastReceiver  {
    private int counter = 0;
    private static boolean wasScreenOn = true;
    @Override
    public void onReceive(final Context context, final Intent intent) {

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                ++counter;
                System.out.println(counter);
                wasScreenOn = false;

            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                ++counter;
                System.out.println(counter);
                wasScreenOn = true;


            }
            if (counter ==7) {

                System.out.println("power_Act");

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
