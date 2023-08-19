package com.developer404.music;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import com.developer404.music.databinding.ActivityAlbumSongsBinding;
import com.developer404.music.interfaces.RecyclerViewClickInterface;
import com.developer404.music.others.model;
import com.developer404.music.others.myService;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class album_songs extends AppCompatActivity implements RecyclerViewClickInterface, View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    ActivityAlbumSongsBinding binding;
    String fromWhichFolder;
    ArrayList<model> songsList;
    SharedPreferences sp_album_songs_active;
    SharedPreferences.Editor sp_album_songs_active_editor;
    ArrayList<model> album_songs = new ArrayList<>();
    SharedPreferences.OnSharedPreferenceChangeListener Listener = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: com.developer404.music.album_songs.1
        @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            if (str.equals(album_songs.this.fromWhichFolder)) {
                SharedPreferences.Editor edit = album_songs.this.getSharedPreferences("active position sp", 0).edit();
                album_songs album_songsVar = album_songs.this;
                edit.putInt("active position", album_songsVar.songsListPositionFromAlbum_songs(album_songsVar.sp_album_songs_active.getInt(album_songs.this.fromWhichFolder, -1)));
                edit.apply();
                album_songs.this.onResume();
            }
        }
    };

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityAlbumSongsBinding inflate = ActivityAlbumSongsBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        this.binding.playlistSongsShuffle.setOnClickListener(this);
        getWindow().setBackgroundDrawableResource(getSharedPreferences("appTheme", 0).getInt("themeResourceID", 0));
        getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        SharedPreferences sharedPreferences = getSharedPreferences("album_songs", 0);
        this.sp_album_songs_active = sharedPreferences;
        this.sp_album_songs_active_editor = sharedPreferences.edit();
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        } else {
            this.songsList = fetch_all_music();
            Log.e("songsList", "onCreateView: " + this.songsList);
        }
        this.fromWhichFolder = getIntent().getStringExtra("fromWhichFolder");
        for (int i = 0; i < this.songsList.size(); i++) {
            if (this.songsList.get(i).getAlbum().equals(this.fromWhichFolder)) {
                this.album_songs.add(this.songsList.get(i));
            }
        }
        this.sp_album_songs_active.registerOnSharedPreferenceChangeListener(this.Listener);
        this.binding.albumSongsTitle.setText(this.fromWhichFolder);
        this.binding.albumSongsRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.binding.albumSongsRv.setAdapter(new songsAdapter(this.album_songs, this, this, this.fromWhichFolder, "album"));
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

    @Override // com.developer404.music.interfaces.RecyclerViewClickInterface
    public void onItemClick(int i) {
        if (i == this.sp_album_songs_active.getInt(this.fromWhichFolder, -1)) {
            Intent intent = new Intent(this, SongPlayer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else {
            stopService(new Intent(this, myService.class));
            Intent intent2 = new Intent(this, SongPlayer.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putSerializable("songsList_bundle", this.album_songs);
            intent2.putExtra("songsList", bundle);
            intent2.putExtra("position", i);
            intent2.putExtra("fromItsCome", this.fromWhichFolder);
            intent2.putExtra("fromWhichFragment", "album");
            startActivity(intent2);
        }
        this.sp_album_songs_active_editor.putInt(this.fromWhichFolder, i);
        this.sp_album_songs_active_editor.apply();
    }

    @Override // com.developer404.music.interfaces.RecyclerViewClickInterface
    public void onLongItemClick(int i) {
        Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();
    }

    public int songsListPositionFromAlbum_songs(int i) {
        for (int i2 = 0; i2 < this.songsList.size(); i2++) {
            if (this.album_songs.get(i).getTitle().equals(this.songsList.get(i2).getTitle())) {
                return i2;
            }
        }
        return -1;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.binding.albumSongsRv.setAdapter(new songsAdapter(this.album_songs, this, this, this.fromWhichFolder, "album"));
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.playlist_songs_shuffle) {
            stopService(new Intent(this, myService.class));
            Intent intent = new Intent(this, SongPlayer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putSerializable("songsList_bundle", this.album_songs);
            intent.putExtra("songsList", bundle);
            intent.putExtra("position", 0);
            intent.putExtra("fromItsCome", this.fromWhichFolder);
            intent.putExtra("fromWhichFragment", "album");
            startActivity(intent);
        }
    }
}
