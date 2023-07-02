package com.example.myloginapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MediaPlayerBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            MainActivity2 mainActivity2 = MainActivity2.getInstance();
            mainActivity2.handleAction(intent.getAction());
        }
    }
}

