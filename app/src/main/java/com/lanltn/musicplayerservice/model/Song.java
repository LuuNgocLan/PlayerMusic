package com.lanltn.musicplayerservice.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Song implements Parcelable {
    public static final int SONG_CHOOSE_PLAY = 2;
    public static final int SONG_CHOOSE_PAUSE = 1;
    public static final int SONG_NOT_CHOOSE = 0;

    private String id;
    private String name;
    @SerializedName("preview_url")
    private String url;
    @SerializedName("duration_ms")
    private double duration;

    private int artistId;
    private String image;
    private String artistName;
    private int statusPlay;
    private int mCountTrack;

    public Song(Parcel in) {
        id = in.readString();
        name = in.readString();
        url = in.readString();
        duration = in.readDouble();
        artistId = in.readInt();
        image = in.readString();
        artistName = in.readString();
        statusPlay = in.readInt();
        mCountTrack = in.readInt();
    }

    public Song(String id, String name, String url, double duration, int artistId, String image, String artistName, int statusPlay, int mCountTrack) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.duration = duration;
        this.artistId = artistId;
        this.image = image;
        this.artistName = artistName;
        this.statusPlay = statusPlay;
        this.mCountTrack = mCountTrack;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeDouble(duration);
        dest.writeInt(artistId);
        dest.writeString(image);
        dest.writeString(artistName);
        dest.writeInt(statusPlay);
        dest.writeInt(mCountTrack);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getStatusPlay() {
        return statusPlay;
    }

    public void setStatusPlay(int statusPlay) {
        this.statusPlay = statusPlay;
    }

    public int getmCountTrack() {
        return mCountTrack;
    }

    public void setmCountTrack(int mCountTrack) {
        this.mCountTrack = mCountTrack;
    }
}
