package com.developer404.music;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.developer404.music.adapters.fragmentAdapter;
import com.developer404.music.databinding.ActivityMainBinding;
import com.developer404.music.fragments.songs;
import com.developer404.music.others.model;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {
    private static final int REQUEST_CODE = 1;
    LinearLayout LL;
    Toolbar TB;
    ActivityMainBinding binding;
    CardView cd1;
    CardView cd10;
    CardView cd11;
    CardView cd12;
    CardView cd13;
    CardView cd14;
    CardView cd15;
    CardView cd2;
    CardView cd3;
    CardView cd4;
    CardView cd5;
    CardView cd6;
    CardView cd7;
    CardView cd8;
    CardView cd9;
    Dialog dialog;
    SharedPreferences.Editor editor;
    Menu menu;
    SearchView searchView;
    ArrayList<model> songsList;
    SharedPreferences sp;
    int themeResource = 0;

    @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
    public boolean onQueryTextSubmit(String str) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityMainBinding inflate = ActivityMainBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        Toolbar toolbar = (Toolbar) findViewById(R.id.TB);
        this.TB = toolbar;
        setSupportActionBar(toolbar);
        this.TB.setTitleTextColor(getResources().getColor(R.color.white));
        Dialog dialog = new Dialog(this);
        this.dialog = dialog;
        dialog.setContentView(R.layout.dialog_theme);
        this.cd1 = (CardView) this.dialog.findViewById(R.id.cd1);
        this.cd2 = (CardView) this.dialog.findViewById(R.id.cd2);
        this.cd3 = (CardView) this.dialog.findViewById(R.id.cd3);
        this.cd4 = (CardView) this.dialog.findViewById(R.id.cd4);
        this.cd5 = (CardView) this.dialog.findViewById(R.id.cd5);
        this.cd6 = (CardView) this.dialog.findViewById(R.id.cd6);
        this.cd7 = (CardView) this.dialog.findViewById(R.id.cd7);
        this.cd8 = (CardView) this.dialog.findViewById(R.id.cd8);
        this.cd9 = (CardView) this.dialog.findViewById(R.id.cd9);
        this.cd10 = (CardView) this.dialog.findViewById(R.id.cd10);
        this.cd11 = (CardView) this.dialog.findViewById(R.id.cd11);
        this.cd12 = (CardView) this.dialog.findViewById(R.id.cd12);
        this.cd13 = (CardView) this.dialog.findViewById(R.id.cd13);
        this.cd14 = (CardView) this.dialog.findViewById(R.id.cd14);
        this.cd15 = (CardView) this.dialog.findViewById(R.id.cd15);
        this.cd1.setOnClickListener(this);
        this.cd2.setOnClickListener(this);
        this.cd3.setOnClickListener(this);
        this.cd4.setOnClickListener(this);
        this.cd5.setOnClickListener(this);
        this.cd6.setOnClickListener(this);
        this.cd7.setOnClickListener(this);
        this.cd8.setOnClickListener(this);
        this.cd9.setOnClickListener(this);
        this.cd10.setOnClickListener(this);
        this.cd11.setOnClickListener(this);
        this.cd12.setOnClickListener(this);
        this.cd13.setOnClickListener(this);
        this.cd14.setOnClickListener(this);
        this.cd15.setOnClickListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences("appTheme", 0);
        this.sp = sharedPreferences;
        this.editor = sharedPreferences.edit();
        if (this.sp.getInt("themeResourceID", 0) == 0) {
            this.themeResource = R.drawable.gradient_gr;
            this.editor.putInt("themeResourceID", R.drawable.gradient_gr);
            this.editor.apply();
            getWindow().setBackgroundDrawableResource(this.themeResource);
        } else if (this.sp.getInt("themeResourceID", 0) != 0) {
            spFetch_setTheme();
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            return;
        }
        this.songsList = fetch_all_music();
        Log.e("songsList", "onCreateView: " + this.songsList);
        this.binding.mainTL.setBackground(null);
        this.binding.mainVP.setAdapter(new fragmentAdapter(this));
        new TabLayoutMediator(this.binding.mainTL, this.binding.mainVP, new TabLayoutMediator.TabConfigurationStrategy() { // from class: com.developer404.music.MainActivity.1
            @Override // com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
            public void onConfigureTab(TabLayout.Tab tab, int i) {
                if (i == 0) {
                    tab.setText("Songs");
                } else if (i == 1) {
                    tab.setText("Playlists");
                } else if (i != 2) {
                } else {
                    tab.setText("Albums");
                }
            }
        }).attach();
    }

    public void forWhite() {
        if (this.sp.getInt("themeResourceID", 0) == R.color.white) {
            this.TB.setTitleTextColor(getResources().getColor(R.color.dark));
            PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(getResources().getColor(R.color.dark), PorterDuff.Mode.MULTIPLY);
            this.menu.findItem(R.id.li_search);
            MenuItem findItem = this.menu.findItem(R.id.li_themes);
            MenuItem findItem2 = this.menu.findItem(R.id.li_settings);
            findItem.getIcon().setColorFilter(porterDuffColorFilter);
            findItem2.getIcon().setColorFilter(porterDuffColorFilter);
        }
    }

    public void forothers() {
        this.TB.setTitleTextColor(getResources().getColor(R.color.white));
        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        this.menu.findItem(R.id.li_search);
        MenuItem findItem = this.menu.findItem(R.id.li_themes);
        MenuItem findItem2 = this.menu.findItem(R.id.li_settings);
        findItem.getIcon().setColorFilter(porterDuffColorFilter);
        findItem2.getIcon().setColorFilter(porterDuffColorFilter);
    }

    public void spFetch_setTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("appTheme", 0);
        getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        getWindow().setBackgroundDrawableResource(sharedPreferences.getInt("themeResourceID", 0));
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        this.menu = menu;
        forWhite();
        SearchView searchView = (SearchView) menu.findItem(R.id.li_search).getActionView();
        this.searchView = searchView;
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != R.id.li_search) {
            if (itemId == R.id.li_themes) {
                this.dialog.show();
            } else if (itemId == R.id.li_settings) {
                Intent intent = new Intent(this, settings.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cd1 /* 2131230832 */:
                this.themeResource = R.drawable.gradient_gr;
                this.editor.putInt("themeResourceID", R.drawable.gradient_gr);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
            case R.id.cd10 /* 2131230833 */:
                this.themeResource = R.drawable.gradient_dark;
                this.editor.putInt("themeResourceID", R.drawable.gradient_dark);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
            case R.id.cd11 /* 2131230834 */:
                this.themeResource = R.color.green;
                this.editor.putInt("themeResourceID", R.color.green);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
            case R.id.cd12 /* 2131230835 */:
                this.themeResource = R.color.voilet;
                this.editor.putInt("themeResourceID", R.color.voilet);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
            case R.id.cd13 /* 2131230836 */:
                this.themeResource = R.color.purple_700;
                this.editor.putInt("themeResourceID", R.color.purple_700);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
            case R.id.cd14 /* 2131230837 */:
                this.themeResource = R.drawable.gradient_red;
                this.editor.putInt("themeResourceID", R.drawable.gradient_red);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
            case R.id.cd15 /* 2131230838 */:
                this.themeResource = R.color.brownis_blue;
                this.editor.putInt("themeResourceID", R.color.brownis_blue);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
            case R.id.cd2 /* 2131230839 */:
                this.themeResource = R.color.yellow;
                this.editor.putInt("themeResourceID", R.color.yellow);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
            case R.id.cd3 /* 2131230840 */:
                this.themeResource = R.color.orange;
                this.editor.putInt("themeResourceID", R.color.orange);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
            case R.id.cd4 /* 2131230841 */:
                this.themeResource = R.drawable.gradient_pb;
                this.editor.putInt("themeResourceID", R.drawable.gradient_pb);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
            case R.id.cd5 /* 2131230842 */:
                this.themeResource = R.drawable.gradient_green;
                this.editor.putInt("themeResourceID", R.drawable.gradient_green);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
            case R.id.cd6 /* 2131230843 */:
                this.themeResource = R.color.brown;
                this.editor.putInt("themeResourceID", R.color.brown);
                this.editor.apply();
                this.dialog.dismiss();
                break;
            case R.id.cd7 /* 2131230844 */:
                this.themeResource = R.color.light_blue;
                this.editor.putInt("themeResourceID", R.color.light_blue);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
            case R.id.cd8 /* 2131230845 */:
                this.themeResource = R.drawable.gradient_orange;
                this.editor.putInt("themeResourceID", R.drawable.gradient_orange);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
            case R.id.cd9 /* 2131230846 */:
                this.themeResource = R.color.greenis_yellow;
                this.editor.putInt("themeResourceID", R.color.greenis_yellow);
                this.editor.apply();
                forothers();
                this.dialog.dismiss();
                break;
        }
        spFetch_setTheme();
    }

    public boolean isServiceRunning(Context context, Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (runningServiceInfo.service.getClassName().equals(cls.getName())) {
                return true;
            }
        }
        return false;
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

    @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
    public boolean onQueryTextChange(String str) {
        String lowerCase = str.toLowerCase();
        ArrayList<model> arrayList = new ArrayList<>();
        for (int i = 0; i < this.songsList.size(); i++) {
            if (this.songsList.get(i).getTitle().toLowerCase().contains(lowerCase)) {
                arrayList.add(this.songsList.get(i));
            }
        }
        songs.songsList2 = arrayList;
        songs.adapter.updateFilteredSong(arrayList);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (!this.searchView.isIconified()) {
            this.searchView.onActionViewCollapsed();
        } else {
            moveTaskToBack(true);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1) {
            if (iArr[0] == 0) {
                this.songsList = fetch_all_music();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return;
            }
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }
}
