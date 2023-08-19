package com.developer404.music.others;

import java.io.Serializable;

/* loaded from: classes.dex */
public class model implements Serializable {
    String album;
    String artist;
    String data;
    String duration;
    String song_ID;
    String title;

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String str) {
        this.artist = str;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String str) {
        this.data = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String str) {
        this.album = str;
    }

    public String getSong_ID() {
        return this.song_ID;
    }

    public void setSong_ID(String str) {
        this.song_ID = str;
    }

    public model(String str, String str2, String str3, String str4, String str5, String str6) {
        this.data = str;
        this.title = str2;
        this.duration = str3;
        this.album = str4;
        this.artist = str5;
        this.song_ID = str6;
    }
}
