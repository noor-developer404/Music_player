package com.developer404.music.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.developer404.music.Playlists_songs;
import com.developer404.music.R;
import com.developer404.music.adapters.playlistsAdapter;
import com.developer404.music.databinding.FragmentPlaylistBinding;
import com.developer404.music.interfaces.RecyclerViewClickInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class playlists extends Fragment implements RecyclerViewClickInterface, View.OnClickListener {
    playlistsAdapter adapter;
    FragmentPlaylistBinding binding;
    Dialog dialog_playlist_create;
    Dialog dialog_playlist_delete;
    SharedPreferences.Editor editor;
    int position_for_delete;
    SharedPreferences sp;
    ArrayList<String> playlist_array = new ArrayList<>();
    Gson gson = new Gson();

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.binding = FragmentPlaylistBinding.inflate(layoutInflater, viewGroup, false);
        Dialog dialog = new Dialog(getContext());
        this.dialog_playlist_create = dialog;
        dialog.setContentView(R.layout.dialog_add_playlist);
        Dialog dialog2 = new Dialog(getContext());
        this.dialog_playlist_delete = dialog2;
        dialog2.setContentView(R.layout.dialog_playlist_delete);
        this.dialog_playlist_delete.findViewById(R.id.playlist_delete_cancel).setBackgroundColor(0);
        this.binding.FAB.setOnClickListener(this);
        this.dialog_playlist_create.findViewById(R.id.btn_create).setOnClickListener(this);
        this.dialog_playlist_delete.findViewById(R.id.playlist_delete).setOnClickListener(this);
        this.dialog_playlist_delete.findViewById(R.id.playlist_delete_cancel).setOnClickListener(this);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("playlist_songs", 0);
        this.sp = sharedPreferences;
        this.editor = sharedPreferences.edit();
        if (!this.sp.contains("Playlists")) {
            this.playlist_array.add("Liked");
            this.editor.putString("Playlists", this.gson.toJson(this.playlist_array));
            this.editor.putInt("FavSongs active position", -1);
            this.editor.apply();
        } else if (this.sp.getString("Playlists", "").equals("")) {
            this.playlist_array.add("Liked");
            this.editor.putString("Playlists", this.gson.toJson(this.playlist_array));
            this.editor.putInt("FavSongs active position", -1);
            this.editor.apply();
        } else if (!this.sp.getString("Playlists", "").equals("")) {
            this.playlist_array = (ArrayList) this.gson.fromJson(this.sp.getString("Playlists", ""), new TypeToken<ArrayList<String>>() { // from class: com.developer404.music.fragments.playlists.1
            }.getType());
            Log.e("playlist_array", "" + this.playlist_array.get(0));
        }
        this.binding.playlistRV.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.adapter = new playlistsAdapter(this.playlist_array, getContext(), this);
        this.binding.playlistRV.setAdapter(this.adapter);
        return this.binding.getRoot();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.FAB /* 2131230724 */:
                this.dialog_playlist_create.show();
                return;
            case R.id.btn_create /* 2131230826 */:
                EditText editText = (EditText) this.dialog_playlist_create.findViewById(R.id.playlist_name_input);
                if (!editText.getText().toString().equals("")) {
                    ArrayList<String> arrayList = (ArrayList) this.gson.fromJson(this.sp.getString("Playlists", ""), new TypeToken<ArrayList<String>>() { // from class: com.developer404.music.fragments.playlists.2
                    }.getType());
                    this.playlist_array = arrayList;
                    arrayList.add(editText.getText().toString());
                    this.editor.putString("Playlists", this.gson.toJson(this.playlist_array));
                    this.editor.apply();
                    this.binding.playlistRV.setAdapter(new playlistsAdapter(this.playlist_array, getContext(), this));
                    if (!this.sp.contains(editText.getText().toString())) {
                        this.editor.putString(editText.getText().toString(), "");
                        this.editor.apply();
                    }
                    SharedPreferences sharedPreferences = this.sp;
                    if (!sharedPreferences.contains(editText.getText().toString() + " active position")) {
                        SharedPreferences.Editor editor = this.editor;
                        editor.putInt(editText.getText().toString() + " active position", -1);
                        this.editor.apply();
                    }
                    editText.setText("");
                    this.dialog_playlist_create.dismiss();
                    return;
                }
                Toast.makeText(getContext(), "khali hai", Toast.LENGTH_SHORT).show();
                return;
            case R.id.playlist_delete /* 2131231099 */:
                ArrayList<String> arrayList2 = (ArrayList) this.gson.fromJson(this.sp.getString("Playlists", ""), new TypeToken<ArrayList<String>>() { // from class: com.developer404.music.fragments.playlists.3
                }.getType());
                this.playlist_array = arrayList2;
                this.editor.remove(arrayList2.get(this.position_for_delete));
                SharedPreferences.Editor editor2 = this.editor;
                editor2.remove(this.playlist_array.get(this.position_for_delete) + " active position");
                this.playlist_array.remove(this.position_for_delete);
                this.editor.putString("Playlists", this.gson.toJson(this.playlist_array));
                this.editor.apply();
                this.binding.playlistRV.setAdapter(new playlistsAdapter(this.playlist_array, getContext(), this));
                this.dialog_playlist_delete.dismiss();
                return;
            case R.id.playlist_delete_cancel /* 2131231100 */:
                this.dialog_playlist_delete.dismiss();
                return;
            default:
                return;
        }
    }

    @Override // com.developer404.music.interfaces.RecyclerViewClickInterface
    public void onItemClick(int i) {
        Intent intent = new Intent(getContext(), Playlists_songs.class);
        if (i == 0) {
            intent.putExtra("playlist_name", "FavSongs");
            this.editor.putString("playlist_before_SongPlayer", "FavSongs");
        } else {
            intent.putExtra("playlist_name", this.playlist_array.get(i));
            this.editor.putString("playlist_before_SongPlayer", this.playlist_array.get(i));
        }
        this.editor.apply();
        startActivity(intent);
    }

    @Override // com.developer404.music.interfaces.RecyclerViewClickInterface
    public void onLongItemClick(int i) {
        if (i != 0) {
            this.position_for_delete = i;
            this.dialog_playlist_delete.show();
        }
    }
}
