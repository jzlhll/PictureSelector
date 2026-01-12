package com.luck.picture.lib.bases;

import android.app.Activity;
import android.util.Pair;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.luck.picture.lib.utils.DensityUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScreenConst {
    private ScreenConst() {}

    public static final ScreenConst instance = new ScreenConst();

    public int statusBarHeight = 0;
    public int navigationBarHeight = 0;

    public int screenWidth = 0;
    public int screenHeight = 0;
    public int screenAppInHeight = 0;
    public boolean isSure = false;

    /**
     * 在启动本程序之前调用。不得在onCreate中调用。必须在点击事件的时候调用。
     */
    public void sure(@NotNull Activity activity) {
        if (!isSure) {
            isSure = true;
            screenWidth = DensityUtil.getRealScreenWidth(activity);
            screenAppInHeight = DensityUtil.getRealScreenHeight(activity);
            var pair = currentStatusBarAndNavBarHeight(activity);

            if (pair != null) {
                statusBarHeight = pair.first;
                navigationBarHeight = pair.second;
                screenHeight = DensityUtil.getScreenHeight(activity, pair.second);
            } else {
                screenHeight = screenAppInHeight;
            }
        }
    }

    /**
     * 必须在activity已经完全渲染之后，可以通过
     * 第一种办法：
     * ViewCompat.setOnApplyWindowInsetsListener(decorView) { _, insets ->
     *         val navHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
     *         val statusHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
     * 第二种办法：
     * View.post中调用本函数。
     *
     * 第三种办法：
     * 在onWindowFocusChanged中调用本函数。
     */
    @Nullable
    public static Pair<Integer, Integer> currentStatusBarAndNavBarHeight(Activity activity) {
        var insets = ViewCompat.getRootWindowInsets(activity.getWindow().getDecorView());
        if (insets == null) return null;
        int nav = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
        int sta = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
        return new Pair<>(sta, nav);
    }
}
