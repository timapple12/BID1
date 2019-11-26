package com.example.bid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SensorRestarterBroadcastReceiver  extends BroadcastReceiver  {
    private int counter = 0;
    private static boolean wasScreenOn = true;
    MyService myService;


    @Override
    public void onReceive(final Context context, final Intent intent) {


            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

                System.out.println("btn_off");
                wasScreenOn = false;
                ++counter;
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                System.out.println("btn_on");
                wasScreenOn = true;
                ++counter;

            }
            if (counter == 7) {
                System.out.println("work power_Act");

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
