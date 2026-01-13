package com.yalantis.ucrop.bases

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding

/**
 * 修改状态栏文字颜色
 * isAppearanceLightXXX true就表示文字就是黑色的。false就表示文字就是白色的。所以要传入正确的值。
 */
fun Activity.changeBarsColor(statusBarTextDark: Boolean? = null,
                             navBarTextDark: Boolean? = null,
                             statusColor:Int?= null,
                             navColor:Int?=null) {
    val light = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    window.changeBarsTextColor(statusBarTextDark ?: light, navBarTextDark ?: light, statusColor, navColor)
}

fun Window.changeBarsTextColor(statusBarTextDark: Boolean,
                               navBarTextDark: Boolean,
                               statusColor:Int?= null,
                               navColor:Int?=null) {
    val controller = WindowInsetsControllerCompat(this, this.decorView)

    controller.isAppearanceLightStatusBars = statusBarTextDark
    controller.isAppearanceLightNavigationBars = navBarTextDark

    statusBarColor = statusColor ?: Color.TRANSPARENT  //android15一直是透明。所以你需要自己做padding，然后绘制。
    navigationBarColor = navColor ?: Color.TRANSPARENT //android15一直是透明。所以你需要自己做padding，然后绘制。
}

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

fun View.setNavPadding(navHeight:Int) {
    updatePadding(bottom = navHeight)
}

fun View.setTopPadding(statusHeight:Int) {
    updatePadding(top = statusHeight)
}

fun View.setTopAndBottomPadding(statusHeight:Int, navHeight:Int) {
    updatePadding(top = statusHeight, bottom = navHeight);
}

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