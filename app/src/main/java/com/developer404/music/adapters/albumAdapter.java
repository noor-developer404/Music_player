package com.developer404.music.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.developer404.music.R;
import com.developer404.music.interfaces.RecyclerViewClickInterface;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class albumAdapter extends RecyclerView.Adapter<albumAdapter.holder> {
    RecyclerViewClickInterface R_interface_playlists;
    ArrayList<String> album_array;
    Context context;

    public albumAdapter(ArrayList<String> arrayList, Context context, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.album_array = arrayList;
        this.context = context;
        this.R_interface_playlists = recyclerViewClickInterface;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album, viewGroup, false));
    }

    public void onBindViewHolder(holder holderVar, int i) {
        holderVar.album_title.setText(this.album_array.get(i));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.album_array.size();
    }

    /* loaded from: classes.dex */
    public class holder extends RecyclerView.ViewHolder {
        TextView album_title;
        ShapeableImageView folder;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public holder(View view) {
            super(view);
// hiding           albumAdapter.this = r2;
            this.folder = (ShapeableImageView) view.findViewById(R.id.item_album_img);
            this.album_title = (TextView) view.findViewById(R.id.item_album_title);
            this.folder.setOnClickListener(new View.OnClickListener() { // from class: com.developer404.music.adapters.albumAdapter.holder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    albumAdapter.this.R_interface_playlists.onItemClick(holder.this.getAdapterPosition());
                }
            });
            this.folder.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.developer404.music.adapters.albumAdapter.holder.2
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view2) {
                    albumAdapter.this.R_interface_playlists.onLongItemClick(holder.this.getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
