package com.alvin.music.ui.player

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.Player
import coil.ImageLoader
import com.alvin.music.R
import com.alvin.music.domain.home.HomeSongEntity
import com.alvin.music.domain.util.formatTime
import com.alvin.music.domain.util.loadImage
import com.alvin.music.domain.util.toast
import com.alvin.music.ui.theme.COLOR_2E1A26
import com.alvin.music.ui.theme.COLOR_30_000
import com.alvin.music.ui.theme.COLOR_50_FFF
import com.alvin.music.ui.theme.COLOR_80_000
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.transformation.blur.BlurTransformationPlugin
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


/**
 * 播放详情页
 *
 * @param playState 播放状态
 * @param progressState 播放进度
 * @param seekTo 拖动进度条
 * @param previous 上一首
 * @param next 下一首
 * @param playOrPause 播放或暂停
 * @param switchSong 切换歌曲
 * @param backListener 返回
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayDetailScreen(
    playState: PlayState,
    progressState: StateFlow<Long>,
    seekTo: (Long) -> Unit,
    previous: () -> Unit,
    next: () -> Unit,
    playOrPause: () -> Unit,
    switchSong: (HomeSongEntity) -> Unit,
    changeMode: (mode: Int) -> Unit,
    removeSong: (HomeSongEntity) -> Unit,
    backListener: () -> Unit
) {
    var isListDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var isSwitch by remember { mutableStateOf(false) }
    val pageState = rememberPagerState()
    var bgUrl by remember { mutableStateOf(playState.list[pageState.currentPage].picUrl) }
    // 判断切换歌曲
    LaunchedEffect(key1 = pageState.currentPage, key2 = pageState.isScrollInProgress, block = {
        if (isSwitch && !pageState.isScrollInProgress) {
            switchSong(playState.list[pageState.currentPage])
        }
        isSwitch = true
    })
    // 更新当前Page
    LaunchedEffect(key1 = playState.currentSong) {
        val page = playState.list.indexOf(playState.currentSong)
        bgUrl = playState.currentSong?.picUrl ?: ""
        if (page != pageState.currentPage) {
            pageState.scrollToPage(page)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(COLOR_2E1A26)
    ) {
        // 背景图片
        CoilImage(imageModel = {
            bgUrl.loadImage(200, 200)
        }, modifier = Modifier.fillMaxSize(), imageLoader = {
            ImageLoader.Builder(LocalContext.current)
                .crossfade(true)
                .build()
        }, component = rememberImageComponent {
            +BlurTransformationPlugin(200)
        })
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(COLOR_30_000, Color.Transparent)
                    )
                )
                .align(Alignment.TopCenter)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, COLOR_30_000)
                    )
                )
                .align(Alignment.BottomCenter)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            PlayTopInfo(playState, backListener)
             HorizontalPager(
                pageCount = playState.list.size, state = pageState, modifier = Modifier
                    .fillMaxSize()
                    .weight(1F)
            ) { page ->
                PlayCenterCover(playState.list[page].picUrl, playState.isPlaying)
            }
            PlayBottomTrack(playState, progressState, seekTo)
            PlayBottomButton(playState, changeMode, {
                if (pageState.currentPage - 1 < 0) {
                    "已经是第一首了".toast()
                    return@PlayBottomButton
                }
                isSwitch = false
                scope.launch {
                    pageState.animateScrollToPage(pageState.currentPage - 1)
                }
                previous()
            }, {
                if (pageState.currentPage + 1 >= playState.list.size) {
                    "已经是最后一首了".toast()
                    return@PlayBottomButton
                }
                isSwitch = false
                scope.launch {
                    pageState.animateScrollToPage(pageState.currentPage + 1)
                }
                next()
            }, playOrPause, playingList = {
                isListDialog = true
            })
        }
    }
    if (isListDialog) {
        PlayListDialog(playState.list, playState.currentSong, onClickItem = {
            switchSong(it)
        }, onRemoveSong = {
            removeSong(it)
        }){
            isListDialog = false
        }
    }
}

/**
 * 顶部播放信息
 *
 * @param backListener 返回监听
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayTopInfo(playState: PlayState, backListener: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "关闭",
            modifier = Modifier
                .clickable(onClick = backListener)
                .padding(20.dp)
                .size(20.dp)
                .rotate(90F),
            tint = Color.White
        )
        Column(modifier = Modifier.weight(1F)) {
            // 歌曲名
            Text(
                text = playState.currentSong?.name ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                    .drawWithContent {
                        drawContent()
                        drawFadedEdge(leftEdge = true)
                        drawFadedEdge(leftEdge = false)
                    }
                    .basicMarquee(
                        // Animate forever.
                        iterations = Int.MAX_VALUE,
                        spacing = MarqueeSpacing(0.dp)
                    ),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(2.dp))
            // 歌手名
            Text(
                text = playState.currentSong?.artist ?: "",
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = COLOR_50_FFF,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center,
            )
        }
        Icon(painter = painterResource(id = R.drawable.ic_share),
            contentDescription = "分享",
            modifier = Modifier
                .clickable { }
                .padding(20.dp)
                .size(20.dp),
            tint = Color.White)
    }
}

/**
 * 中部封面信息
 *
 * @param cover 封面
 * @param isPlaying 是否正在播放
 */
@Composable
fun PlayCenterCover(cover: String, isPlaying: Boolean = false) {
    // 旋转动画，一直旋转
    val infiniteTransition = rememberInfiniteTransition()
    val rotate by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 30000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )
    var lastRotation by remember { mutableStateOf(0F) }
    LaunchedEffect(key1 = isPlaying) {
        if (!isPlaying) {
            lastRotation = rotate
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CoilImage(
            imageModel = {
                cover.loadImage(200, 200)
            }, modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .rotate(if (isPlaying) rotate else lastRotation)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_play_disc),
            contentDescription = null,
            modifier = Modifier.size(300.dp)
        )
    }
}

/**
 * 底部播放滑块
 *
 * @param progress 播放进度
 */
@Composable
fun PlayBottomTrack(playState: PlayState, progress: StateFlow<Long>, seekTo: (Long) -> Unit = {}) {
    val progressStateValue by progress.collectAsState(0L)
    var changeProcess by remember { mutableStateOf(0L) }
    var isTouch by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isTouch) changeProcess.formatTime() else progressStateValue.formatTime(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center
        )
        Slider(
            value = if (isTouch) changeProcess.toFloat() else progressStateValue.toFloat(),
            onValueChange = {
                isTouch = true
                changeProcess = it.toLong()
            },
            onValueChangeFinished = {
                seekTo(changeProcess)
                isTouch = false
            },
            valueRange = 0f..(playState.currentSong?.time ?: 1L).toFloat(),
            modifier = Modifier
                .weight(1F)
                .padding(horizontal = 5.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = COLOR_50_FFF,
                activeTickColor = Color.White,
                inactiveTickColor = COLOR_50_FFF
            )
        )
        Text(
            text = playState.currentSong?.time?.formatTime() ?: "00:00",
            style = MaterialTheme.typography.labelSmall,
            color = COLOR_50_FFF,
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 底部播放控制按钮
 */
@Composable
fun PlayBottomButton(
    playState: PlayState,
    changeMode: (mode: Int) -> Unit = {},
    previous: () -> Unit = {},
    next: () -> Unit = {},
    playOrPause: () -> Unit = {},
    playingList: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(painter = painterResource(
            id = when (playState.repeatMode) {
                Player.REPEAT_MODE_ONE -> R.drawable.ic_single_cycle
                Player.REPEAT_MODE_ALL -> R.drawable.ic_list_cycle
                else -> R.drawable.ic_random_play
            }
        ),
            contentDescription = "播放模式",
            modifier = Modifier
                .clickable {
                    changeMode(
                        when(playState.repeatMode){
                            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
                            Player.REPEAT_MODE_ALL -> 3
                            else -> Player.REPEAT_MODE_ONE
                        }
                    )
                }
                .padding(20.dp)
                .size(24.dp),
            tint = Color.White)
        Icon(painter = painterResource(id = R.drawable.ic_play_next),
            contentDescription = "上一首",
            modifier = Modifier
                .clickable { previous() }
                .padding(17.dp)
                .size(30.dp)
                .rotate(180F),
            tint = Color.White)
        Icon(painter = painterResource(id = if (playState.isPlaying) R.drawable.ic_pause_circle else R.drawable.ic_play_circle),
            contentDescription = "播放",
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clickable { playOrPause() }
                .padding(start = 1.dp)
                .size(45.dp),
            tint = Color.White)
        Icon(painter = painterResource(id = R.drawable.ic_play_next),
            contentDescription = "下一首",
            modifier = Modifier
                .clickable { next() }
                .padding(17.dp)
                .size(30.dp),
            tint = Color.White)
        Icon(painter = painterResource(id = R.drawable.ic_play_list),
            contentDescription = "播放列表",
            modifier = Modifier
                .clickable { playingList() }
                .padding(20.dp)
                .size(24.dp),
            tint = Color.White)
    }
}

/**
 * 跑马灯
 */
private val edgeWidth = 32.dp
fun ContentDrawScope.drawFadedEdge(leftEdge: Boolean) {
    val edgeWidthPx = edgeWidth.toPx()
    drawRect(
        topLeft = Offset(if (leftEdge) 0f else size.width - edgeWidthPx, 0f),
        size = Size(edgeWidthPx, size.height),
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startX = if (leftEdge) 0f else size.width,
            endX = if (leftEdge) edgeWidthPx else size.width - edgeWidthPx
        ),
        blendMode = BlendMode.DstIn
    )
}