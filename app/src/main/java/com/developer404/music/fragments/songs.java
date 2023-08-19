package com.developer404.music.fragments;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer404.music.R;
import com.developer404.music.SongPlayer;
import com.developer404.music.adapters.songsAdapter;
import com.developer404.music.databinding.FragmentSongsBinding;
import com.developer404.music.interfaces.RecyclerViewClickInterface;
import com.developer404.music.others.model;
import com.developer404.music.others.myService;
import com.developer404.music.settings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class songs extends Fragment implements RecyclerViewClickInterface, View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    public static songsAdapter adapter;
    public static ArrayList<model> songsList2;
    LinearLayout LL_addToPlaylist;
    LinearLayout LL_delete;
    LinearLayout LL_sleepTimer;
    SharedPreferences active_position_main;
    SharedPreferences.Editor active_position_main_editor;
    FragmentSongsBinding binding;
    Dialog dialog_addToPlaylist;
    Dialog dialog_moreOption;
    int long_pressed_position;
    ArrayList<model> songsList;
    SharedPreferences sp_playlist_songs;
    SharedPreferences.Editor sp_playlist_songs_editor;
    Gson gson = new Gson();
    SharedPreferences.OnSharedPreferenceChangeListener Listener = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: com.developer404.music.fragments.songs.3
        @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            songs.this.onResume();
        }
    };

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.binding = FragmentSongsBinding.inflate(layoutInflater, viewGroup, false);
        Dialog dialog = new Dialog(getContext());
        this.dialog_moreOption = dialog;
        dialog.setContentView(R.layout.dialog_more_option);
        this.dialog_moreOption.getWindow().setGravity(80);
        this.LL_addToPlaylist = (LinearLayout) this.dialog_moreOption.findViewById(R.id.dialog_more_addToPlaylist);
        this.LL_sleepTimer = (LinearLayout) this.dialog_moreOption.findViewById(R.id.dialog_more_sleepTimer);
        this.LL_delete = (LinearLayout) this.dialog_moreOption.findViewById(R.id.dialog_more_delete);
        this.LL_addToPlaylist.setOnClickListener(this);
        this.LL_sleepTimer.setOnClickListener(this);
        this.LL_delete.setOnClickListener(this);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("playlist_songs", 0);
        this.sp_playlist_songs = sharedPreferences;
        this.sp_playlist_songs_editor = sharedPreferences.edit();
        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("active position sp", 0);
        this.active_position_main = sharedPreferences2;
        this.active_position_main_editor = sharedPreferences2.edit();
        this.active_position_main.registerOnSharedPreferenceChangeListener(this.Listener);
        if (ContextCompat.checkSelfPermission(getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        } else {
            this.songsList = fetch_all_music();
            songsList2 = fetch_all_music();
            Log.e("songsList", "onCreateView: " + this.songsList);
        }
        this.binding.songsRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new songsAdapter(this.songsList, getContext(), this, "main", "main");
        this.binding.songsRV.setAdapter(adapter);
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

    @Override // com.developer404.music.interfaces.RecyclerViewClickInterface
    public void onItemClick(int i) {
        ArrayList arrayList;
        ArrayList arrayList2;
        if (isServiceRunning(getContext(), myService.class)) {
            if (i == this.active_position_main.getInt("active position", 0)) {
                Intent intent = new Intent(getContext(), SongPlayer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            } else {
                Intent intent2 = new Intent(getContext(), SongPlayer.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putSerializable("songsList_bundle", this.songsList);
                intent2.putExtra("songsList", bundle);
                intent2.putExtra("position", mainPositionFromSearch(i));
                intent2.putExtra("fromItsCome", "main");
                intent2.putExtra("fromWhichFragment", "main");
                startActivity(intent2);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<String>>() { // from class: com.developer404.music.fragments.songs.1
                }.getType();
                if (!this.sp_playlist_songs.getString("Playlists", "").equals("")) {
                    arrayList2 = (ArrayList) gson.fromJson(this.sp_playlist_songs.getString("Playlists", ""), type);
                } else {
                    arrayList2 = new ArrayList();
                }
                for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                    if (i2 == 0) {
                        this.sp_playlist_songs_editor.putInt("FavSongs active position", -1);
                        this.sp_playlist_songs_editor.apply();
                    }
                    SharedPreferences.Editor editor = this.sp_playlist_songs_editor;
                    editor.putInt(((String) arrayList2.get(i2)) + " active position", -1);
                    this.sp_playlist_songs_editor.apply();
                }
            }
        } else {
            Intent intent3 = new Intent(getContext(), SongPlayer.class);
            intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle2 = new Bundle();
            bundle2.putSerializable("songsList_bundle", this.songsList);
            intent3.putExtra("songsList", bundle2);
            intent3.putExtra("position", mainPositionFromSearch(i));
            intent3.putExtra("fromItsCome", "main");
            intent3.putExtra("fromWhichFragment", "main");
            startActivity(intent3);
            Gson gson2 = new Gson();
            Type type2 = new TypeToken<ArrayList<String>>() { // from class: com.developer404.music.fragments.songs.2
            }.getType();
            if (!this.sp_playlist_songs.getString("Playlists", "").equals("")) {
                arrayList = (ArrayList) gson2.fromJson(this.sp_playlist_songs.getString("Playlists", ""), type2);
            } else {
                arrayList = new ArrayList();
            }
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                if (i3 == 0) {
                    this.sp_playlist_songs_editor.putInt("FavSongs active position", -1);
                    this.sp_playlist_songs_editor.apply();
                }
                SharedPreferences.Editor editor2 = this.sp_playlist_songs_editor;
                editor2.putInt(((String) arrayList.get(i3)) + " active position", -1);
                this.sp_playlist_songs_editor.apply();
            }
        }
        this.active_position_main_editor.putInt("active position", mainPositionFromSearch(i));
        this.active_position_main_editor.apply();
    }

    @Override // com.developer404.music.interfaces.RecyclerViewClickInterface
    public void onLongItemClick(int i) {
        this.long_pressed_position = i;
        this.dialog_moreOption.show();
        this.dialog_moreOption.getWindow().setLayout(-1, -2);
        this.dialog_moreOption.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    public int mainPositionFromSearch(int i) {
        for (int i2 = 0; i2 < this.songsList.size(); i2++) {
            if (songsList2.get(i).getTitle().equals(this.songsList.get(i2).getTitle())) {
                return i2;
            }
        }
        return -1;
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1) {
            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            this.dialog_moreOption.dismiss();
            if (this.long_pressed_position < this.active_position_main.getInt("active position", -1)) {
                this.active_position_main_editor.putInt("active position", this.active_position_main.getInt("active position", -1) - 1);
                this.active_position_main_editor.apply();
                this.songsList.remove(this.long_pressed_position);
                adapter.notifyItemRemoved(this.long_pressed_position);
                if (ContextCompat.checkSelfPermission(getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                    return;
                }
                songsList2 = fetch_all_music();
                Log.e("songsList", "onCreateView: " + this.songsList);
                return;
            }
            this.songsList.remove(this.long_pressed_position);
            adapter.notifyItemRemoved(this.long_pressed_position);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_more_addToPlaylist /* 2131230895 */:
                this.dialog_moreOption.dismiss();
                Type type = new TypeToken<String[]>() { // from class: com.developer404.music.fragments.songs.4
                }.getType();
                final Type type2 = new TypeToken<ArrayList<model>>() { // from class: com.developer404.music.fragments.songs.5
                }.getType();
                final String[] strArr = (String[]) this.gson.fromJson(this.sp_playlist_songs.getString("Playlists", ""), type);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setSingleChoiceItems(strArr, -1, new DialogInterface.OnClickListener() { // from class: com.developer404.music.fragments.songs.6
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
                        if (songs.this.sp_playlist_songs.getString(str, "").equals("")) {
                            arrayList = new ArrayList<>();
                        } else {
                            arrayList = (ArrayList) songs.this.gson.fromJson(songs.this.sp_playlist_songs.getString(str, ""), type2);
                        }
                        if (!isSongExists(songs.this.songsList.get(songs.this.long_pressed_position).getTitle(), arrayList)) {
                            arrayList.add(songs.this.songsList.get(songs.this.long_pressed_position));
                        }
                        if (strArr[i].equals("Liked")) {
                            songs.this.sp_playlist_songs_editor.putString("FavSongs", songs.this.gson.toJson(arrayList));
                        } else {
                            songs.this.sp_playlist_songs_editor.putString(strArr[i], songs.this.gson.toJson(arrayList));
                        }
                        songs.this.sp_playlist_songs_editor.apply();
                        Toast.makeText(songs.this.getContext(), "Added", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog create = builder.create();
                create.getWindow().setBackgroundDrawableResource(R.drawable.dialog_more_option_bg);
                create.getWindow().setGravity(80);
                create.show();
                return;
            case R.id.dialog_more_delete /* 2131230896 */:
                Uri withAppendedId = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(this.songsList.get(this.long_pressed_position).getSong_ID()));
                PendingIntent pendingIntent = null;
                try {
                    requireActivity().getContentResolver().delete(withAppendedId, null, null);
                    return;
                } catch (SecurityException e) {
                    if (Build.VERSION.SDK_INT >= 30) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(withAppendedId);
                        pendingIntent = MediaStore.createDeleteRequest(requireActivity().getContentResolver(), arrayList);
                    } else if (Build.VERSION.SDK_INT >= 29 && (e instanceof RecoverableSecurityException)) {
                        pendingIntent = ((RecoverableSecurityException) e).getUserAction().getActionIntent();
                    }
                    if (pendingIntent == null) {
                        return;
                    }
                    try {
                        startIntentSenderForResult(pendingIntent.getIntentSender(), 100, null, 0, 0, 0, null);
                        return;
                    } catch (IntentSender.SendIntentException e2) {
                        throw new RuntimeException(e2);
                    }
                }
            case R.id.dialog_more_sleepTimer /* 2131230897 */:
                startActivity(new Intent(getContext(), settings.class));
                this.dialog_moreOption.dismiss();
                return;
            default:
                return;
        }
    }

    public boolean isServiceRunning(Context context, Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (runningServiceInfo.service.getClassName().equals(cls.getName())) {
                return true;
            }
        }
        return false;
    }
}
