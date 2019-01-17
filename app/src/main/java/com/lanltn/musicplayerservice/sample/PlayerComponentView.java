package com.lanltn.musicplayerservice.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lanltn.musicplayerservice.R;

public class PlayerComponentView extends RelativeLayout implements IPlayerComponentView {
    private View mViewRoot;
    private MiniPlayerView miniPlayerView;
    private FullPlayerView fullPlayerView;
    private boolean isPlaying;

    //    private XyZService ;
    public PlayerComponentView(Context context) {
        super(context);
        inflateView(context);
    }

    public PlayerComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView(context);
    }

    public PlayerComponentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context);
    }

    void inflateView(Context context) {
        mViewRoot = inflate(context, R.layout.layout_player_main, this);
        miniPlayerView = mViewRoot.findViewById(R.id.miniPlayerView);
        fullPlayerView = mViewRoot.findViewById(R.id.fullPlayerView);

        //when show component set status isPlaying = true;
        isPlaying = true;

        miniPlayerView.setiPlayerComponentView(this);
        fullPlayerView.setiPlayerComponentView(this);

        eventSetup();
    }

    void eventSetup() {

    }

    @Override
    public void onShowPlayer() {
        //Show action when start music player
        Toast.makeText(getContext(), "on Show Player!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFullPlayerMode() {
        miniPlayerView.setVisibility(GONE);
        fullPlayerView.setVisibility(VISIBLE);
        Toast.makeText(getContext(), "on Show with Full mode Player!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMiniPlayerMode() {
        miniPlayerView.setVisibility(VISIBLE);
        fullPlayerView.setVisibility(GONE);
        Toast.makeText(getContext(), "on Show with Mini Player!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlayingMusic() {
        isPlaying = true;
    }

    @Override
    public void onPauseMusic() {
        isPlaying = false;
    }

    @Override
    public void onStopMusic() {
        isPlaying = false;
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


}
