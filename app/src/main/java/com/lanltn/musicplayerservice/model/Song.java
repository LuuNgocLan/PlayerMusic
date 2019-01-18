package com.lanltn.musicplayerservice.model;

public class Song {
    private int mId = 1;
    private String mTitle = "Baby one more time";
    private String mSongUrl = "https://p.scdn.co/mp3-preview/5819b17392b4c33276809c5bce01fe259f2516ce?cid=73c8b632d25b48b3b537832fb728dc29";
    private Artist mArtist = new Artist(1,"britney spears","http://3.bp.blogspot.com/-mCZgOUR7yRo/U-6EmDdXbyI/AAAAAAAAAF8/nKWKkNrgFJU/s1600/Britney%2Bspears.jpg");

    public Artist getmArtist() {
        return mArtist;
    }

    public void setmArtist(Artist mArtist) {
        this.mArtist = mArtist;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSongUrl() {
        return mSongUrl;
    }

    public void setmSongUrl(String mSongUrl) {
        this.mSongUrl = mSongUrl;
    }

    public Song() {
    }

    public Song(int mId, String mTitle, String mSongUrl, Artist mArtist) {

        this.mId = mId;
        this.mTitle = mTitle;
        this.mSongUrl = mSongUrl;
        this.mArtist = mArtist;
    }
}
