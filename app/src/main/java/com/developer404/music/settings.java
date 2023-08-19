package com.developer404.music;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import com.developer404.music.databinding.ActivitySettingsBinding;
import com.developer404.music.others.myService;

/* loaded from: classes.dex */
public class settings extends AppCompatActivity implements View.OnClickListener {
    ActivitySettingsBinding binding;
    CountDownTimer countDownTimer;
    Dialog dialog_sleep_timer;
    TextView min120;
    TextView min15;
    TextView min30;
    TextView min45;
    TextView min60;
    NotificationManagerCompat notificationManagerCompat;
    int sleep_time;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivitySettingsBinding inflate = ActivitySettingsBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("appTheme", 0);
        getWindow().setBackgroundDrawableResource(sharedPreferences.getInt("themeResourceID", 0));
        getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        if (sharedPreferences.getInt("themeResourceID", 0) == R.color.white) {
            this.binding.settingTB.setTitleTextColor(getResources().getColor(R.color.dark));
        } else {
            this.binding.settingTB.setTitleTextColor(getResources().getColor(R.color.white));
        }
        this.notificationManagerCompat = NotificationManagerCompat.from(this);
        Dialog dialog = new Dialog(this);
        this.dialog_sleep_timer = dialog;
        dialog.setContentView(R.layout.dialog_sleep_timer);
        this.dialog_sleep_timer.setCanceledOnTouchOutside(false);
        this.min15 = (TextView) this.dialog_sleep_timer.findViewById(R.id.min15);
        this.min30 = (TextView) this.dialog_sleep_timer.findViewById(R.id.min30);
        this.min45 = (TextView) this.dialog_sleep_timer.findViewById(R.id.min45);
        this.min60 = (TextView) this.dialog_sleep_timer.findViewById(R.id.min60);
        this.min120 = (TextView) this.dialog_sleep_timer.findViewById(R.id.min120);
        this.min15.setOnClickListener(this);
        this.min30.setOnClickListener(this);
        this.min45.setOnClickListener(this);
        this.min60.setOnClickListener(this);
        this.min120.setOnClickListener(this);
        this.binding.settingEqualizer.setOnClickListener(this);
        this.binding.sleepTimerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.developer404.music.settings.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    settings.this.dialog_sleep_timer.show();
                } else {
                    settings.this.countDownTimer.cancel();
                }
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.setting_equalizer) {
            switch (id) {
                case R.id.min120 /* 2131231025 */:
                    this.sleep_time = 7200000;
                    fun_sleep_timer();
                    this.countDownTimer.start();
                    this.dialog_sleep_timer.dismiss();
                    return;
                case R.id.min15 /* 2131231026 */:
                    this.sleep_time = 900000;
                    fun_sleep_timer();
                    this.countDownTimer.start();
                    this.dialog_sleep_timer.dismiss();
                    return;
                case R.id.min30 /* 2131231027 */:
                    this.sleep_time = 1800000;
                    fun_sleep_timer();
                    this.countDownTimer.start();
                    this.dialog_sleep_timer.dismiss();
                    return;
                case R.id.min45 /* 2131231028 */:
                    this.sleep_time = 2700000;
                    fun_sleep_timer();
                    this.countDownTimer.start();
                    this.dialog_sleep_timer.dismiss();
                    return;
                case R.id.min60 /* 2131231029 */:
                    this.sleep_time = 3600000;
                    fun_sleep_timer();
                    this.countDownTimer.start();
                    this.dialog_sleep_timer.dismiss();
                    return;
                default:
                    return;
            }
        }
        Intent intent = new Intent("android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL");
        intent.putExtra("android.media.extra.AUDIO_SESSION", SongPlayer.MP.getAudioSessionId());
        intent.putExtra("android.media.extra.PACKAGE_NAME", getPackageName());
        intent.putExtra("android.media.extra.CONTENT_TYPE", 0);
        startActivity(intent);
    }

    public void fun_sleep_timer() {
        this.countDownTimer = new CountDownTimer(this.sleep_time, 10L) { // from class: com.developer404.music.settings.2
            @Override // android.os.CountDownTimer
            public void onTick(long j) {
                settings.this.binding.sleepTimerSwitch.setChecked(true);
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                Intent intent = new Intent(settings.this, myService.class);
                intent.putExtra("command", "sleep");
                settings.this.startService(intent);
                settings.this.binding.sleepTimerSwitch.setChecked(false);
            }
        };
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(131072);
        startActivity(intent);
    }
}
