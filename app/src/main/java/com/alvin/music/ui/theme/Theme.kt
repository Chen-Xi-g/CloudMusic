package com.alvin.music.ui.theme

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.OverscrollConfiguration
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import javax.annotation.concurrent.Immutable

/**
 * @Description ：   自定义主题颜色
 *
 * @property isDark 是否是暗色模式
 * @property homeTopBackground 首页顶部背景色1
 * @property iconTint 图标颜色
 * @property searchBackground 搜索框背景色
 * @property searchText 搜索框文字颜色
 * @property subText 次要文字颜色
 * @property text 文字颜色
 * @property iconDiscoverStart 发现图标渐变色开始
 * @property iconDiscoverEnd 发现图标渐变色结束
 * @property cardBackground 卡片背景色
 * @property cardShadow 卡片阴影颜色
 * @property playerBackground 播放器背景色
 * @property playerTrackBackground 播放器进度条背景色
 * @constructor Create empty Custom color palette
 */
@Immutable
data class CustomColorPalette(
    val isDark: Boolean = false,
    val homeTopBackground: Color = Color.Unspecified,
    val iconTint: Color = Color.Unspecified,
    val searchBackground: Color = Color.Unspecified,
    val searchText: Color = Color.Unspecified,
    val subText: Color = Color.Unspecified,
    val text: Color = Color.Unspecified,
    val iconDiscoverStart : Color = Color.Unspecified,
    val iconDiscoverEnd : Color = Color.Unspecified,
    val cardBackground: Color = Color.Unspecified,
    val cardShadow: Color = Color.Unspecified,
    val playerBackground: Color = Color.Unspecified,
    val playerTrackBackground: Color = Color.Unspecified,
)

val LocalCustomColor = staticCompositionLocalOf { CustomColorPalette() }

private val DarkColorScheme = darkColorScheme(
    primary = COLOR_EB3B1C,
    secondary = COLOR_EB3B1C,
    tertiary = COLOR_EB3B1C,
    background = COLOR_1B1B25,
    onPrimary = COLOR_F8F9FF,
    onSecondary = COLOR_50_FFF,
    onTertiary = COLOR_20_WHITE,
    surface = COLOR_1B1B25,
    onSurface = COLOR_1B1B25
)

private val LightColorScheme = lightColorScheme(
    primary = COLOR_EB3B1C,
    secondary = COLOR_EB3B1C,
    tertiary = COLOR_EB3B1C,
    background = COLOR_F8F9FF,
    onPrimary = COLOR_091227,
    onSecondary = COLOR_50_333,
    onTertiary = COLOR_20_BLACK,
    surface = COLOR_F8F9FF,
    onSurface = COLOR_F8F9FF,
)

private val CustomDarkColorScheme = CustomColorPalette(
    isDark = true,
    homeTopBackground = COLOR_2E1A26,
    iconTint = Color.White,
    searchBackground = COLOR_10_FFF,
    searchText = COLOR_50_FFF,
    subText = COLOR_70_FFF,
    text = COLOR_90_FFF,
    iconDiscoverStart = COLOR_FF5722,
    iconDiscoverEnd = COLOR_E91E63,
    cardBackground = COLOR_272530,
    cardShadow = COLOR_393B46,
    playerBackground = COLOR_97_23212B,
    playerTrackBackground = COLOR_50_FFF
)

private val CustomLightColorScheme = CustomColorPalette(
    isDark = false,
    homeTopBackground = COLOR_F9ECF0,
    iconTint = Color.Black,
    searchBackground = COLOR_10_000,
    searchText = COLOR_50_333,
    subText = COLOR_70_333,
    text = COLOR_515151,
    iconDiscoverStart = COLOR_FF5722,
    iconDiscoverEnd = COLOR_E91E63,
    cardBackground = Color.White,
    cardShadow = COLOR_EBECF1,
    playerBackground = Color.White,
    playerTrackBackground = COLOR_50_333
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val customColorScheme = when {
        darkTheme -> CustomDarkColorScheme
        else -> CustomLightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val orientation = LocalConfiguration.current.orientation
    val screenWidth = displayMetrics.widthPixels
    val density = if (orientation == Configuration.ORIENTATION_PORTRAIT){
        //竖屏以设计的宽为基准
        screenWidth / 375F
    }else{
        //横屏以设计的高为基准
        screenWidth / 812F
    }
    val fontScale = LocalDensity.current.fontScale
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = {
            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = density,
                    fontScale = .8F
                ),
                LocalCustomColor provides customColorScheme,
                LocalOverscrollConfiguration provides null,
                LocalRippleTheme provides NoRippleTheme,
                LocalIndication provides NoIndication,
            ) {
                content()
            }
        }
    )
}

private object NoRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor(): Color {
        return Color.Unspecified
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        return RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
    }
}

private object NoIndication : Indication {

    private object NoIndicationInstance : IndicationInstance {
        override fun ContentDrawScope.drawIndication() {
            drawContent()
        }
    }

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        return NoIndicationInstance
    }

}