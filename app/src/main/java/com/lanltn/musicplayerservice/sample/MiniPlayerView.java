package com.lanltn.musicplayerservice.sample;

import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lanltn.musicplayerservice.MainActivity;
import com.lanltn.musicplayerservice.model.Artist;
import com.lanltn.musicplayerservice.model.Song;
import com.lanltn.musicplayerservice.utils.AnimationUtils;
import com.lanltn.musicplayerservice.R;
import com.lanltn.musicplayerservice.utils.AppUtils;

public class MiniPlayerView extends RelativeLayout implements IMiniPlayerView {
    private final int SCROLL_ACCELERATION = 1;
    private static final int ESTIMATE_DEFAULT_HEIGHT = 500;

    private GestureDetector mGestureDetector;
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

    private PlayerBarLocationType mLocationLevel = PlayerBarLocationType.PLAYER_HIDDEN;
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
        hardHeightOfTabBar = AppUtils.dipToPx(getContext(), 50);
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
    public void setPlayerGesture(final GestureDetector gestureDetector) {

        clContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //TODO: Check show Player Full Move In Up Touched
                   // ((MainActivity) getContext()).checkShowPlayerFullMoveInUpTouched();
                }
                return !gestureDetector.onTouchEvent(event);
            }
        });
        setGestureDetector(gestureDetector);
    }

    private void setGestureDetector(GestureDetector gestureDetector) {
    }

    public PlayerSeekBarView getPlayerSeekBarView() {
        return mPlayerSeekBarView;
    }
    void eventSetup() {
        //Click to play button with animation
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

    public void setShowUpPlayerBar(boolean isShow, boolean isPopupShown, Fragment fragment) {
        if(mParentContainer==null){
            throw  new RuntimeException("Player bar parent container must not be null");
        }

        if(isShow){
            mIsShowing=true;
            mPlayerSeekBarView.updatePrimaryProgressBar(0);
            mParentContainer.setVisibility(VISIBLE);
            updatePlayerPlaceLevel(isPopupShown, fragment);
        } else {
            mIsShowing = false;
            int height;
            if(getHeight()>0){
                height = getHeight();
            } else {
                height = ESTIMATE_DEFAULT_HEIGHT;
            }

            mParentContainer.animate().translationY(height).setInterpolator(
                    new AccelerateInterpolator(SCROLL_ACCELERATION)).withEndAction(new Runnable() {
                @Override
                public void run() {
                    mParentContainer.setVisibility(GONE);
                }
            }).start();
        }
    }

    boolean inTriggerAction =false;
    int count = 0;

    public void updatePlayerPlaceLevel(final boolean isPopupFrameShown, final Fragment fragment) {

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

    /**
     * position and status of MiniPlayer
     */
    public enum PlayerBarLocationType {
        PLAYER_PLACE_BOTTOM, PLAYER_PLACE_UPPER_TABBAR, PLAYER_HIDDEN
    }

    interface IMiniPlayerListener extends IBasePlayer {

        void onClickBarView();
    }
}
