package com.lanltn.musicplayerservice.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class AppUtils {

    public static int dipToPx(Context c, float dipValue) {
        if (c == null) {
            return 0;
        }
        DisplayMetrics metrics = c.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static float pxToDip(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
