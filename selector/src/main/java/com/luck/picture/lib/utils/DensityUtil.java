package com.luck.picture.lib.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * @author：luck
 * @date：2021/11/17 11:48 上午
 * @describe：DensityUtil
 */
public class DensityUtil {
    /**
     * 获取屏幕真实宽度
     *
     * @param context
     * @return
     */
    public static int getRealScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.x;
    }

    /**
     * 获取屏幕真实高度
     *
     * @param context
     * @return
     */
    public static int getRealScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.y;
    }

    /**
     * 获取屏幕高度(不包含状态栏高度)
     */
    public static int getScreenHeight(Context context, int navBarHeight) {
        return getRealScreenHeight(context) - navBarHeight;
    }

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
