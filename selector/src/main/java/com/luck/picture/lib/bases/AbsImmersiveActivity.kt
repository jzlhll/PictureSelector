package com.luck.picture.lib.bases

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.luck.picture.lib.bases.ScreenConst.currentStatusBarAndNavBarHeight

abstract class AbsImmersiveActivity : AppCompatActivity() {
    abstract val enterAnim:Int
    abstract val exitAnim:Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeFix()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            if(enterAnim != 0) overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, enterAnim, 0)
            if(exitAnim != 0) overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, exitAnim)
        }
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        //处理安卓8.0报错
        //Only fullscreen activities can request orientation
        ignoreError { super.setRequestedOrientation(requestedOrientation) }
    }

    override fun finish() {
        super.finish()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            if(exitAnim != 0) overridePendingTransition(0, exitAnim)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        onWindowFocusChangedInner(hasFocus)
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, newConfig: Configuration) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig)
        onWindowFocusChangedInner(true)
    }

    /**
     * 如果你想自己控制immersive请使用它
     */
    @CallSuper
    private fun onWindowFocusChangedInner(hasFocus: Boolean) {
        if (hasFocus) {
            val pair = currentStatusBarAndNavBarHeight(this)
            immersive(this, findViewById(android.R.id.content),
                pair?.first ?:0, pair?.second ?:0)
        }
    }

    abstract fun immersive(activity: Activity, root: View,  statusBarHeight: Int, navBarHeight: Int)
}