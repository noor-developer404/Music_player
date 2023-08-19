package com.developer404.music;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import com.developer404.music.databinding.ActivitySongPlayerBinding;
import com.developer404.music.others.broadcastReciever;
import com.developer404.music.others.model;
import com.developer404.music.others.myService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class SongPlayer extends AppCompatActivity implements View.OnClickListener {
    static MediaPlayer MP = new MediaPlayer();
    LinearLayout LL_addToPlaylist;
    LinearLayout LL_delete;
    LinearLayout LL_sleepTimer;
    Notification.Action action1;
    Notification.Action action3;
    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    AudioManager audioManager;
    ActivitySongPlayerBinding binding;
    ServiceConnection connection;
    model current_song;
    Dialog dialog_addToPlaylist;
    ListView dialog_addToPlaylist_lv;
    Dialog dialog_moreOption;
    SharedPreferences.Editor editor;
    String fromItsCome;
    String fromWhichFragment;
    MediaSession mediaSession;
    Intent next_intent;
    PendingIntent next_pi;
    Notification notification;
    NotificationManagerCompat notificationManagerCompat;
    PendingIntent pi;
    Intent play_intent;
    PendingIntent play_pi;
    SharedPreferences playlist_songs_sp;
    int position;
    Intent prev_intent;
    PendingIntent prev_pi;
    Intent service_Intent;
    PlaybackState.Builder state;
    Intent to_SongPlayer;
    UpdateSeekbar updateSeekbar;
    ArrayList<model> songsList = new ArrayList<>();
    myService my_service = new myService();
    int START_SERVICE_FLAG = 0;
    Handler handler1 = new Handler();
    ArrayList<model> favSongs_arraylist = new ArrayList<>();
    Gson gson = new Gson();
    int disconnected = 0;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivitySongPlayerBinding inflate = ActivitySongPlayerBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        binding.songPlayerLoop.setOnClickListener(this);
        this.binding.songPlayerLove.setOnClickListener(this);
        this.binding.songPlayerMoreOptions.setOnClickListener(this);
        this.binding.songPlayerPrev.setOnClickListener(this);
        this.binding.songPlayerPlay.setOnClickListener(this);
        this.binding.songPlayerNext.setOnClickListener(this);
        this.notificationManagerCompat = NotificationManagerCompat.from(this);
        this.updateSeekbar = new UpdateSeekbar();
        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Dialog dialog = new Dialog(this);
        this.dialog_moreOption = dialog;
        dialog.setContentView(R.layout.dialog_more_option);
        this.dialog_moreOption.getWindow().setGravity(80);
        this.LL_addToPlaylist = (LinearLayout) this.dialog_moreOption.findViewById(R.id.dialog_more_addToPlaylist);
        this.LL_sleepTimer = (LinearLayout) this.dialog_moreOption.findViewById(R.id.dialog_more_sleepTimer);
        this.LL_delete = (LinearLayout) this.dialog_moreOption.findViewById(R.id.dialog_more_delete);
        this.LL_addToPlaylist.setOnClickListener(this);
        this.LL_sleepTimer.setOnClickListener(this);
        this.LL_delete.setOnClickListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences("playlist_songs", 0);
        this.playlist_songs_sp = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        this.editor = edit;
        edit.apply();
        this.audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() { // from class: com.developer404.music.SongPlayer.1
            @Override // android.media.AudioManager.OnAudioFocusChangeListener
            public void onAudioFocusChange(int i) {
                if (i == 1) {
                    Toast.makeText(SongPlayer.this, "focus gain", Toast.LENGTH_SHORT).show();
                    if (SongPlayer.MP.isPlaying()) {
                        return;
                    }
                    SongPlayer.MP.start();
                } else if (i == -1) {
                    Toast.makeText(SongPlayer.this, "focus loss", Toast.LENGTH_SHORT).show();
                    if (!SongPlayer.MP.isPlaying()) {
                        return;
                    }
                    SongPlayer.this.play_pause();
                } else if (i == -2) {
                    SongPlayer.MP.pause();
                } else if (i != 2) {
                } else {
                    SongPlayer.MP.start();
                }
            }
        };
        this.binding.songPlayerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.developer404.music.SongPlayer.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    SongPlayer.MP.seekTo(i);
                    SongPlayer.MP.start();
                }
            }
        });
        Intent action = new Intent(this, broadcastReciever.class).setAction("PREV");
        this.prev_intent = action;
        this.prev_pi = PendingIntent.getBroadcast(this, 1, action, PendingIntent.FLAG_IMMUTABLE);
        Intent action2 = new Intent(this, broadcastReciever.class).setAction("PLAY_PAUSE");
        this.play_intent = action2;
        this.play_pi = PendingIntent.getBroadcast(this, 2, action2, PendingIntent.FLAG_IMMUTABLE);
        Intent action3 = new Intent(this, broadcastReciever.class).setAction("NEXT");
        this.next_intent = action3;
        this.next_pi = PendingIntent.getBroadcast(this, 3, action3, PendingIntent.FLAG_IMMUTABLE);
        Intent intent = new Intent(getApplicationContext(), SongPlayer.class);
        this.to_SongPlayer = intent;
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        this.pi = PendingIntent.getActivity(this, 7, this.to_SongPlayer, PendingIntent.FLAG_IMMUTABLE);
        this.action1 = new Notification.Action(R.drawable.icon_prev, "prev", this.prev_pi);
        this.action3 = new Notification.Action(R.drawable.icon_next, "next", this.next_pi);
        this.songsList = (ArrayList) getIntent().getBundleExtra("songsList").getSerializable("songsList_bundle");
        this.position = getIntent().getIntExtra("position", 0);
        this.fromItsCome = getIntent().getStringExtra("fromItsCome");
        this.fromWhichFragment = getIntent().getStringExtra("fromWhichFragment");
        Log.e("songsList", "onCreate: " + this.songsList);
        this.current_song = this.songsList.get(this.position);
        int i = getSharedPreferences("appTheme", 0).getInt("themeResourceID", 0);
        getWindow().setBackgroundDrawableResource(i);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_05));
        this.binding.songPlayerImage.setBackgroundResource(i);
        this.binding.songPlayerPlay.setBackgroundResource(i);
        MediaSession mediaSession = new MediaSession(this, "session");
        this.mediaSession = mediaSession;
        mediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS);
        this.mediaSession.setMediaButtonReceiver(null);
        PlaybackState.Builder actions = new PlaybackState.Builder().setActions(PlaybackState.ACTION_PLAY);
        this.state = actions;
        actions.setState(PlaybackState.STATE_PLAYING, MP.getCurrentPosition(), 1.0f);
        this.mediaSession.setPlaybackState(this.state.build());
        this.mediaSession.setCallback(new MediaSession.Callback() { // from class: com.developer404.music.SongPlayer.3
            @Override // android.media.session.MediaSession.Callback
            public void onPlay() {
                super.onPlay();
                SongPlayer.this.service_Intent.putExtra("command", "play_pause");
                SongPlayer songPlayer = SongPlayer.this;
                songPlayer.startService(songPlayer.service_Intent);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onPause() {
                super.onPause();
                SongPlayer.this.service_Intent.putExtra("command", "play_pause");
                SongPlayer songPlayer = SongPlayer.this;
                songPlayer.startService(songPlayer.service_Intent);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onSkipToNext() {
                super.onSkipToNext();
                SongPlayer.this.service_Intent.putExtra("command", "next");
                SongPlayer songPlayer = SongPlayer.this;
                songPlayer.startService(songPlayer.service_Intent);
            }

            @Override // android.media.session.MediaSession.Callback
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                SongPlayer.this.service_Intent.putExtra("command", "prev");
                SongPlayer songPlayer = SongPlayer.this;
                songPlayer.startService(songPlayer.service_Intent);
            }
        });
        this.mediaSession.setActive(true);
        this.connection = new ServiceConnection() { // from class: com.developer404.music.SongPlayer.4
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                SongPlayer.this.my_service = ((myService.myservice_ibinder) iBinder).return_muservice();
                SongPlayer.this.my_service.callback(SongPlayer.this);
                Log.e("debuging", "callback: onserviceConnected ");
                if (SongPlayer.this.disconnected == 0) {
                    SongPlayer.this.service_Intent = new Intent(SongPlayer.this, myService.class);
                    SongPlayer.this.service_Intent.putExtra("command", "play_current");
                    SongPlayer.this.service_Intent.putExtra("restartService", "No");
                    SongPlayer songPlayer = SongPlayer.this;
                    songPlayer.startService(songPlayer.service_Intent);
                    SongPlayer.this.disconnected++;
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                Log.e("debuging", "callback: onservice disconected ");
            }
        };
        bindService(new Intent(this, myService.class), this.connection, Context.BIND_AUTO_CREATE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100) {
            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            this.dialog_moreOption.dismiss();
            finish();
            SharedPreferences.Editor edit = getSharedPreferences("active position sp", 0).edit();
            edit.putInt("active position", -1);
            edit.apply();
            Intent intent2 = new Intent(this, MainActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        this.service_Intent = new Intent(this, myService.class);
        int id = view.getId();
        switch (id) {
            case R.id.dialog_more_addToPlaylist /* 2131230895 */:
                this.dialog_moreOption.dismiss();
                Type type = new TypeToken<String[]>() { // from class: com.developer404.music.SongPlayer.5
                }.getType();
                final Type type2 = new TypeToken<ArrayList<model>>() { // from class: com.developer404.music.SongPlayer.6
                }.getType();
                final String[] strArr = (String[]) this.gson.fromJson(this.playlist_songs_sp.getString("Playlists", ""), type);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setSingleChoiceItems(strArr, -1, new DialogInterface.OnClickListener() { // from class: com.developer404.music.SongPlayer.7
                    public boolean isSongExists(String str, ArrayList<model> arrayList) {
                        for (int i = 0; i < arrayList.size(); i++) {
                            if (str.equals(arrayList.get(i).getTitle())) {
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<model> arrayList;
                        String str = strArr[i].equals("Liked") ? "FavSongs" : strArr[i];
                        if (SongPlayer.this.playlist_songs_sp.getString(str, "").equals("")) {
                            arrayList = new ArrayList<>();
                        } else {
                            arrayList = (ArrayList) SongPlayer.this.gson.fromJson(SongPlayer.this.playlist_songs_sp.getString(str, ""), type2);
                        }
                        if (!isSongExists(SongPlayer.this.current_song.getTitle(), arrayList)) {
                            arrayList.add(SongPlayer.this.current_song);
                        }
                        if (strArr[i].equals("Liked")) {
                            SongPlayer.this.editor.putString("FavSongs", SongPlayer.this.gson.toJson(arrayList));
                        } else {
                            SongPlayer.this.editor.putString(strArr[i], SongPlayer.this.gson.toJson(arrayList));
                        }
                        SongPlayer.this.editor.apply();
                        Toast.makeText(SongPlayer.this.getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                        SongPlayer.this.favChecker();
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog create = builder.create();
                create.getWindow().setBackgroundDrawableResource(R.drawable.dialog_more_option_bg);
                create.getWindow().setGravity(80);
                create.show();
                return;
            case R.id.dialog_more_delete /* 2131230896 */:
                Uri withAppendedId = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(this.current_song.getSong_ID()));
                PendingIntent pendingIntent = null;
                try {
                    getContentResolver().delete(withAppendedId, null, null);
                    return;
                } catch (SecurityException e) {
                    if (Build.VERSION.SDK_INT >= 30) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(withAppendedId);
                        pendingIntent = MediaStore.createDeleteRequest(getContentResolver(), arrayList);
                    } else if (Build.VERSION.SDK_INT >= 29 && (e instanceof RecoverableSecurityException)) {
                        pendingIntent = ((RecoverableSecurityException) e).getUserAction().getActionIntent();
                    }
                    if (pendingIntent == null) {
                        return;
                    }
                    try {
                        startIntentSenderForResult(pendingIntent.getIntentSender(), 100, null, 0, 0, 0);
                        return;
                    } catch (IntentSender.SendIntentException e2) {
                        throw new RuntimeException(e2);
                    }
                }
            case R.id.dialog_more_sleepTimer /* 2131230897 */:
                startActivity(new Intent(this, settings.class));
                this.dialog_moreOption.dismiss();
                return;
            default:
                switch (id) {
                    case R.id.songPlayerLoop /* 2131231167 */:
                        if (MP.isLooping()) {
                            MP.setLooping(false);
                            this.binding.songPlayerLoop.setImageResource(R.drawable.icon_shuffle);
                            shuffle();
                            return;
                        }
                        MP.setLooping(true);
                        MP.start();
                        this.binding.songPlayerLoop.setImageResource(R.drawable.icon_repeat_one);
                        return;
                    case R.id.songPlayerLove /* 2131231168 */:
                        if (!this.playlist_songs_sp.contains("FavSongs")) {
                            this.favSongs_arraylist.add(this.songsList.get(this.position));
                            this.editor.putString("FavSongs", this.gson.toJson(this.favSongs_arraylist));
                            this.editor.apply();
                            this.binding.songPlayerLove.setImageResource(R.drawable.icon_heart_solid);
                            return;
                        } else if (this.playlist_songs_sp.getString("FavSongs", "").equals("")) {
                            this.favSongs_arraylist.add(this.songsList.get(this.position));
                            this.editor.putString("FavSongs", this.gson.toJson(this.favSongs_arraylist));
                            this.editor.apply();
                            this.binding.songPlayerLove.setImageResource(R.drawable.icon_heart_solid);
                            return;
                        } else if (this.playlist_songs_sp.getString("FavSongs", "").equals("")) {
                            return;
                        } else {
                            int favChecker = favChecker();
                            if (favChecker == -1) {
                                this.favSongs_arraylist.add(this.songsList.get(this.position));
                                this.editor.putString("FavSongs", this.gson.toJson(this.favSongs_arraylist));
                                this.editor.apply();
                                this.binding.songPlayerLove.setImageResource(R.drawable.icon_heart_solid);
                                return;
                            }
                            this.favSongs_arraylist.remove(favChecker);
                            this.editor.putString("FavSongs", this.gson.toJson(this.favSongs_arraylist));
                            this.editor.apply();
                            this.binding.songPlayerLove.setImageResource(R.drawable.icon_heart_line);
                            return;
                        }
                    case R.id.songPlayerMoreOptions /* 2131231169 */:
                        this.dialog_moreOption.show();
                        this.dialog_moreOption.getWindow().setLayout(-1, -2);
                        this.dialog_moreOption.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        return;
                    case R.id.songPlayerNext /* 2131231170 */:
                        this.service_Intent.putExtra("command", "next");
                        startService(this.service_Intent);
                        return;
                    case R.id.songPlayerPlay /* 2131231171 */:
                        this.service_Intent.putExtra("command", "play_pause");
                        startService(this.service_Intent);
                        return;
                    case R.id.songPlayerPrev /* 2131231172 */:
                        this.service_Intent.putExtra("command", "prev");
                        startService(this.service_Intent);
                        return;
                    default:
                        return;
                }
        }
    }

    public void play_current() throws IOException {
        this.current_song = this.songsList.get(this.position);
        this.binding.songPlayerTitle.setText(this.current_song.getTitle());
        MP.reset();
        MP.setDataSource(this.current_song.getData());
        MP.prepare();
        MP.start();
        update_active_position(this.position);
        this.binding.songPlayerSeekbar.setMax(MP.getDuration());
        this.binding.songPlayerEndTime.setText(timeConvert(String.valueOf(MP.getDuration())));
        requestAudioFocus();
        this.handler1.post(this.updateSeekbar);
        shuffle();
        favChecker();
    }

    public void prev() throws IOException {
        int i = this.position - 1;
        this.position = i;
        this.current_song = this.songsList.get(i);
        this.binding.songPlayerTitle.setText(this.current_song.getTitle());
        MP.reset();
        MP.setDataSource(this.current_song.getData());
        MP.prepare();
        MP.start();
        update_active_position(this.position);
        this.state.setState(PlaybackState.STATE_PLAYING, MP.getCurrentPosition(), 1.0f);
        this.mediaSession.setPlaybackState(this.state.build());
        this.binding.songPlayerSeekbar.setMax(MP.getDuration());
        this.binding.songPlayerEndTime.setText(timeConvert(String.valueOf(MP.getDuration())));
        this.handler1.post(this.updateSeekbar);
        favChecker();
    }

    public void play_pause() {
        if (MP.isPlaying()) {
            MP.pause();
            this.binding.songPlayerPlay.setImageResource(R.drawable.icon_play);
            this.state.setState(PlaybackState.STATE_PAUSED, MP.getCurrentPosition(), 1.0f);
            this.mediaSession.setPlaybackState(this.state.build());
            this.service_Intent.putExtra("command", "pauseService");
            startService(this.service_Intent);
            return;
        }
        requestAudioFocus();
        MP.start();
        this.binding.songPlayerPlay.setImageResource(R.drawable.icon_pause);
        this.state.setState(PlaybackState.STATE_PLAYING, MP.getCurrentPosition(), 1.0f);
        this.mediaSession.setPlaybackState(this.state.build());
        this.service_Intent.putExtra("command", "resumeService");
        startService(this.service_Intent);
    }

    public void next() throws IOException {
        int i = this.position + 1;
        this.position = i;
        this.current_song = this.songsList.get(i);
        this.binding.songPlayerTitle.setText(this.current_song.getTitle());
        MP.reset();
        MP.setDataSource(this.current_song.getData());
        MP.prepare();
        MP.start();
        update_active_position(this.position);
        this.state.setState(PlaybackState.STATE_PLAYING, MP.getCurrentPosition(), 1.0f);
        this.mediaSession.setPlaybackState(this.state.build());
        this.binding.songPlayerSeekbar.setMax(MP.getDuration());
        this.binding.songPlayerEndTime.setText(timeConvert(String.valueOf(MP.getDuration())));
        this.handler1.post(this.updateSeekbar);
        favChecker();
    }

    public Boolean MP_position() {
        if (this.position < this.songsList.size() - 1) {
            return true;
        }
        return false;
    }

    public void onShufflled() {
        SharedPreferences.Editor edit = getSharedPreferences("active position sp", 0).edit();
        edit.putInt("active position", -1);
        edit.apply();
        finish();
    }

    public void releaseMP() {
        MP.release();
    }

    public void update_active_position(int i) {
        if (this.fromItsCome.equals("main")) {
            SharedPreferences.Editor edit = getSharedPreferences("active position sp", 0).edit();
            edit.putInt("active position", i);
            edit.apply();
        } else if (this.fromWhichFragment.equals("playlist")) {
            SharedPreferences.Editor edit2 = getSharedPreferences("playlist_songs", 0).edit();
            edit2.putInt(this.fromItsCome + " active position", i);
            edit2.apply();
        } else if (!this.fromWhichFragment.equals("album")) {
        } else {
            SharedPreferences.Editor edit3 = getSharedPreferences("album_songs", 0).edit();
            edit3.putInt(this.fromItsCome, i);
            edit3.apply();
        }
    }

    public void requestAudioFocus() {
        this.audioManager.requestAudioFocus(this.audioFocusChangeListener, 3, 1);
    }

    public boolean isServiceRunning(Context context, Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (runningServiceInfo.service.getClassName().equals(cls.getName())) {
                return true;
            }
        }
        return false;
    }

    public void notificationFunction() {
        Notification.Action action = new Notification.Action(MP.isPlaying() ? R.drawable.icon_pause : R.drawable.icon_play, "play", this.play_pi);
        if (Build.VERSION.SDK_INT >= 26) {
            this.notificationManagerCompat.createNotificationChannel(new NotificationChannel("songPlayerID", "SongPlayer", NotificationManager.IMPORTANCE_DEFAULT));
            this.notification = new Notification.Builder(getApplicationContext(), "songPlayerID").setContentTitle(this.current_song.getTitle()).setContentText(this.current_song.getArtist()).setSmallIcon(R.drawable.icon_app).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_app)).setContentIntent(this.pi).addAction(this.action1).addAction(action).addAction(this.action3).setStyle(new Notification.MediaStyle().setMediaSession(this.mediaSession.getSessionToken())).setChannelId("songPlayerID").setOnlyAlertOnce(true).build();
        }
    }

    public String timeConvert(String str) {
        Long valueOf = Long.valueOf(Long.parseLong(str));
        return String.format(new Locale("en"), "%d:%d", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(valueOf.longValue()) % TimeUnit.HOURS.toMinutes(1L)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(valueOf.longValue()) % 60));
    }

    /* loaded from: classes.dex */
    public class UpdateSeekbar implements Runnable {
        public UpdateSeekbar() {
        }

        @Override // java.lang.Runnable
        public void run() {
            SongPlayer.this.binding.songPlayerSeekbar.setProgress(SongPlayer.MP.getCurrentPosition());
            long currentPosition = SongPlayer.MP.getCurrentPosition();
            SongPlayer.this.binding.songPlayerStartTime.setText(String.format(new Locale("en"), "%d:%d", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(currentPosition)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(currentPosition) % 60)));
            SongPlayer.this.handler1.postDelayed(this, 1000L);
        }
    }

    public void removeRunable() {
        this.handler1.removeCallbacks(this.updateSeekbar);
    }

    public void shuffle() {
        MP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.developer404.music.SongPlayer.8
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer) {
                SongPlayer.this.service_Intent.putExtra("command", "next");
                SongPlayer songPlayer = SongPlayer.this;
                songPlayer.startService(songPlayer.service_Intent);
            }
        });
    }

    public int favChecker() {
        Type type = new TypeToken<ArrayList<model>>() { // from class: com.developer404.music.SongPlayer.9
        }.getType();
        if (this.playlist_songs_sp.contains("FavSongs") && !this.playlist_songs_sp.getString("FavSongs", "").equals("") && !this.playlist_songs_sp.getString("FavSongs", "").equals("")) {
            ArrayList<model> arrayList = (ArrayList) this.gson.fromJson(this.playlist_songs_sp.getString("FavSongs", ""), type);
            this.favSongs_arraylist = arrayList;
            if (arrayList != null) {
                for (int i = 0; i < this.favSongs_arraylist.size(); i++) {
                    if (this.favSongs_arraylist.get(i).getTitle().equals(this.songsList.get(this.position).getTitle())) {
                        this.binding.songPlayerLove.setImageResource(R.drawable.icon_heart_solid);
                        return i;
                    }
                }
            }
        }
        this.binding.songPlayerLove.setImageResource(R.drawable.icon_heart_line);
        return -1;
    }

    public boolean MP_isPlaying() {
        return MP.isPlaying();
    }

    public Notification returnNotification() {
        return this.notification;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        notificationFunction();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        bindService(new Intent(this, myService.class), this.connection, Context.BIND_AUTO_CREATE);
        int i = getSharedPreferences("appTheme", 0).getInt("themeResourceID", 0);
        getWindow().setBackgroundDrawableResource(i);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_05));
        this.binding.songPlayerImage.setBackgroundResource(i);
        this.binding.songPlayerPlay.setBackgroundResource(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.audioManager.abandonAudioFocus(this.audioFocusChangeListener);
    }

    public void doOnTaskRemoved() {
        SharedPreferences.Editor edit = getSharedPreferences("active position sp", 0).edit();
        edit.putInt("active position", -1);
        edit.apply();
        this.handler1.removeCallbacks(this.updateSeekbar);
        this.notificationManagerCompat.cancel(2);
        this.mediaSession.release();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.fromItsCome.equals("main")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            unbindService(this.connection);
        } else if (this.fromWhichFragment.equals("playlist")) {
            Intent intent2 = new Intent(this, Playlists_songs.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent2);
        } else if (!this.fromWhichFragment.equals("album")) {
        } else {
            Intent intent3 = new Intent(this, album_songs.class);
            intent3.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent3);
        }
    }
}
