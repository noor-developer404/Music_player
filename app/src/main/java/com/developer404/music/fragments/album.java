package com.developer404.music.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.developer404.music.adapters.albumAdapter;
import com.developer404.music.album_songs;
import com.developer404.music.databinding.FragmentAlbumBinding;
import com.developer404.music.interfaces.RecyclerViewClickInterface;
import com.developer404.music.others.model;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class album extends Fragment implements RecyclerViewClickInterface {
    private static final int REQUEST_CODE = 1;
    ArrayList<String> albumList = new ArrayList<>();
    FragmentAlbumBinding binding;
    ArrayList<model> songsList;
    SharedPreferences sp_album_songs_active;
    SharedPreferences.Editor sp_album_songs_active_editor;

    @Override // com.developer404.music.interfaces.RecyclerViewClickInterface
    public void onLongItemClick(int i) {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.binding = FragmentAlbumBinding.inflate(layoutInflater, viewGroup, false);
        if (ContextCompat.checkSelfPermission(getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        } else {
            this.songsList = fetch_all_music();
            Log.e("songsList", "onCreateView: " + this.songsList);
        }
        for (int i = 0; i < this.songsList.size(); i++) {
            if (i == 0) {
                this.albumList.add(this.songsList.get(i).getAlbum());
            }
            if (check_albumList(i) == -1) {
                this.albumList.add(this.songsList.get(i).getAlbum());
            }
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("album_songs", 0);
        this.sp_album_songs_active = sharedPreferences;
        this.sp_album_songs_active_editor = sharedPreferences.edit();
        for (int i2 = 0; i2 < this.albumList.size(); i2++) {
            this.sp_album_songs_active_editor.putInt(this.albumList.get(i2), -1);
            this.sp_album_songs_active_editor.apply();
        }
        this.binding.albumRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.binding.albumRv.setAdapter(new albumAdapter(this.albumList, getContext(), this));
        return this.binding.getRoot();
    }

    public ArrayList<model> fetch_all_music() {
        ArrayList<model> arrayList = new ArrayList<>();
        Cursor query = requireActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "title", TypedValues.TransitionType.S_DURATION, "album", "artist", "_id"}, null, null, null);
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

    public int check_albumList(int i) {
        for (int i2 = 0; i2 < this.albumList.size(); i2++) {
            if (this.songsList.get(i).getAlbum().equals(this.albumList.get(i2))) {
                return i2;
            }
        }
        return -1;
    }

    @Override // com.developer404.music.interfaces.RecyclerViewClickInterface
    public void onItemClick(int i) {
        Intent intent = new Intent(getContext(), album_songs.class);
        intent.putExtra("fromWhichFolder", this.albumList.get(i));
        startActivity(intent);
    }
}
