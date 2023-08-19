package com.developer404.music;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer404.music.adapters.songsAdapter;
import com.developer404.music.databinding.ActivityPlaylistsSongsBinding;
import com.developer404.music.interfaces.RecyclerViewClickInterface;
import com.developer404.music.others.model;
import com.developer404.music.others.myService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class Playlists_songs extends AppCompatActivity implements RecyclerViewClickInterface, View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    ActivityPlaylistsSongsBinding binding;
    String playlist_name;
    ArrayList<model> songsList;
    songsAdapter songs_Adapter;
    SharedPreferences sp_active_position_main;
    SharedPreferences.Editor sp_active_position_main_editor;
    SharedPreferences sp_playlist;
    SharedPreferences sp_playlist_active_position;
    SharedPreferences.Editor sp_playlist_active_position_editor;
    SharedPreferences sp_theme;
    Type type;
    ArrayList<model> playlist_songsList = new ArrayList<>();
    Gson gson = new Gson();
    SharedPreferences.OnSharedPreferenceChangeListener Listener = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: com.developer404.music.Playlists_songs.2
        @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            if (str.equals(Playlists_songs.this.playlist_name + " active position")) {
                Playlists_songs playlists_songs = Playlists_songs.this;
                playlists_songs.sp_active_position_main = playlists_songs.getSharedPreferences("active position sp", 0);
                Playlists_songs playlists_songs2 = Playlists_songs.this;
                playlists_songs2.sp_active_position_main_editor = playlists_songs2.sp_active_position_main.edit();
                SharedPreferences.Editor editor = Playlists_songs.this.sp_active_position_main_editor;
                Playlists_songs playlists_songs3 = Playlists_songs.this;
                SharedPreferences sharedPreferences2 = playlists_songs3.sp_playlist_active_position;
                editor.putInt("active position", playlists_songs3.songsListPositionFromPlaylist_songs(sharedPreferences2.getInt(Playlists_songs.this.playlist_name + " active position", -1)));
                Playlists_songs.this.sp_active_position_main_editor.apply();
                Playlists_songs.this.onResume();
            }
        }
    };

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityPlaylistsSongsBinding inflate = ActivityPlaylistsSongsBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("playlist_songs", 0);
        this.sp_playlist_active_position = sharedPreferences;
        this.sp_playlist_active_position_editor = sharedPreferences.edit();
        this.playlist_name = this.sp_playlist_active_position.getString("playlist_before_SongPlayer", "");
        isFileExist();
        this.sp_playlist_active_position.registerOnSharedPreferenceChangeListener(this.Listener);
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        } else {
            this.songsList = fetch_all_music();
            Log.e("songsList from add", "onCreateView: " + this.songsList);
        }
        this.binding.playlistSongsShuffle.setOnClickListener(this);
        this.binding.playlistSongsAddIcon.setOnClickListener(this);
        SharedPreferences sharedPreferences2 = getSharedPreferences("appTheme", 0);
        this.sp_theme = sharedPreferences2;
        getWindow().setBackgroundDrawableResource(sharedPreferences2.getInt("themeResourceID", 0));
        getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        this.type = new TypeToken<ArrayList<model>>() { // from class: com.developer404.music.Playlists_songs.1
        }.getType();
        SharedPreferences sharedPreferences3 = getSharedPreferences("playlist_songs", 0);
        this.sp_playlist = sharedPreferences3;
        String string = sharedPreferences3.getString(this.playlist_name, "");
        if (!string.equals("")) {
            this.playlist_songsList = (ArrayList) this.gson.fromJson(string, this.type);
        }
        this.binding.playlistSongsTitle.setText(this.playlist_name);
        if (this.playlist_name.equals("FavSongs")) {
            this.binding.playlistSongsIcon.setImageResource(R.drawable.icon_heart_solid);
            this.binding.playlistSongsIcon.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY));
        }
        this.binding.playlistSongsRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.songs_Adapter = new songsAdapter(this.playlist_songsList, this, this, this.playlist_name, "playlist");
        this.binding.playlistSongsRv.setAdapter(this.songs_Adapter);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.playlist_songs_add_icon) {
            Intent intent = new Intent(this, add_to_playlist.class);
            intent.putExtra("playlist_name", this.playlist_name);
            startActivity(intent);
        } else if (id != R.id.playlist_songs_shuffle) {
        } else {
            stopService(new Intent(this, myService.class));
            Intent intent2 = new Intent(this, SongPlayer.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putSerializable("songsList_bundle", this.playlist_songsList);
            intent2.putExtra("songsList", bundle);
            intent2.putExtra("position", 0);
            intent2.putExtra("fromItsCome", this.playlist_name);
            intent2.putExtra("fromWhichFragment", "playlist");
            startActivity(intent2);
        }
    }

    @Override // com.developer404.music.interfaces.RecyclerViewClickInterface
    public void onItemClick(int i) {
        SharedPreferences sharedPreferences = this.sp_playlist_active_position;
        if (i == sharedPreferences.getInt(this.playlist_name + " active position", -1)) {
            Intent intent = new Intent(this, SongPlayer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else {
            stopService(new Intent(this, myService.class));
            Intent intent2 = new Intent(this, SongPlayer.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putSerializable("songsList_bundle", this.playlist_songsList);
            intent2.putExtra("songsList", bundle);
            intent2.putExtra("position", i);
            intent2.putExtra("fromItsCome", this.playlist_name);
            intent2.putExtra("fromWhichFragment", "playlist");
            startActivity(intent2);
        }
        SharedPreferences.Editor editor = this.sp_playlist_active_position_editor;
        editor.putInt(this.playlist_name + " active position", i);
        this.sp_playlist_active_position_editor.apply();
    }

    @Override // com.developer404.music.interfaces.RecyclerViewClickInterface
    public void onLongItemClick(int i) {
        Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        String string = this.sp_playlist.getString(this.playlist_name, "");
        if (!string.equals("")) {
            this.playlist_songsList = (ArrayList) this.gson.fromJson(string, this.type);
        }
        this.binding.playlistSongsRv.setAdapter(new songsAdapter(this.playlist_songsList, this, this, this.playlist_name, "playlist"));
        super.onResume();
    }

    public ArrayList<model> fetch_all_music() {
        ArrayList<model> arrayList = new ArrayList<>();
        Cursor query = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "title", TypedValues.TransitionType.S_DURATION, "album", "artist", "_id"}, null, null, null);
        if (query != null) {
            while (query.moveToNext()) {
                arrayList.add(new model(query.getString(0), query.getString(1), query.getString(2), query.getString(3), query.getString(4), query.getString(5)));
            }
            query.close();
        } else {
            Log.e("cursor", "getSongsArray:cursor is empty ");
        }
        return arrayList;
    }

    public int songsListPositionFromPlaylist_songs(int i) {
        for (int i2 = 0; i2 < this.songsList.size(); i2++) {
            if (this.playlist_songsList.get(i).getTitle().equals(this.songsList.get(i2).getTitle())) {
                return i2;
            }
        }
        return -1;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    public void isFileExist() {
        ArrayList arrayList = new ArrayList();
        this.type = new TypeToken<ArrayList<model>>() { // from class: com.developer404.music.Playlists_songs.3
        }.getType();
        SharedPreferences sharedPreferences = getSharedPreferences("playlist_songs", 0);
        this.sp_playlist = sharedPreferences;
        String string = sharedPreferences.getString(this.playlist_name, "");
        if (!string.equals("")) {
            arrayList = (ArrayList) this.gson.fromJson(string, this.type);
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (!new File(((model) arrayList.get(i)).getData()).exists()) {
                arrayList.remove(i);
            }
        }
        SharedPreferences.Editor edit = this.sp_playlist.edit();
        edit.putString(this.playlist_name, this.gson.toJson(arrayList));
        edit.apply();
    }
}
