package com.lanltn.musicplayerservice.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class Artist implements Parcelable {
    private int mID;
    private String mNameArtist;
    private String mImageUrl;
    private String[] urlImage = {
            "https://i.pinimg.com/originals/72/93/74/72937431e02a8ddeda520d1ba0ff1f74.png",
            "http://minhlacongai.com/wp-content/uploads/2018/09/t%C3%B3c-d%C3%A0i-u%E1%BB%91n-xo%C4%83n-nh%E1%BA%B9-ph%E1%BA%A7n-%C4%91u%C3%B4i-758x532.jpg",
            "https://photo-2-baomoi.zadn.vn/w1000_r1/2018_06_30_329_26715244/6f05eb58201ec940900f.jpg",
            "https://i.pinimg.com/236x/88/0a/d0/880ad0cf9a00885e09a1a82428782fdd--bigbang-live-bigbang-gd.jpg",
            "https://i.pinimg.com/736x/eb/30/c4/eb30c43eec3bafc8cd01774e743230f6--iu-hair-kpop-girls.jpg",
            "https://www.sbs.com.au/popasia/sites/sbs.com.au.popasia/files/styles/full/public/IU_eating_disorder.jpg",
            "https://data.whicdn.com/images/267356458/original.png",
            "https://vignette.wikia.nocookie.net/kpopgirls/images/d/dd/BLACKPINK_Lisa_Square_Up_Teaser_Image.png/revision/latest?cb=20180617160645"
    };
    public Random random = new Random();

    public Artist() {
        int num = random.nextInt(8);
        mID = num;
        mNameArtist = "UI";
        mImageUrl = urlImage[num];
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
