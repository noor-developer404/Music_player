package com.developer404.music.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.developer404.music.R;
import com.developer404.music.interfaces.RecyclerViewClickInterface;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class playlistsAdapter extends RecyclerView.Adapter<playlistsAdapter.holder> {
    RecyclerViewClickInterface R_interface_playlists;
    Context context;
    ArrayList<String> playlist_array;

    public playlistsAdapter(ArrayList<String> arrayList, Context context, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.playlist_array = arrayList;
        this.context = context;
        this.R_interface_playlists = recyclerViewClickInterface;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_playlist_rv, viewGroup, false));
    }

    public void onBindViewHolder(holder holderVar, int i) {
        holderVar.tv.setText(this.playlist_array.get(i));
        if (i == 0) {
            holderVar.tv.setText("Liked");
            holderVar.img.setImageResource(R.drawable.icon_heart_solid);
            holderVar.img.setColorFilter(new PorterDuffColorFilter(this.context.getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.playlist_array.size();
    }

    /* loaded from: classes.dex */
    public class holder extends RecyclerView.ViewHolder {
        ShapeableImageView img;
        RelativeLayout relativeLayout;
        TextView tv;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public holder(View view) {
            super(view);
// hiding           playlistsAdapter.this = r2;
            this.img = (ShapeableImageView) view.findViewById(R.id.playlist_img);
            this.tv = (TextView) view.findViewById(R.id.playlist_title);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
            this.img.setOnClickListener(new View.OnClickListener() { // from class: com.developer404.music.adapters.playlistsAdapter.holder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    playlistsAdapter.this.R_interface_playlists.onItemClick(holder.this.getAdapterPosition());
                }
            });
            this.img.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.developer404.music.adapters.playlistsAdapter.holder.2
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view2) {
                    playlistsAdapter.this.R_interface_playlists.onLongItemClick(holder.this.getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
