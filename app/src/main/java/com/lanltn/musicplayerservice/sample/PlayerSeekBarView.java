package com.lanltn.musicplayerservice.sample;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.lanltn.musicplayerservice.R;

public class PlayerSeekBarView extends LinearLayout {
    View mViewRoot;
    SeekBar seekBar;
    CoordinatorLayout clContainer;

    private IPlayerSeekBarView mIPlayerSeekBarView;

    private boolean isSeekingPlayer = false;

    public PlayerSeekBarView(Context context) {
        super(context);
        init(context);
    }

    public PlayerSeekBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mViewRoot = inflate(context,R.layout.partial_custom_seekbar_view,this);
        seekBar = mViewRoot.findViewById(R.id.seek_bar);
        clContainer = mViewRoot.findViewById(R.id.constraint_layout_container);
       // setEvent();
    }

    private void setEvent() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekingPlayer = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekingPlayer = false;
                if (mIPlayerSeekBarView != null) {
                    int position = seekBar.getProgress();
                    mIPlayerSeekBarView.onCallServiceSeekToPosition(position);
                }
            }
        });
    }

    public void resetSeekBar(int duration) {
        seekBar.setProgress(0);
        seekBar.setMax(duration);

    }

    public void updatePrimaryProgressBar(int progress) {
        if (!isSeekingPlayer) {
            seekBar.setProgress(progress);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                seekBar.setProgress(progress, true);
//            } else {
//                seekBar.setProgress(progress);
            //  ObjectAnimator.ofInt(seekBar, "progress", progress).setDuration(100).start();
            //   }
        }
    }


    public void updateSecondaryProgressBar(int progress) {
        seekBar.setSecondaryProgress(progress);
    }


    public void setIPlayerSeekBarView(IPlayerSeekBarView mIPlayerSeekBarView) {
        this.mIPlayerSeekBarView = mIPlayerSeekBarView;
    }

    public interface IPlayerSeekBarView {
        void onCallServiceSeekToPosition(int positionPercent);
    }

}
