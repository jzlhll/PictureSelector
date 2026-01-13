package com.yalantis.ucrop.bases;

import android.app.Activity;
import android.util.Pair;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jetbrains.annotations.Nullable;

public class ScreenConst {
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
