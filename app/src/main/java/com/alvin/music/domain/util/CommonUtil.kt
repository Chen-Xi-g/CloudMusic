package com.alvin.music.domain.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Image
import com.alvin.music.BaseApp

private var toast: Toast? = null

/**
 * 显示Toast
 */
fun String?.toast() {
    if (this.isNullOrEmpty()) return
    toast?.cancel()
    toast = null
    toast = Toast.makeText(
        BaseApp.CONTEXT, null, if (length > 12) {
            Toast.LENGTH_LONG
        } else {
            Toast.LENGTH_SHORT
        }
    )
    toast?.setText(this)
    toast?.show()
}

/**
 * 格式化时间
 *
 * @return 格式化后的时间
 */
fun Long.formatTime(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val remainingSeconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

/**
 * 按照指定宽高加载图片
 *
 * @param width 宽
 * @param height 高
 */
fun String?.loadImage(width: Int, height: Int): String {
    if (this.isNullOrEmpty()) return ""
    return "$this?param=${width * 2}y${height * 2}"
}
