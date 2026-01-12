package com.luck.picture.lib.bases

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.AnimRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.luck.picture.lib.R

fun ComponentActivity.enableEdgeToEdgeFix(
    statusBarStyle: SystemBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
    navigationBarStyle: SystemBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
) {
    enableEdgeToEdge(statusBarStyle, navigationBarStyle)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isStatusBarContrastEnforced = false
        //与edgeToEdge不同，它的源码是判断 nightMode == UiModeManager.MODE_NIGHT_AUTO
        window.isNavigationBarContrastEnforced = false
    }
}

fun View.plusNavPadding() {
    val bottomPadding = this.paddingBottom;
    updatePadding(bottom = bottomPadding + ScreenConst.instance.navigationBarHeight);
}

fun View.setNavPadding(navHeight:Int) {
    updatePadding(bottom = navHeight);
}

fun View.setTopPadding(statusHeight:Int) {
    updatePadding(top = statusHeight);
}

fun View.setTopAndBottomPadding(statusHeight:Int, navHeight:Int) {
    updatePadding(top = statusHeight, bottom = navHeight);
}

//
///**
// * 修改状态栏文字颜色
// * isAppearanceLightXXX true就表示文字就是黑色的。false就表示文字就是白色的。所以要传入正确的值。
// */
//fun Activity.changeBarsColor(statusBarTextDark: Boolean? = null,
//                             navBarTextDark: Boolean? = null,
//                             statusColor:Int?= null,
//                             navColor:Int?=null) {
//    val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
//    val light = mode == Configuration.UI_MODE_NIGHT_YES
//    window.changeBarsTextColor(statusBarTextDark ?: light, navBarTextDark ?: light, statusColor, navColor)
//}
//
//fun Window.changeBarsTextColor(statusBarTextDark: Boolean,
//                               navBarTextDark: Boolean,
//                               statusColor:Int?= null,
//                               navColor:Int?=null) {
//    val controller = WindowInsetsControllerCompat(this, this.decorView)
//
//    controller.isAppearanceLightStatusBars = statusBarTextDark
//    controller.isAppearanceLightNavigationBars = navBarTextDark
//
//    statusBarColor = statusColor ?: Color.TRANSPARENT  //android15一直是透明。所以你需要自己做padding，然后绘制。
//    navigationBarColor = navColor ?: Color.TRANSPARENT //android15一直是透明。所以你需要自己做padding，然后绘制。
//}

internal inline fun <T:Any> ignoreError(
    block: () -> T?
): T? {
    return try {
        block.invoke()
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

const val KEY_EXIT_ANIM = "activity_key_exit_anim"
const val KEY_ENTER_ANIM = "activity_key_enter_anim"

fun Context.startActivityFix(intent: Intent, opts:Bundle? = null,
                             @AnimRes enterAnim:Int? = null,
                             @AnimRes exitAnim:Int? = null) {
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    if (enterAnim != null) intent.putExtra(KEY_ENTER_ANIM, enterAnim)
    if (exitAnim != null) intent.putExtra(KEY_EXIT_ANIM, exitAnim)

    try {
        startActivity(intent, opts)
    } catch (_:Exception) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Android 10 或更高版本
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } else {
            // Android 10 以下版本
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent, opts)
    }

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU && enterAnim != null && this is Activity) {
        this.overridePendingTransition(enterAnim, R.anim.ps_anim_stay)
    }
}

fun Fragment.startActivityFix(intent: Intent, opts:Bundle? = null, @AnimRes enterAnim:Int? = null, @AnimRes exitAnim:Int? = null) {
    requireActivity().startActivityFix(intent, opts, enterAnim, exitAnim)
}

fun Context.startOutActivity(intent: Intent, opts:Bundle? = null, @AnimRes enterAnim:Int? = null, @AnimRes exitAnim:Int? = null) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        // Android 10 或更高版本
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    } else {
        // Android 10 以下版本
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }

    if (enterAnim != null) intent.putExtra(KEY_ENTER_ANIM, enterAnim)
    if (exitAnim != null) intent.putExtra(KEY_EXIT_ANIM, exitAnim)

    try {
        startActivity(intent, opts)
    } catch (e:Exception) {
        e.printStackTrace()
    }

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU && enterAnim != null && this is Activity) {
        this.overridePendingTransition(enterAnim, R.anim.ps_anim_stay)
    }
}