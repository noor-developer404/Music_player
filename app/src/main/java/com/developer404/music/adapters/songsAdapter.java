package com.developer404.music.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.developer404.music.R;
import com.developer404.music.interfaces.RecyclerViewClickInterface;
import com.developer404.music.others.model;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class songsAdapter extends RecyclerView.Adapter<songsAdapter.holder> {
    RecyclerViewClickInterface R_interface;
    Context context;
    String fromWhichActivity;
    String inWhichSetAdapter;
    ArrayList<model> songsList;
    SharedPreferences sp_album_songs;
    SharedPreferences sp_playlist_songs;
    SharedPreferences sp_position_main;

    public songsAdapter(ArrayList<model> arrayList, Context context, RecyclerViewClickInterface recyclerViewClickInterface, String str, String str2) {
        this.songsList = arrayList;
        this.context = context;
        this.R_interface = recyclerViewClickInterface;
        this.inWhichSetAdapter = str;
        this.fromWhichActivity = str2;
        this.sp_position_main = context.getSharedPreferences("active position sp", 0);
        this.sp_playlist_songs = context.getSharedPreferences("playlist_songs", 0);
        this.sp_album_songs = context.getSharedPreferences("album_songs", 0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_songs_rv, viewGroup, false));
    }

    public void onBindViewHolder(holder holderVar, int i) {
        model modelVar = this.songsList.get(i);
        holderVar.title.setText(modelVar.getTitle());
        holderVar.artist.setText(modelVar.getArtist());
        if (this.inWhichSetAdapter.equals("main")) {
            if (i == this.sp_position_main.getInt("active position", -1)) {
                holderVar.title.setTextColor(this.context.getResources().getColor(R.color.active));
                holderVar.artist.setTextColor(this.context.getResources().getColor(R.color.active));
                holderVar.icon_music.setColorFilter(this.context.getResources().getColor(R.color.active));
                return;
            }
            holderVar.title.setTextColor(this.context.getResources().getColor(R.color.white_09));
            holderVar.artist.setTextColor(this.context.getResources().getColor(R.color.white_09));
            holderVar.icon_music.setColorFilter(this.context.getResources().getColor(R.color.white_09));
        } else if (this.fromWhichActivity.equals("playlist")) {
            SharedPreferences sharedPreferences = this.sp_playlist_songs;
            if (i == sharedPreferences.getInt(this.inWhichSetAdapter + " active position", -1)) {
                holderVar.title.setTextColor(this.context.getResources().getColor(R.color.active));
                holderVar.artist.setTextColor(this.context.getResources().getColor(R.color.active));
                holderVar.icon_music.setColorFilter(this.context.getResources().getColor(R.color.active));
                return;
            }
            holderVar.title.setTextColor(this.context.getResources().getColor(R.color.white_09));
            holderVar.artist.setTextColor(this.context.getResources().getColor(R.color.white_09));
            holderVar.icon_music.setColorFilter(this.context.getResources().getColor(R.color.white_09));
        } else if (!this.fromWhichActivity.equals("album")) {
        } else {
            if (i == this.sp_album_songs.getInt(this.inWhichSetAdapter, -1)) {
                holderVar.title.setTextColor(this.context.getResources().getColor(R.color.active));
                holderVar.artist.setTextColor(this.context.getResources().getColor(R.color.active));
                holderVar.icon_music.setColorFilter(this.context.getResources().getColor(R.color.active));
                return;
            }
            holderVar.title.setTextColor(this.context.getResources().getColor(R.color.white_09));
            holderVar.artist.setTextColor(this.context.getResources().getColor(R.color.white_09));
            holderVar.icon_music.setColorFilter(this.context.getResources().getColor(R.color.white_09));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.songsList.size();
    }

    /* loaded from: classes.dex */
    public class holder extends RecyclerView.ViewHolder {
        LinearLayoutCompat LLcompat;
        TextView artist;
        ImageView icon_more;
        ImageView icon_music;
        TextView title;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public holder(View view) {
            super(view);
//hiding            songsAdapter.this = r2;
            this.icon_music = (ImageView) view.findViewById(R.id.icon_music);
            this.title = (TextView) view.findViewById(R.id.item_title);
            this.artist = (TextView) view.findViewById(R.id.item_artist);
            this.LLcompat = (LinearLayoutCompat) view.findViewById(R.id.LLcompat);
            view.setOnClickListener(new View.OnClickListener() { // from class: com.developer404.music.adapters.songsAdapter.holder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    songsAdapter.this.R_interface.onItemClick(holder.this.getAdapterPosition());
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.developer404.music.adapters.songsAdapter.holder.2
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view2) {
                    songsAdapter.this.R_interface.onLongItemClick(holder.this.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public void updateFilteredSong(ArrayList<model> arrayList) {
        this.songsList = arrayList;
        notifyDataSetChanged();
    }
}
