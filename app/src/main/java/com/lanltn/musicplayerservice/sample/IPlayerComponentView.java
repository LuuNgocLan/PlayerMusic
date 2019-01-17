package com.lanltn.musicplayerservice.sample;

public interface IPlayerComponentView {
    void onShowPlayer();

    void onFullPlayerMode();

    void onMiniPlayerMode();

    void onPlayingMusic();

    void onPauseMusic();

    void onStopMusic();

    boolean getStatusPlayer();

    void setStatusPlayer(boolean isPlaying);

    void setPositionMiniPlayer();
}
