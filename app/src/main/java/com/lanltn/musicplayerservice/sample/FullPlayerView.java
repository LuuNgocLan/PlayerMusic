package com.lanltn.musicplayerservice.sample;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lanltn.musicplayerservice.R;
import com.lanltn.musicplayerservice.adapter.PlayerSlideShowAdapter;
import com.lanltn.musicplayerservice.adapter.PlaylistArtistAdapter;
import com.lanltn.musicplayerservice.model.Artist;
import com.lanltn.musicplayerservice.model.Song;
import com.lanltn.musicplayerservice.utils.AnimationUtils;
import com.lanltn.musicplayerservice.utils.AppUtils;
import com.lanltn.musicplayerservice.utils.ScreenUtils;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import static com.lanltn.musicplayerservice.model.Song.SONG_NOT_CHOOSE;

public class FullPlayerView extends RelativeLayout
        implements
        IFullPlayerView,
        PlayerSlideShowAdapter.ISlideShowViewHolderListener,
        PlaylistArtistAdapter.IPlayListViewHolderListener, Transition.TransitionListener {

    View mViewRoot;
    ConstraintLayout clContainer;
    ConstraintLayout mClController;
    ConstraintLayout mClContent;
    LinearLayout mLlButtonActionContainer;
    ImageView mImgArtist;
    TextView mTxtFesName;
    DiscreteScrollView mSlideShowPlayer;
    PlayerSeekBarView playerSeekBar;
    ImageView imgPlayerToggle;
    ImageView imgSkipPrevious;
    ImageView imgSkipNext;
    TextView txtCurrentTime;
    TextView txtLeftTime;
    TextView txtArtist;
    TextView txtSongName;
    ImageView mImgFavouriteHeader;
    ImageView mImgFavouriteBody;
    LinearLayout mLinearLayoutWatch;
    LinearLayout mLinearLayoutShopping;
    TextView mTxtPlayList;
    RecyclerView mPlayerRecyclerView;
    LinearLayout mLinearLayoutPlaylist;
    View artistPlayView;
    ImageView imgViewClose;

    private ConstraintSet mConstraintSetContainer = new ConstraintSet();
    private IPlayerComponentView iPlayerComponentView;
    private PlayerSlideShowAdapter mPlayerSlideShowAdapter;
    private PlaylistArtistAdapter mPlaylistArtistAdapter;
    private List<Song> mListSong = new ArrayList<>();

    private int mLastPage;
    private boolean mIsShowSlide = true;
    private boolean mIsArtistPlayer;
    private boolean isHiddenPlaylist;
    private int maxPaddingBottomController;
    private int heightPlaylistHidingStage = 0;
    private boolean mIsPlaying = true;
    private boolean alreadyExecutePlayer;
    private int mCurrentIndex = 0;

    /**
     * Settup listenner Parent View, data songs and init Slide/Playlist
     *
     * @param iPlayerComponentView
     */
    public void setiPlayerComponentView(IPlayerComponentView iPlayerComponentView) {
        this.iPlayerComponentView = iPlayerComponentView;
        this.mListSong = iPlayerComponentView.getDataInputSong();
        initSlideShow();
        initPlayListArtist();
    }

    public FullPlayerView(Context context) {
        super(context);
        init(context);
    }

    public FullPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FullPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflateView(context);
        setUpEvent();
    }

    private void inflateView(Context context) {
        mViewRoot = inflate(context, R.layout.partial_player_full_view, this);
        clContainer = mViewRoot.findViewById(R.id.constraint_layout_container);
        mClController = mViewRoot.findViewById(R.id.player_contraint_layout_controller);
        mClContent = mViewRoot.findViewById(R.id.player_constraint_layout_body);
        mLlButtonActionContainer = mViewRoot.findViewById(R.id.player_full_linear_layout_buttons);
        mImgArtist = mViewRoot.findViewById(R.id.img_artist);
        mTxtFesName = mViewRoot.findViewById(R.id.tv_fes_name);
        mSlideShowPlayer = mViewRoot.findViewById(R.id.slide_show_player);
        playerSeekBar = mViewRoot.findViewById(R.id.player_seek_bar);
        imgPlayerToggle = mViewRoot.findViewById(R.id.image_view_player_toggle);
        imgSkipPrevious = mViewRoot.findViewById(R.id.image_view_skip_previous);
        imgSkipNext = mViewRoot.findViewById(R.id.image_view_skip_next);
        txtCurrentTime = mViewRoot.findViewById(R.id.text_view_current_time);
        txtLeftTime = mViewRoot.findViewById(R.id.text_view_left_time);
        txtArtist = mViewRoot.findViewById(R.id.text_view_player_full_artist);
        txtSongName = mViewRoot.findViewById(R.id.text_view_player_full_song);
        mImgFavouriteHeader = mViewRoot.findViewById(R.id.image_view_player_full_star);
        mImgFavouriteBody = mViewRoot.findViewById(R.id.player_header_image_view_favourite);
        mLinearLayoutWatch = mViewRoot.findViewById(R.id.image_view_watch);
        mLinearLayoutShopping = mViewRoot.findViewById(R.id.image_view_shopping);
        mTxtPlayList = mViewRoot.findViewById(R.id.player_full_text_view_playlist);
        mPlayerRecyclerView = mViewRoot.findViewById(R.id.player_full_recycler_view);
        mLinearLayoutPlaylist = mViewRoot.findViewById(R.id.player_full_linear_playlist);
        artistPlayView = mViewRoot.findViewById(R.id.artist_view_play);
        imgViewClose = mViewRoot.findViewById(R.id.image_view_close);
        calculateMaxPadding();
    }

    /**
     * Initial play list of Fes/Artist
     */
    private void initPlayListArtist() {
        mPlayerRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mPlaylistArtistAdapter = new PlaylistArtistAdapter(getContext(), mListSong);
        mPlaylistArtistAdapter.setmViewHolderListener(this);
        mPlayerRecyclerView.setAdapter(mPlaylistArtistAdapter);
        ((SimpleItemAnimator) mPlayerRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    //TODO: Note this ==> special: mConstraitSetContainer.clone(clContainer)
    private void calculateMaxPadding() {
        if (maxPaddingBottomController == 0) {
            mConstraintSetContainer.clone(clContainer);
            heightPlaylistHidingStage = (int) getResources().getDimension(R.dimen.pl10_padding_bottom);
            mClController.post(new Runnable() {
                @Override
                public void run() {
                    float heightHeader = getResources().getDimension(R.dimen.standard_height_bar);
                    float heightController = mClController.getHeight();
                    maxPaddingBottomController = (int) (ScreenUtils.getScreenHeight(getContext()) - heightHeader - heightController);
                }
            });
        }
    }

    /**
     * initial Slide and add listen when item Slide changed ==> update UI
     */
    private void initSlideShow() {
        mPlayerSlideShowAdapter = new PlayerSlideShowAdapter(getContext(), mListSong);
        mPlayerSlideShowAdapter.setmViewHolderListener(this);
        mSlideShowPlayer.setAdapter(mPlayerSlideShowAdapter);
        mSlideShowPlayer.scrollToPosition(0);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mSlideShowPlayer.getLayoutParams();
        if (AppUtils.pxToDip(ScreenUtils.getScreenHeight(getContext()), getContext()) > ScreenUtils.STANDARD_HEIGHT_DENSITY_DP) {
            layoutParams.height = (int) ((ScreenUtils.getScreenWidth(getContext()) - AppUtils.dipToPx(getContext(), 80)) * 1.1f);
            mSlideShowPlayer.setLayoutParams(layoutParams);
            mSlideShowPlayer.setItemTransformer(new ScaleTransformer.Builder()
                    .setMaxScale(1.1f)
                    .build());
        } else {
            layoutParams.height = (int) ((ScreenUtils.getScreenWidth(getContext()) - AppUtils.dipToPx(getContext(), 80)) * 1f);
            mSlideShowPlayer.setItemTransformer(new ScaleTransformer.Builder()
                    .setMaxScale(1f)
                    .setMinScale(0.85f)
                    .build());
        }

        mSlideShowPlayer.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder vilayerewHolder, int adapterPosition) {
                Toast.makeText(getContext(), "POSITION: " + adapterPosition, Toast.LENGTH_SHORT).show();
                if (adapterPosition == 0) {
                    imgSkipPrevious.setEnabled(false);
                    imgSkipNext.setEnabled(true);
                } else if (adapterPosition == mListSong.size()) {
                    imgSkipPrevious.setEnabled(true);
                    imgSkipNext.setEnabled(false);
                } else {
                    imgSkipPrevious.setEnabled(true);
                    imgSkipNext.setEnabled(true);
                }

                if (mLastPage != adapterPosition) {
                    mLastPage = adapterPosition;
                    if (!alreadyExecutePlayer) {
                        iPlayerComponentView.executePlayerWithIndex(adapterPosition);
                    } else {
                        alreadyExecutePlayer = false;
                    }
                }
            }
        });
    }

    /**
     * All event in Full Player Mode
     */
    private void setUpEvent() {
        imgViewClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iPlayerComponentView.onMiniPlayerMode();
            }
        });

        mLinearLayoutPlaylist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayoutPlaylist.setEnabled(false);

                mIsShowSlide = !mIsShowSlide;
                setHidePlaylist(mIsShowSlide);
            }
        });

        //Todo: show artist detail when click on title artist
        txtArtist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imgPlayerToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimationUtils.animationRipple(artistPlayView, 0, AnimationUtils.DURATION_PLAY_BUTTON);
                    }
                }, 50);

                mIsPlaying = !mIsPlaying;
                imgPlayerToggle.setSelected(mIsPlaying);
                if (iPlayerComponentView != null) {
                    if (mIsPlaying) {
                        iPlayerComponentView.onPlayingMusic();
                    } else {
                        iPlayerComponentView.onPauseMusic();
                    }
                }
            }
        });

        imgSkipNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: notify state already play
                if (mLastPage == mListSong.size() - 1) {
                    imgSkipNext.setEnabled(false);
                } else {
                    iPlayerComponentView.onPlayNextSong();
                    imgSkipNext.setEnabled(true);
                }
            }
        });

        imgSkipPrevious.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLastPage == 0) {
                    imgSkipPrevious.setEnabled(false);
                } else {
                    //Todo: notify state already play

                    iPlayerComponentView.onPlayPreviousSong();
                    imgSkipPrevious.setEnabled(true);
                }
            }
        });

    }


    /**
     * show/hide list song
     *
     * @param isHidden
     */
    private void setHidePlaylist(boolean isHidden) {
        AutoTransition transition = new AutoTransition();
        transition.addListener(this);
        transition.excludeChildren(mPlayerRecyclerView, true);
        if (!isHidden) {
            mTxtPlayList.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down_white_small, 0);
            mConstraintSetContainer.connect(R.id.player_full_linear_layout_buttons, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);

//            mConstraintSetContainer.connect(R.id.player_contraint_layout_controller, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, maxPaddingBottomController);
            mConstraintSetContainer.clear(R.id.player_contraint_layout_controller, ConstraintSet.BOTTOM);
            mConstraintSetContainer.connect(R.id.player_contraint_layout_controller, ConstraintSet.TOP, R.id.player_constraint_layout_header, ConstraintSet.BOTTOM, 0);

            TransitionManager.beginDelayedTransition(clContainer, transition);
            mConstraintSetContainer.applyTo(clContainer);

            //check if artist
            if (mIsArtistPlayer) {
                mImgFavouriteBody.setVisibility(VISIBLE);
            } else {
                mImgFavouriteBody.setVisibility(GONE);
            }
        } else {
            int maxMarginButton = (int) getResources().getDimension(R.dimen.standard_height_bar);
            mTxtPlayList.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_white_small, 0);

            mConstraintSetContainer.clear(R.id.player_contraint_layout_controller, ConstraintSet.TOP);
            mConstraintSetContainer.connect(R.id.player_contraint_layout_controller, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, heightPlaylistHidingStage);

            mConstraintSetContainer.connect(R.id.player_full_linear_layout_buttons, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, maxMarginButton);
            TransitionManager.beginDelayedTransition(clContainer, transition);
            mConstraintSetContainer.applyTo(clContainer);
            mImgFavouriteBody.setVisibility(GONE);
        }
        isHiddenPlaylist = isHidden;
    }

    private void resetPlayer() {
        playerSeekBar.updatePrimaryProgressBar(0);
        txtCurrentTime.setText("00:00");
        txtLeftTime.setText("00:00");
    }

    public void movePreviousSong(int index) {
        alreadyExecutePlayer = true;
        if (mListSong.size() == 0 || index >= mListSong.size() || index < 0) {
            return;
        }
        mListSong.get(index).setStatusPlay(Song.SONG_CHOOSE_PLAY);
        mPlaylistArtistAdapter.notifyItemChanged(index);
        mSlideShowPlayer.smoothScrollToPosition(index);
        resetPlayer();
    }

    public void moveNextSong(int index) {
        alreadyExecutePlayer = true;
        if (mListSong.size() == 0 || index >= mListSong.size() || index < 0) {
            return;
        }
        mListSong.get(index).setStatusPlay(Song.SONG_CHOOSE_PLAY);
        mPlaylistArtistAdapter.notifyItemChanged(index);
        mSlideShowPlayer.smoothScrollToPosition(index);
        if (mListSong.size() == 1) {
            imgSkipNext.setEnabled(true);
        }
        resetPlayer();
    }

    /**
     * Change status of button
     *
     * @param isPlaying
     */
    public void changePlayerToggleState(boolean isPlaying) {
        if (mListSong.size() == 0 || mCurrentIndex >= mListSong.size() || mCurrentIndex < 0) {
            return;
        }

        getImgPlayerToggle().setSelected(isPlaying);
        if (isPlaying) {
            mListSong.get(mCurrentIndex).setStatusPlay(Song.SONG_CHOOSE_PLAY);
            getImgPlayerToggle().setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.selector_pause_normal));
        } else {
            getImgPlayerToggle().setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.selector_play_nomarl));
            mListSong.get(mCurrentIndex).setStatusPlay(Song.SONG_CHOOSE_PAUSE);
        }

        for (int i = 0; i < mListSong.size(); i++) {
            if (i != mCurrentIndex && mListSong.get(i).getStatusPlay() != SONG_NOT_CHOOSE) {
                mListSong.get(i).setStatusPlay(SONG_NOT_CHOOSE);
                mPlaylistArtistAdapter.notifyItemChanged(i);
            }
        }
        mPlaylistArtistAdapter.notifyItemChanged(mCurrentIndex);
    }

    private ImageView getImgPlayerToggle() {
        return imgPlayerToggle;
    }

    public void updateAdapter(List<Song> songs) {
        mSlideShowPlayer.scrollToPosition(0);
        mPlayerRecyclerView.smoothScrollToPosition(0);
        mListSong.clear();
        mListSong.addAll(songs);
        mListSong.get(0).setStatusPlay(2);
        mPlayerSlideShowAdapter.updateAdapter(mListSong);
        mPlaylistArtistAdapter.updateAdapter(mListSong);
    }

    public void updateTime(int currentTime, int leftTime) {
        String currentTimeText = getTimeInText(getMinuteFromTime(currentTime)) + ":" + getTimeInText((currentTime));
        txtCurrentTime.setText(currentTimeText);

        String leftTimeText = "-" + getTimeInText(getMinuteFromTime(leftTime)) + ":" + getTimeInText(getSecondFromTime(leftTime));
        txtLeftTime.setText(leftTimeText);
    }

    private String getTimeInText(int time) {
        if (time < 10)
            return "0" + time;

        return time + "";
    }

    private int getMinuteFromTime(int time) {
        return time / 60;
    }

    private int getSecondFromTime(int time) {
        return time - (getMinuteFromTime(time) * 60);
    }

    public void moveNextSongIndexed() {

    }

    /**
     * Set infor of Song on View
     *
     * @param isArtistPlayer
     * @param artist
     * @param song
     * @param index
     * @param spotifyPlaylistId
     */
    public void setSongInfor(boolean isArtistPlayer, Artist artist, Song song, int index, String spotifyPlaylistId) {

    }

    public void updateFavoriteView(boolean isFavorite, boolean isEnable) {
        mImgFavouriteHeader.setSelected(isFavorite);
        mImgFavouriteBody.setSelected(isFavorite);
    }

    public void setEnableFavourite() {
        mImgFavouriteBody.setEnabled(true);
        mImgFavouriteHeader.setEnabled(true);
    }


    @Override
    public void onClickPlayMusic(int artistId) {

    }

    @Override
    public void onClickThumbnail(int position) {

    }

    @Override
    public void onTransitionStart(Transition transition) {
        mLinearLayoutPlaylist.setEnabled(false);
    }

    @Override
    public void onTransitionEnd(Transition transition) {
        mLinearLayoutPlaylist.setEnabled(true);
    }

    @Override
    public void onTransitionCancel(Transition transition) {

    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {

    }

    @Override
    public void onClickSongRecyclerView(int position) {
        if (position == mSlideShowPlayer.getCurrentItem()) {
            if (iPlayerComponentView != null) {
                iPlayerComponentView.executePlayerWithIndex(position);
            }
        } else {
            if (position >= mListSong.size() || position < 0) {
                return;
            }
            mListSong.get(position).setStatusPlay(Song.SONG_CHOOSE_PLAY);
            alreadyExecutePlayer = false;
            mSlideShowPlayer.smoothScrollToPosition(position);
        }
    }

    interface IPlayerFullListener extends IBasePlayer {

    }
}
