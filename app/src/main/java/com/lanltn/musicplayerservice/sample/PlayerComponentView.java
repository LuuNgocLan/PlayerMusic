package com.lanltn.musicplayerservice.sample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lanltn.musicplayerservice.R;
import com.lanltn.musicplayerservice.model.Song;
import com.lanltn.musicplayerservice.service.PlayerMusicService;

import java.util.ArrayList;
import java.util.List;

public class PlayerComponentView extends RelativeLayout implements IPlayerComponentView {

    private View mViewRoot;
    private MiniPlayerView miniPlayerView;
    private FullPlayerView fullPlayerView;

    //service
    private PlayerMusicService playerMusicService;
    private Intent playIntent;
    private boolean musicBound = false;
    private boolean isPlaying;
    private List<Song> songList = new ArrayList<>();

    //==============================================================================================
    //      Contructor
    //==============================================================================================


    public PlayerComponentView(Context context) {
        super(context);
        init(context);
    }

    public PlayerComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayerComponentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflateView(context);
        initIntentPlayer();
    }

    public void initIntentPlayer() {
        if (playIntent == null) {
            playIntent = new Intent(getContext(), PlayerMusicService.class);
            getContext().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getContext().startService(playIntent);
        }
    }

    void inflateView(Context context) {

        mViewRoot = inflate(context, R.layout.layout_player_main, this);
        miniPlayerView = mViewRoot.findViewById(R.id.miniPlayerView);
        fullPlayerView = mViewRoot.findViewById(R.id.fullPlayerView);
        //when show component set status isPlaying = true;
        isPlaying = true;

        eventSetup();
    }

    void eventSetup() {

    }

    //connect to service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerMusicService.MusicBinder binder = (PlayerMusicService.MusicBinder) service;
            playerMusicService = binder.getService();

            //TODO: SET LIST SONG TO PLAY
            playerMusicService.setDataSongs(getDataInputSong());
            Toast.makeText(getContext(), "SERVICE_MUSIC DATA SONGS: " + songList.size(), Toast.LENGTH_SHORT).show();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(getContext(), "MUSIC_SERVICE DISCONNECTED!", Toast.LENGTH_SHORT).show();
            musicBound = false;
        }
    };

    @Override
    public void onStartPlayer() {
        //Show action when start music player
        Toast.makeText(getContext(), "on Show Player!!!", Toast.LENGTH_SHORT).show();

        miniPlayerView.setiPlayerComponentView(this);
        fullPlayerView.setiPlayerComponentView(this);
    }


    @Override
    public void onFullPlayerMode() {
        miniPlayerView.setVisibility(GONE);
        fullPlayerView.setVisibility(VISIBLE);
    }


    @Override
    public void onMiniPlayerMode() {
        miniPlayerView.setVisibility(VISIBLE);
        fullPlayerView.setVisibility(GONE);
    }


    @Override
    public void onPlayingMusic() {
        isPlaying = true;
        playerMusicService.playSong();
        Toast.makeText(getContext(), "Song playing!!!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPauseMusic() {
        isPlaying = false;
        playerMusicService.pauseSong(true);
        Toast.makeText(getContext(), "Song paused!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStopMusic() {
        isPlaying = false;
    }

    @Override
    public void onPlayNextSong() {
        //TODO: call service to play next song
        playerMusicService.playNextSong(true);
    }

    @Override
    public void onPlayPreviousSong() {
        //TODO: call service to play previuos song
        playerMusicService.playPreviousSong();
    }

    @Override
    public void executePlayerWithIndex(int indexSong) {
        playerMusicService.playSongWithIndex(indexSong);
    }


    @Override
    public boolean getStatusPlayer() {
        Toast.makeText(getContext(), "Status Button: " + isPlaying, Toast.LENGTH_SHORT).show();
        return isPlaying;
    }


    @Override
    public void setStatusPlayer(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }


    @Override
    public void setPositionMiniPlayer() {

    }

    @Override
    public void setDataInputSong(List<Song> dataInputSong) {
        this.songList = dataInputSong;
    }

    @Override
    public List<Song> getDataInputSong() {
        return this.songList;
    }


}
