package com.lanltn.musicplayerservice.sample;

import com.lanltn.musicplayerservice.model.Song;

import java.util.List;

public interface IPlayerComponentView {
    void onStartPlayer();

    void onFullPlayerMode();

    void onMiniPlayerMode();

    void onPlayingMusic();

    void onPauseMusic();

    void onStopMusic();

    void onPlayNextSong();

    void onPlayPreviousSong();

    void executePlayerWithIndex(int indexSong);

    boolean getStatusPlayer();

    void setStatusPlayer(boolean isPlaying);

    void setPositionMiniPlayer();

    void setDataInputSong(List<Song> dataInputSong);

    List<Song> getDataInputSong();
}
