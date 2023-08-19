package com.developer404.music.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.developer404.music.R;
import com.developer404.music.interfaces.RV_interface_addToPlaylist;
import com.developer404.music.others.model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class addToPlaylistAdapter extends RecyclerView.Adapter<addToPlaylistAdapter.holder> {
    RV_interface_addToPlaylist RV_interface;
    Context context;
    String playlist_name;
    String playlist_songs_string;
    ArrayList<model> songsList;
    SharedPreferences sp;
    Type type;
    ArrayList<model> playlist_songs = new ArrayList<>();
    Gson gson = new Gson();

    public addToPlaylistAdapter(Context context, ArrayList<model> arrayList, RV_interface_addToPlaylist rV_interface_addToPlaylist, String str) {
        this.context = context;
        this.songsList = arrayList;
        this.RV_interface = rV_interface_addToPlaylist;
        this.playlist_name = str;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_to_playlists, viewGroup, false));
    }

    public void onBindViewHolder(holder holderVar, int i) {
        holderVar.title.setText(this.songsList.get(i).getTitle());
        holderVar.artist.setText(this.songsList.get(i).getArtist());
        if (check_playlist_song(i) == i) {
            holderVar.icon_add.setImageResource(R.drawable.icon_done);
        } else {
            holderVar.icon_add.setImageResource(R.drawable.icon_add);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.songsList.size();
    }

    /* loaded from: classes.dex */
    public class holder extends RecyclerView.ViewHolder {
        TextView artist;
        ImageView icon_add;
        TextView title;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public holder(final View view) {
            super(view);
// hiding           addToPlaylistAdapter.this = r2;
            this.title = (TextView) view.findViewById(R.id.item_ATP_title);
            this.artist = (TextView) view.findViewById(R.id.item_ATP_artist);
            this.icon_add = (ImageView) view.findViewById(R.id.item_ATP_add_icon);
            view.setOnClickListener(new View.OnClickListener() { // from class: com.developer404.music.adapters.addToPlaylistAdapter.holder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    addToPlaylistAdapter.this.RV_interface.onItemClick(holder.this.getAdapterPosition(), view);
                }
            });
        }
    }

    public int check_playlist_song(int i) {
        this.type = new TypeToken<ArrayList<model>>() { // from class: com.developer404.music.adapters.addToPlaylistAdapter.1
        }.getType();
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("playlist_songs", 0);
        this.sp = sharedPreferences;
        String string = sharedPreferences.getString(this.playlist_name, "");
        this.playlist_songs_string = string;
        if (!string.equals("")) {
            this.playlist_songs = (ArrayList) this.gson.fromJson(this.playlist_songs_string, this.type);
        }
        for (int i2 = 0; i2 < this.playlist_songs.size(); i2++) {
            if (this.songsList.get(i).getTitle().equals(this.playlist_songs.get(i2).getTitle())) {
                return i;
            }
        }
        return -1;
    }
}
