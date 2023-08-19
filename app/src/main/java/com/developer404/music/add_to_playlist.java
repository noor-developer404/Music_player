package com.developer404.music;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer404.music.adapters.addToPlaylistAdapter;
import com.developer404.music.databinding.ActivityAddToPlaylistBinding;
import com.developer404.music.interfaces.RV_interface_addToPlaylist;
import com.developer404.music.others.model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class add_to_playlist extends AppCompatActivity implements RV_interface_addToPlaylist {
    private static final int REQUEST_CODE = 1;
    ActivityAddToPlaylistBinding binding;
    SharedPreferences.Editor editor_playlist;
    SharedPreferences.Editor editor_theme;
    String playlist_name;
    SharedPreferences sp_playlist;
    SharedPreferences sp_theme;
    Type type;
    ArrayList<model> songsList = new ArrayList<>();
    Gson gson = new Gson();
    ArrayList<model> playlist_songs = new ArrayList<>();

    @Override // com.developer404.music.interfaces.RV_interface_addToPlaylist
    public void onLongItemClick(int i, View view) {
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityAddToPlaylistBinding inflate = ActivityAddToPlaylistBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        this.type = new TypeToken<ArrayList<model>>() { // from class: com.developer404.music.add_to_playlist.1
        }.getType();
        this.sp_theme = getSharedPreferences("appTheme", 0);
        getWindow().setBackgroundDrawableResource(this.sp_theme.getInt("themeResourceID", 0));
        getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        this.playlist_name = getIntent().getStringExtra("playlist_name");
        SharedPreferences sharedPreferences = getSharedPreferences("playlist_songs", 0);
        this.sp_playlist = sharedPreferences;
        this.editor_playlist = sharedPreferences.edit();
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        } else {
            this.songsList = fetch_all_music();
            Log.e("songsList from add", "onCreateView: " + this.songsList);
        }
        this.binding.addToPlaylistRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.binding.addToPlaylistRV.setAdapter(new addToPlaylistAdapter(this, this.songsList, this, this.playlist_name));
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

    @Override // com.developer404.music.interfaces.RV_interface_addToPlaylist
    public void onItemClick(int i, View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.item_ATP_add_icon);
        if (checkPlaylist(i) == -1) {
            if (!this.sp_playlist.getString(this.playlist_name, "").equals("")) {
                this.playlist_songs = (ArrayList) this.gson.fromJson(this.sp_playlist.getString(this.playlist_name, ""), this.type);
            }
            this.playlist_songs.add(this.songsList.get(i));
            this.editor_playlist.putString(this.playlist_name, this.gson.toJson(this.playlist_songs));
            this.editor_playlist.apply();
            imageView.setImageResource(R.drawable.icon_done);
            return;
        }
        int checkPlaylist = checkPlaylist(i);
        ArrayList<model> arrayList = (ArrayList) this.gson.fromJson(this.sp_playlist.getString(this.playlist_name, ""), this.type);
        this.playlist_songs = arrayList;
        arrayList.remove(checkPlaylist);
        this.editor_playlist.putString(this.playlist_name, this.gson.toJson(this.playlist_songs));
        this.editor_playlist.apply();
        imageView.setImageResource(R.drawable.icon_add);
    }

    public int checkPlaylist(int i) {
        String string = this.sp_playlist.getString(this.playlist_name, "");
        if (!string.equals("")) {
            this.playlist_songs = (ArrayList) this.gson.fromJson(string, this.type);
        }
        for (int i2 = 0; i2 < this.playlist_songs.size(); i2++) {
            if (this.songsList.get(i).getTitle().equals(this.playlist_songs.get(i2).getTitle())) {
                return i2;
            }
        }
        return -1;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
