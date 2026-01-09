package com.luck.picture.lib.config;

import android.util.Log;

import com.luck.picture.lib.PictureSelectorFragment;

import java.util.LinkedList;

import kotlin.jvm.Volatile;

/**
 * @author：luck
 * @date：2023/3/31 4:15 下午
 * @describe：SelectorProviders
 */
public class SelectorProviders {

    @Volatile
    private SelectorConfig selectorConfig;

    public SelectorConfig getSelectorConfigReset() {
        destroy();
        selectorConfig = new SelectorConfig();
        return selectorConfig;
    }

    public SelectorConfig getSelectorConfig() {
        var config = selectorConfig;
        if (config == null) {
            config = new SelectorConfig();
            selectorConfig = config;
        }
        return config;
    }

    public void destroy() {
        var config = selectorConfig;
        if (config != null) {
            config.destroy();
        }
        selectorConfig = null;
    }

    private static volatile SelectorProviders selectorProviders;

    public static SelectorProviders getInstance() {
        if (selectorProviders == null) {
            synchronized (SelectorProviders.class) {
                if (selectorProviders == null) {
                    selectorProviders = new SelectorProviders();
                }
            }
        }
        return selectorProviders;
    }
}
