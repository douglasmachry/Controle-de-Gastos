package com.example.iossenac.controlededespesas.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.iossenac.controlededespesas.LocationUpdatesService;

public class StartService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, LocationUpdatesService.class);
            context.startService(pushIntent);
        }
    }
}
