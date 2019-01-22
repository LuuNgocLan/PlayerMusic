package com.lanltn.musicplayerservice.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class Artist implements Parcelable {
    private int mID;
    private String mNameArtist;
    private String mImageUrl;

    public Artist() {
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmNameArtist() {
        return mNameArtist;
    }

    public void setmNameArtist(String mNameArtist) {
        this.mNameArtist = mNameArtist;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public Artist(int mID, String mNameArtist, String mImageUrl) {

        this.mID = mID;
        this.mNameArtist = mNameArtist;
        this.mImageUrl = mImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
