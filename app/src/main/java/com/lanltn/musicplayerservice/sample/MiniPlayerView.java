package com.lanltn.musicplayerservice.sample;

import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lanltn.musicplayerservice.model.Artist;
import com.lanltn.musicplayerservice.model.Song;
import com.lanltn.musicplayerservice.utils.AnimationUtils;
import com.lanltn.musicplayerservice.R;

public class MiniPlayerView extends RelativeLayout implements IMiniPlayerView {

    View mViewRoot;
    ConstraintLayout clContainer;
    PlayerSeekBarView mPlayerSeekBarView;
    TextView txtPlayerBarTitle;
    TextView txtPlayerBarSubTitle;
    TextView txtFesName;
    ImageView imgPlayerBar;
    ImageView imgStar;
    ImageView imgPlayerToggle;
    View mViewFakeToggle;
    IPlayerComponentView iPlayerComponentView;

    private RelativeLayout mParentContainer;

    private Artist mArtist;
    private boolean isSwipedUpPlayer;
    private int hardHeightOfTabBar;
    private boolean mIsShowing = false;
    private boolean mIsPlaying = false;
    private boolean mIsPaused = false;

    public void setiPlayerComponentView(IPlayerComponentView iPlayerComponentView) {
        this.iPlayerComponentView = iPlayerComponentView;
        mIsPlaying = iPlayerComponentView.getStatusPlayer();
        //init all status data in View mini Player
        setSelectedTogglePlayerView(mIsPlaying);
    }

    public MiniPlayerView(Context context) {
        super(context);
        init(context);
    }

    public MiniPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MiniPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mViewRoot = inflate(context, R.layout.partial_player_bar_view, this);
        clContainer = mViewRoot.findViewById(R.id.constraint_layout_container);
        mPlayerSeekBarView = mViewRoot.findViewById(R.id.player_seek_bar);
        imgPlayerToggle = mViewRoot.findViewById(R.id.image_view_player_toggle);
        txtPlayerBarTitle = mViewRoot.findViewById(R.id.text_view_player_bar_title);
        txtPlayerBarSubTitle = mViewRoot.findViewById(R.id.text_view_player_bar_sub_title);
        txtFesName = mViewRoot.findViewById(R.id.text_view_player_bar_fes_name);
        imgPlayerBar = mViewRoot.findViewById(R.id.image_view_player_bar);
        imgStar = mViewRoot.findViewById(R.id.image_view_player_bar_star);
        mViewFakeToggle = mViewRoot.findViewById(R.id.view_toggle_fake_animation);
        //init status button
        eventSetup();

    }

    void eventSetup() {
        //Click to play button
        imgPlayerToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Button Play was clicked!", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimationUtils.animationRipple(mViewFakeToggle, 0, AnimationUtils.DURATION_PLAY_BUTTON);
                    }
                }, 100);

                // As required, touch playbar mini, sync timetable if available screen and data


                //update status of player to component View
                mIsPlaying = !mIsPlaying;
                iPlayerComponentView.setStatusPlayer(mIsPlaying);
                setSelectedTogglePlayerView(mIsPlaying);
                iPlayerComponentView.onPlayingMusic();

            }
        });

        clContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //call method to show Full mode
                iPlayerComponentView.onFullPlayerMode();
            }
        });

    }

    public void setSelectedTogglePlayerView(boolean isPlay) {
        imgPlayerToggle.setSelected(isPlay);
    }

    /**
     * update name Artist, name fes, name Music
     * update image Artist
     */
    public void setSongInfo(Song song) {
        txtPlayerBarTitle.setText(song.getName());
        txtPlayerBarSubTitle.setText(song.getArtistName());

        Glide
                .with(getContext())
                .load(song.getImage())
                .apply((new RequestOptions()).error(R.drawable.img_artist))
                .into(imgPlayerBar);
    }

    interface IMiniPlayerListener extends IBasePlayer {

        void onClickBarView();
    }
}
