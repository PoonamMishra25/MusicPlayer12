package com.example.musicplayer.Notification;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationActionService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("Tracks")
                .putExtra("actionName",intent.getAction()));
        Toast.makeText(context, ""+intent.getAction(), Toast.LENGTH_SHORT).show();
    }
}
