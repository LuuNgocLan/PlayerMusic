package com.lanltn.musicplayerservice.sample;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lanltn.musicplayerservice.MainActivity;
import com.lanltn.musicplayerservice.R;
import com.lanltn.musicplayerservice.adapter.PlayerSlideShowAdapter;
import com.lanltn.musicplayerservice.adapter.PlaylistArtistAdapter;
import com.lanltn.musicplayerservice.model.Song;
import com.lanltn.musicplayerservice.utils.AppUtils;
import com.lanltn.musicplayerservice.utils.ScreenUtils;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class FullPlayerView extends RelativeLayout
        implements
        IFullPlayerView,
        PlayerSlideShowAdapter.ISlideShowViewHolderListener,
        PlaylistArtistAdapter.IPlayListViewHolderListener {

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

    private IPlayerComponentView iPlayerComponentView;
    private PlayerSlideShowAdapter mPlayerSlideShowAdapter;
    private PlaylistArtistAdapter mPlaylistArtistAdapter;
    private List<Song> mListSong = new ArrayList<>();

    private int mLastPage;
    private boolean mIsShowSlide = true;

    public void setiPlayerComponentView(IPlayerComponentView iPlayerComponentView) {
        this.iPlayerComponentView = iPlayerComponentView;
        this.mListSong = iPlayerComponentView.getDataInputSong();
        initSlideShow();
        initPlayListArtist();
        Toast.makeText(getContext(), "FULL_VIEW: " + mListSong.size(), Toast.LENGTH_SHORT).show();
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
    }

    private void initPlayListArtist() {
        mPlaylistArtistAdapter = new PlaylistArtistAdapter(getContext(), mListSong);
        mPlaylistArtistAdapter.setmViewHolderListener(this);
        mPlayerRecyclerView.setAdapter(mPlaylistArtistAdapter);
    }


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
                imgSkipPrevious.setEnabled(false);
                imgSkipNext.setEnabled(true);
                if (mLastPage != adapterPosition) {
                    mLastPage = adapterPosition;


                }
            }
        });
    }

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
                Toast.makeText(getContext(), "GONE Slide: " + mIsShowSlide, Toast.LENGTH_SHORT).show();
                mIsShowSlide = !mIsShowSlide;
                if (mIsShowSlide) {
                    mSlideShowPlayer.setVisibility(VISIBLE);
                } else {
                    mSlideShowPlayer.setVisibility(GONE);
                }
            }
        });
    }

    private void showFullPlayer() {
        mViewRoot.setVisibility(VISIBLE);
    }

    @Override
    public void onClickPlayMusic(int artistId) {

    }

    @Override
    public void onClickThumbnail(int position) {

    }

    interface IPlayerFullListener extends IBasePlayer {

    }
}
