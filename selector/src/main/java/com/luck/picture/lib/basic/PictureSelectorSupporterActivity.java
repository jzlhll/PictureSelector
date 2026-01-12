package com.luck.picture.lib.basic;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.luck.picture.lib.PictureSelectorFragment;
import com.luck.picture.lib.R;
import com.luck.picture.lib.bases.AbsImmersiveActivity;
import com.luck.picture.lib.bases.UtilKt;
import com.luck.picture.lib.config.SelectorConfig;
import com.luck.picture.lib.config.SelectorProviders;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.language.PictureLanguageUtils;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;


public class PictureSelectorSupporterActivity extends AbsImmersiveActivity {
    private SelectorConfig selectorConfig;

    @Override
    public int getEnterAnim() {
        PictureWindowAnimationStyle windowAnimationStyle = selectorConfig.selectorStyle.getWindowAnimationStyle();
        return windowAnimationStyle.activityEnterAnimation;
    }

    @Override
    public int getExitAnim() {
        PictureWindowAnimationStyle windowAnimationStyle = selectorConfig.selectorStyle.getWindowAnimationStyle();
        return windowAnimationStyle.activityExitAnimation;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initSelectorConfig();
        super.onCreate(savedInstanceState);
        UtilKt.changeBarsColor(this, false, null, null, null);
        setContentView(R.layout.ps_activity_container);
        setupFragment();
    }

    private void initSelectorConfig() {
        selectorConfig = SelectorProviders.getInstance().getSelectorConfig();
    }

    private void setupFragment() {
        FragmentInjectManager.injectFragment(this, PictureSelectorFragment.TAG,
                PictureSelectorFragment.newInstance());
    }

    /**
     * set app language
     */
    public void initAppLanguage() {
        if (selectorConfig != null && selectorConfig.language != LanguageConfig.UNKNOWN_LANGUAGE && !selectorConfig.isOnlyCamera) {
            PictureLanguageUtils.setAppLanguage(this, selectorConfig.language, selectorConfig.defaultLanguage);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initAppLanguage();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SelectorConfig selectorConfig = SelectorProviders.getInstance().getSelectorConfig();
        if (selectorConfig != null) {
            super.attachBaseContext(PictureContextWrapper.wrap(newBase, selectorConfig.language, selectorConfig.defaultLanguage));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    @Override
    public void immersive(@NotNull Activity activity, @NotNull View root, int statusBarHeight, int navBarHeight) {
    }
}
