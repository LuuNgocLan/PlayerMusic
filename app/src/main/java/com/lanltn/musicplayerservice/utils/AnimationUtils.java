package com.lanltn.musicplayerservice.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

public class AnimationUtils {
    public static final int DURATION_PLAY_BUTTON = 600;

    // Ripple animation for play button doesn't work in ToolbarLayout, using this kind of animation instead of
    public static void animationRipple(final View view, final int delay) {
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setStartDelay(delay);
            anim.setDuration(1200);
            view.setVisibility(View.VISIBLE);

            anim.start();

            view.animate().alpha(0f).setDuration(300).setStartDelay(200 + delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setAlpha(1f);
                    view.setVisibility(View.GONE);
                }
            });
        }
    }

    // Ripple animation for play button doesn't work in ToolbarLayout, using this kind of animation instead of
    public static void animationRipple(final View view, final int delay, int duration) {
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setStartDelay(delay);
            anim.setDuration(duration);
            view.setVisibility(View.VISIBLE);

            anim.start();

            view.animate().alpha(0f).setDuration(duration / 3).setStartDelay(duration / 6 + delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setAlpha(1f);
                    view.setVisibility(View.GONE);
                }
            });
        }
    }
}
