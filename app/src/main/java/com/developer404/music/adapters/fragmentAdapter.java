package com.developer404.music.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.developer404.music.fragments.album;
import com.developer404.music.fragments.playlists;
import com.developer404.music.fragments.songs;

/* loaded from: classes.dex */
public class fragmentAdapter extends FragmentStateAdapter {
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return 3;
    }

    public fragmentAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override // androidx.viewpager2.adapter.FragmentStateAdapter
    public Fragment createFragment(int i) {
        if (i != 0) {
            if (i == 1) {
                return new playlists();
            }
            if (i == 2) {
                return new album();
            }
            return null;
        }
        return new songs();
    }
}
