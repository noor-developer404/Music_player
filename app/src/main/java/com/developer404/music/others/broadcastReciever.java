package com.developer404.music.others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* loaded from: classes.dex */
public class broadcastReciever extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Intent intent2 = new Intent(context, myService.class);
        String action = intent.getAction();
        action.hashCode();
        char c = 65535;
        switch (action.hashCode()) {
            case -971121397:
                if (action.equals("PLAY_PAUSE")) {
                    c = 0;
                    break;
                }
                break;
            case 2392819:
                if (action.equals("NEXT")) {
                    c = 1;
                    break;
                }
                break;
            case 2464307:
                if (action.equals("PREV")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                intent2.putExtra("command", "play_pause");
                context.startService(intent2);
                return;
            case 1:
                intent2.putExtra("command", "next");
                context.startService(intent2);
                return;
            case 2:
                intent2.putExtra("command", "prev");
                context.startService(intent2);
                return;
            default:
                return;
        }
    }
}
