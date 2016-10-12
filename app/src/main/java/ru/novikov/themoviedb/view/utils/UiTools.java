package ru.novikov.themoviedb.view.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by inovikov on 12.10.2016.
 */

public class UiTools {

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getColumnCount(Context context, int columnWidth) {
        int screenWidth = getScreenWidth(context);
        return screenWidth / columnWidth;
    }

}
