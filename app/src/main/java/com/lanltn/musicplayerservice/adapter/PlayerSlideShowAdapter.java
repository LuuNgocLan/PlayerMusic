package com.lanltn.musicplayerservice.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lanltn.musicplayerservice.R;
import com.lanltn.musicplayerservice.model.Artist;
import com.lanltn.musicplayerservice.model.Song;

import java.util.List;


public class PlayerSlideShowAdapter extends RecyclerView.Adapter<PlayerSlideShowAdapter.PlayerSlideViewHolder> {

    private List<Song> songList;
    private Context mContext;
    private ISlideShowViewHolderListener mViewHolderListener;

    public PlayerSlideShowAdapter(Context context, List<Song> songList) {
        this.mContext = context;
        this.songList = songList;
    }

    @Override
    public PlayerSlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_slide_show, parent, false);
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

    public void setmViewHolderListener(ISlideShowViewHolderListener mViewHolderListener) {
        this.mViewHolderListener = mViewHolderListener;
    }

    public void updateAdapter(List<Song> songList) {
        this.songList = songList;
        notifyDataSetChanged();
    }

    public class PlayerSlideViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBackground;

        TextView txtTitle;

        ImageView imgPlay;
        View mViewConstraintPlay;
        View mViewFakeAnimation;

        RelativeLayout rlSubViewContainer;
        CardView mSlideshowContainer;

        private int mPosition;
        private Artist mArtist;

        private ISlideShowViewHolderListener mListener;

        public PlayerSlideViewHolder(View itemView) {
            super(itemView);
            init(itemView);
            setEvent();
        }

        private void setEvent() {
            imgPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Action play
                }
            });

            mSlideshowContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        private void init(View itemView) {
            imgBackground = itemView.findViewById(R.id.slide_show_image_view_background);
            txtTitle = itemView.findViewById(R.id.slide_show_text_view_title);
            imgPlay = itemView.findViewById(R.id.slide_show_image_view_play);
            mViewConstraintPlay = itemView.findViewById(R.id.constraint_slide_show_image_view_play);
            mViewFakeAnimation = itemView.findViewById(R.id.view_mm20_fake_animation);
            rlSubViewContainer = itemView.findViewById(R.id.slide_show_relative_container);
            mSlideshowContainer = itemView.findViewById(R.id.slideshow_card_container);
        }

        public void bindView(int position, Song song) {
            mPosition = position;

            Glide.with(mContext)
                    .load(song.getImage())
                    .apply(new RequestOptions().error(R.drawable.img_artist))
                    .into(imgBackground);
            txtTitle.setText(song.getName());
        }

        public void setListener(ISlideShowViewHolderListener mListener) {
            this.mListener = mListener;
        }


    }

    public interface ISlideShowViewHolderListener {
        void onClickPlayMusic(int artistId);

        void onClickThumbnail(int position);
    }
}