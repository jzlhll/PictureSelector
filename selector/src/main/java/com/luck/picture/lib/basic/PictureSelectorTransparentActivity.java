package com.luck.picture.lib.basic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.luck.picture.lib.PictureOnlyCameraFragment;
import com.luck.picture.lib.R;
import com.luck.picture.lib.bases.AbsImmersiveActivity;
import com.luck.picture.lib.config.SelectorConfig;
import com.luck.picture.lib.config.SelectorProviders;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;


public class PictureSelectorTransparentActivity extends AbsImmersiveActivity {
    private SelectorConfig selectorConfig;

    @Override
    public int getEnterAnim() {
        PictureWindowAnimationStyle windowAnimationStyle = selectorConfig.selectorStyle.getWindowAnimationStyle();
        return windowAnimationStyle.activityEnterAnimation;
    }

    @Override
    public int getExitAnim() {
        return R.anim.ps_anim_fade_out;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initSelectorConfig();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_empty);
        setActivitySize();
        setupFragment();
    }

    private void initSelectorConfig() {
        selectorConfig = SelectorProviders.getInstance().getSelectorConfig();
    }

    private void setupFragment() {
        String fragmentTag;
        Fragment targetFragment = null;
        fragmentTag = PictureOnlyCameraFragment.TAG;
        targetFragment = PictureOnlyCameraFragment.newInstance();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentByTag(fragmentTag);
        if (fragment != null) {
            supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
        FragmentInjectManager.injectSystemRoomFragment(supportFragmentManager, fragmentTag, targetFragment);
    }

    @SuppressLint("RtlHardcoded")
    private void setActivitySize() {
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
    }

    @Override
    public void immersive(@NotNull Activity activity, @NotNull View root, int statusBarHeight, int navBarHeight) {

    }
}
