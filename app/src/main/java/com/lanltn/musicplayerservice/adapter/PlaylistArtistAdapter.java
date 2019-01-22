package com.lanltn.musicplayerservice.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lanltn.musicplayerservice.R;
import com.lanltn.musicplayerservice.model.Artist;
import com.lanltn.musicplayerservice.model.Song;

import java.util.List;

import io.gresse.hugo.vumeterlibrary.VuMeterView;

import static com.lanltn.musicplayerservice.utils.ViewUtils.delaySetAlphaEnable;


public class PlaylistArtistAdapter extends RecyclerView.Adapter<PlaylistArtistAdapter.PlayerSlideViewHolder> {

    private List<Song> songList;
    private Context mContext;
    private IPlayListViewHolderListener mViewHolderListener;

    public PlaylistArtistAdapter(Context context, List<Song> songList) {
        this.mContext = context;
        this.songList = songList;
    }

    @Override
    public PlayerSlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_player_artist_song, parent, false);
        return new PlayerSlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerSlideViewHolder holder, int position) {
        holder.bindView(position, songList.get(position));
        if (mViewHolderListener != null) {
            holder.setListener(mViewHolderListener);
        }
    }

    @Override

    public int getItemCount() {
        return songList.size();
    }

    public void setmViewHolderListener(IPlayListViewHolderListener mViewHolderListener) {
        this.mViewHolderListener = mViewHolderListener;
    }

    public void updateAdapter(List<Song> songList) {
        this.songList = songList;
        notifyDataSetChanged();
    }

    public class PlayerSlideViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mClContainer;
        ConstraintLayout mClContent;
        ImageView mImgArtist;
        TextView mTxtTitle;
        TextView mTxtSubTitle;
        VuMeterView mImgEqualizer;

        private int mPosition;
        private Artist mArtist;

        private IPlayListViewHolderListener mListener;

        public PlayerSlideViewHolder(View itemView) {
            super(itemView);
            init(itemView);
            setEvent();
        }

        private void setEvent() {

        }

        private void init(View itemView) {
            mClContainer = itemView.findViewById(R.id.player_artist_constraint_container);
            mClContent = itemView.findViewById(R.id.player_artist_view_content);
            mImgArtist = itemView.findViewById(R.id.player_artist_image_view);
            mTxtTitle = itemView.findViewById(R.id.player_artist_text_view_title);
            mTxtSubTitle = itemView.findViewById(R.id.player_artist_text_view_sub_title);
            mImgEqualizer = itemView.findViewById(R.id.image_equalizer);
        }

        public void bindView(final int position, Song song) {
            mPosition = position;
            if (song != null) {

                Glide.with(mContext)
                        .load(song.getImage())
                        .apply((new RequestOptions()).error(R.drawable.img_artist))
                        .into(mImgArtist);
                mTxtSubTitle.setText(song.getArtistName());
                mTxtTitle.setText(song.getName());
            }
            mClContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClickSongRecyclerView(position);
                    }
                }
            });

            mClContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mClContainer.setAlpha(0.5F);
                            break;
                        case MotionEvent.ACTION_UP:
                            delaySetAlphaEnable(mClContainer);
                            if(mListener!=null){
                                mListener.onClickSongRecyclerView(position);
                            }
                            mClContainer.setAlpha(1f);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            delaySetAlphaEnable(mClContainer);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            mClContainer.setAlpha(0.5F);
                            break;
                    }
                    return true;
                }
            });

            if(song.getStatusPlay()==Song.SONG_CHOOSE_PLAY){
                mImgEqualizer.setVisibility(View.VISIBLE);
                mImgEqualizer.resume(true);
            } else if(song.getStatusPlay()==song.SONG_CHOOSE_PAUSE){
                mImgEqualizer.setVisibility(View.VISIBLE);
                mImgEqualizer.pause();
            } else {
                mImgEqualizer.setVisibility(View.GONE);
                mImgEqualizer.pause();
            }

        }

        public void setListener(IPlayListViewHolderListener mListener) {
            this.mListener = mListener;
        }


    }

    public interface IPlayListViewHolderListener {
        void onClickSongRecyclerView(int position);
    }
}