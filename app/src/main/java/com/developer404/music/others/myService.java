package com.developer404.music.others;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.developer404.music.SongPlayer;
import java.io.IOException;

/* loaded from: classes.dex */
public class myService extends Service {
    String command;
    Binder mbinder = new myservice_ibinder();
    SongPlayer songPlayer_refrence;

    /* loaded from: classes.dex */
    public class myservice_ibinder extends Binder {
        public myservice_ibinder() {
//hiding            myService.this = r1;
        }

        public myService return_muservice() {
            Log.e("debuging", "callback: return_musicservice ");
            return myService.this;
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        Log.e("debugging", "callback: onbind ");
        return this.mbinder;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        Log.e("debugging", "onUnbind: onUnBind");
        return true;
    }

    @Override // android.app.Service
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.e("debugging", "onRebind: onRebind");
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        Log.e(NotificationCompat.CATEGORY_SERVICE, "onStartCommand:service start ");
        String stringExtra = intent.getStringExtra("command");
        this.command = stringExtra;
        stringExtra.hashCode();
        char c = 65535;
        switch (stringExtra.hashCode()) {
            case -1575257234:
                if (stringExtra.equals("play_current")) {
                    c = 0;
                    break;
                }
                break;
            case -570312737:
                if (stringExtra.equals("pauseService")) {
                    c = 1;
                    break;
                }
                break;
            case 3377907:
                if (stringExtra.equals("next")) {
                    c = 2;
                    break;
                }
                break;
            case 3449395:
                if (stringExtra.equals("prev")) {
                    c = 3;
                    break;
                }
                break;
            case 109522647:
                if (stringExtra.equals("sleep")) {
                    c = 4;
                    break;
                }
                break;
            case 1922620715:
                if (stringExtra.equals("play_pause")) {
                    c = 5;
                    break;
                }
                break;
            case 2072332025:
                if (stringExtra.equals("shuffle")) {
                    c = 6;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                try {
                    this.songPlayer_refrence.play_current();
                    this.songPlayer_refrence.notificationFunction();
                    startForeground(121, this.songPlayer_refrence.returnNotification());
                    break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            case 1:
                stopForeground(false);
                break;
            case 2:
                if (this.songPlayer_refrence.MP_position().booleanValue()) {
                    try {
                        this.songPlayer_refrence.next();
                        this.songPlayer_refrence.notificationFunction();
                        startForeground(121, this.songPlayer_refrence.returnNotification());
                        break;
                    } catch (IOException e2) {
                        throw new RuntimeException(e2);
                    }
                } else {
                    this.songPlayer_refrence.onShufflled();
                    Toast.makeText(getApplicationContext(), "completed", Toast.LENGTH_SHORT).show();
                    stopForeground(true);
                    break;
                }
            case 3:
                try {
                    this.songPlayer_refrence.prev();
                    this.songPlayer_refrence.notificationFunction();
                    startForeground(121, this.songPlayer_refrence.returnNotification());
                    break;
                } catch (IOException e3) {
                    throw new RuntimeException(e3);
                }
            case 4:
                if (this.songPlayer_refrence.MP_isPlaying()) {
                    this.songPlayer_refrence.play_pause();
                    stopForeground(false);
                    break;
                }
                break;
            case 5:
                this.songPlayer_refrence.play_pause();
                this.songPlayer_refrence.notificationFunction();
                startForeground(121, this.songPlayer_refrence.returnNotification());
                break;
            case 6:
                try {
                    this.songPlayer_refrence.play_current();
                    this.songPlayer_refrence.shuffle();
                    break;
                } catch (IOException e4) {
                    throw new RuntimeException(e4);
                }
        }
        return Service.START_STICKY;
    }

    public void callback(SongPlayer songPlayer) {
        this.songPlayer_refrence = songPlayer;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // android.app.Service
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
        this.songPlayer_refrence.doOnTaskRemoved();
        this.songPlayer_refrence.releaseMP();
        stopForeground(true);
        stopSelf();
        Log.e("debugging", "onStartCommand:task removed ");
    }
}
