package com.example.bid;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class Main_Seervice extends IntentService {
    private static final String ACTION_FOO = "com.example.bid.action.FOO";
    private static final String ACTION_BAZ = "com.example.bid.action.BAZ";
    private static final String EXTRA_PARAM1 = "com.example.bid.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.bid.extra.PARAM2";

    public Main_Seervice() {
        super("Main_Seervice");
    }

    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, Main_Seervice.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, Main_Seervice.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }
   /* @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("lol","stop service");
        a=false;
        handlerList.removeCallbacks(sendEmail_inThread);
        // googleApiClient.disconnect();
        stopSelf();

    }*/
    private void handleActionFoo(String param1, String param2) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionBaz(String param1, String param2) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
