package com.alvin.music

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.sp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.alvin.music.ui.route.HomeRoute
import com.alvin.music.ui.theme.MusicTheme
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null

    private val player by lazy {
        ExoPlayer.Builder(this).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        setContent {
            MusicTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .autoCloseKeyboard(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()
                    HomeRoute(navController!!)
                }
            }
        }
        // 监听动画退出
        splashScreen.setOnExitAnimationListener { provider ->
            val iconView = provider.iconView
            AnimatorSet().apply {
                playSequentially(
                    ObjectAnimator.ofFloat(iconView, View.TRANSLATION_Y, 0f, 50f),
                    ObjectAnimator.ofFloat(
                        iconView,
                        View.TRANSLATION_Y,
                        50f,
                        -provider.view.height.toFloat()
                    ),
                )
                doOnEnd {
                    provider.remove()
                }
                start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.autoCloseKeyboard(): Modifier = composed {
    val keyboardController = LocalSoftwareKeyboardController.current
    pointerInput(this@autoCloseKeyboard) {
        detectTapGestures(
            onPress = {
                keyboardController?.hide()
            }
        )
    }
}