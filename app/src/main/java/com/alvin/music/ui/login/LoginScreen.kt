package com.alvin.music.ui.login

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.transition.Transition
import com.alvin.music.R
import com.alvin.music.ui.home.Loading
import com.alvin.music.ui.theme.LocalCustomColor
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

/**
 * @Description ：   登录页面
 * @Date        ：   2023/6/26
 * @author      ：   高国峰
 *
 * @param loginListener 登录成功回调
 * @param viewModel 登录ViewModel
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    loginListener: () -> Unit,
) {
    val state = viewModel.state
    val lifecycleOwner = LocalLifecycleOwner.current
    var topIconPadding by remember { mutableStateOf(0.dp) }
    val topIconPaddingAnim by animateDpAsState(
        targetValue = topIconPadding, animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )
    var sizeWidth by remember { mutableStateOf(0.dp) }
    val sizeWidthAnim by animateDpAsState(
        targetValue = sizeWidth, animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )
    var sizeHeight by remember { mutableStateOf(0.dp) }
    val sizeHeightAnim by animateDpAsState(
        targetValue = sizeHeight, animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )
    /*根据生命周期获取二维码数据*/
    LaunchedEffect(key1 = lifecycleOwner, block = {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.loadQrData()
        }
    })
    /*监听二维码扫描状态*/
    LaunchedEffect(key1 = state.qrStatusEntity?.status, block = {
        when (state.qrStatusEntity?.status) {
            800 -> {
                // 二维码过期，重新获取二维码
                viewModel.loadQrData()
            }

            803 -> {
                // 二维码授权成功，用户一登录
                loginListener()
            }
        }
    })
    /*监听是否获取到了二维码。然后执行动画*/
    LaunchedEffect(key1 = state.qrEntity, block = {
        if (state.qrEntity != null) {
            topIconPadding = 50.dp
            sizeWidth = 300.dp
            sizeHeight = 400.dp
        }
    })
    if (state.isLoading) {
        Loading()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (topIconPaddingAnim > 0.dp) {
            Icon(
                painter = painterResource(id = R.drawable.ic_splash_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = topIconPaddingAnim)
                    .size(50.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .align(
                        Alignment.TopCenter
                    ),
                tint = Color.Unspecified
            )
        }
        /*主要内容卡片*/
        Card(modifier = Modifier.size(sizeWidthAnim, sizeHeightAnim)) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = topIconPaddingAnim)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val qrUrl = state.qrEntity?.qrUrl
                if (!qrUrl.isNullOrEmpty()) {
                    /*加载二维码*/
                    val qrPainter = rememberQrBitmapPainter(qrUrl)
                    Image(
                        painter = qrPainter,
                        contentDescription = "二维码",
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.weight(1F))
                    /*扫码成功，等待授权时显示头像、昵称*/
                    if (!state.qrStatusEntity?.nickname.isNullOrEmpty() && !state.qrStatusEntity?.avatarUrl.isNullOrEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            /*头像*/
                            CoilImage(
                                imageModel = { state.qrStatusEntity?.avatarUrl },
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(
                                        RoundedCornerShape(15.dp)
                                    )
                            )
                            /*昵称*/
                            Text(
                                text = state.qrStatusEntity?.nickname ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    /*扫码提示信息*/
                    Text(
                        text = state.qrStatusEntity?.message ?: "未知异常，请重新进入页面。",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

            }

        }
        Icon(
            painter = painterResource(id = R.drawable.ic_splash_tips),
            contentDescription = "欢迎语",
            tint = Color.White,
            modifier = Modifier
                .padding(bottom = 50.dp, start = 80.dp, end = 80.dp)
                .align(Alignment.BottomCenter)
        )
        Text(text = "跳过",
            style = MaterialTheme.typography.bodyMedium,
            color = LocalCustomColor.current.text,
            modifier = Modifier
                .statusBarsPadding()
                .clickable {
                    loginListener()
                }
                .padding(10.dp)
                .align(Alignment.TopEnd)
        )
    }
}