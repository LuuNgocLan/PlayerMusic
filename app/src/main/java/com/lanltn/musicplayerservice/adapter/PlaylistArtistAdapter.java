package com.lanltn.musicplayerservice.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lanltn.musicplayerservice.R;
import com.lanltn.musicplayerservice.model.Artist;
import com.lanltn.musicplayerservice.model.Song;

import java.util.List;

import io.gresse.hugo.vumeterlibrary.VuMeterView;


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

        public void bindView(int position, Song song) {
            mPosition = position;
            if (song != null) {
                mArtist = song.getmArtist();
                if (mArtist != null) {

                    Glide.with(mContext)
                            .load(mArtist.getmImageUrl())
                            .into(mImgArtist);
                    mTxtSubTitle.setText(mArtist.getmNameArtist());
                }
                mTxtTitle.setText(song.getmTitle());
            }
        }

        public void setListener(IPlayListViewHolderListener mListener) {
            this.mListener = mListener;
        }


    }

    public interface IPlayListViewHolderListener {
        void onClickPlayMusic(int artistId);

        void onClickThumbnail(int position);
    }
}