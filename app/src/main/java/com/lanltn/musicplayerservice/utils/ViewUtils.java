/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.lanltn.musicplayerservice.utils;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

public final class ViewUtils {

    private static final float ENABLE_BUTTON_ALPHA = 0.5f;

    public static void delaySetAlphaEnable(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setAlpha(ENABLE_BUTTON_ALPHA);
            }
        }, 100);
    }

    public static int getTextWidth(TextView textView, String text) {
        Rect bounds = new Rect();
        Paint textPaint = textView.getPaint();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    public static int getTextHeight(TextView textView, String text) {
        Rect bounds = new Rect();
        Paint textPaint = textView.getPaint();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    public static void delayEnableViewAfterTouch(final View view) {
        view.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 1000);
    }
}
