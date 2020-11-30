package org.slovenlypolygon.recipes.utils;

import android.content.Context;
import android.util.TypedValue;

public class Converters {
    public static int fromDP(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
