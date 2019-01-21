package com.lanltn.musicplayerservice.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayerState implements Parcelable {

    private String key;
    private int valueInt;
    private Song song;
    private Artist artist;
    private int currentTime;
    private int leftTime;
    private boolean valueBoolean;
    private boolean valueBooleanSecondState; // Secondary state
    private boolean valueBooleanRequestedApi = true;
    private String spotifyPlaylistId;

    public PlayerState(Parcel in) {
        key = in.readString();
        valueInt = in.readInt();
        valueBoolean = in.readByte() != 0;
        valueBooleanSecondState = in.readByte() != 0;
        valueBooleanRequestedApi = in.readByte() != 0;
        song = in.readParcelable(Song.class.getClassLoader());
        artist = in.readParcelable(Artist.class.getClassLoader());
        currentTime = in.readInt();
        leftTime = in.readInt();
        spotifyPlaylistId = in.readString();
    }

    public static final Creator<PlayerState> CREATOR = new Creator<PlayerState>() {
        @Override
        public PlayerState createFromParcel(Parcel in) {
            return new PlayerState(in);
        }

        @Override
        public PlayerState[] newArray(int size) {
            return new PlayerState[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeInt(valueInt);
        dest.writeByte((byte) (valueBoolean ? 1 : 0));
        dest.writeByte((byte) (valueBooleanSecondState ? 1 : 0));
        dest.writeByte((byte) (valueBooleanRequestedApi ? 1 : 0));
        dest.writeParcelable(song, flags);
        dest.writeParcelable(artist, flags);
        dest.writeInt(currentTime);
        dest.writeInt(leftTime);
        dest.writeString(spotifyPlaylistId);
    }
}