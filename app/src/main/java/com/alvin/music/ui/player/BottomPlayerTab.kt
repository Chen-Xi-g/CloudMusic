package com.alvin.music.ui.player

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alvin.music.R
import com.alvin.music.domain.home.HomeSongEntity
import com.alvin.music.ui.theme.COLOR_222
import com.alvin.music.ui.theme.COLOR_50_333
import com.alvin.music.ui.theme.COLOR_50_FFF
import com.alvin.music.ui.theme.COLOR_80_000
import com.alvin.music.ui.theme.LocalCustomColor
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.transformation.blur.BlurTransformationPlugin
import kotlinx.coroutines.flow.StateFlow

/**
 * 底部播放器Tab
 *
 * @param modifier
 * @param songEntity 当前播放歌曲
 * @param progressState 播放进度
 * @param isPlaying 是否正在播放
 * @param playerClickListener 播放按钮点击事件
 * @param playingListClickListener 播放列表点击事件
 * @param onBottomTabClick 底部Tab点击事件
 * @receiver
 */
@Composable
fun BottomPlayerTab(
    modifier: Modifier = Modifier,
    songEntity: HomeSongEntity,
    progressState: StateFlow<Long>,
    isPlaying: Boolean,
    playerClickListener: () -> Unit,
    playingListClickListener: () -> Unit,
    onBottomTabClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = LocalCustomColor.current.playerBackground)
            .clickable(
                onClick = onBottomTabClick,
                interactionSource = interactionSource,
                indication = null
            )
            .navigationBarsPadding()
            .padding(start = 18.dp, end = 18.dp, top = 5.dp, bottom = 10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            CoilImage(
                imageModel = {
                    songEntity.picUrl
                }, modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "${songEntity.name} - ${songEntity.artist}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = LocalCustomColor.current.text,
                modifier = Modifier
                    .weight(1F)
            )
            Spacer(modifier = Modifier.width(10.dp))
            PlayerProgress(progressState, isPlaying, songEntity.time, playerClickListener)
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_play_list),
                contentDescription = "播放列表",
                tint = LocalCustomColor.current.text,
                modifier = Modifier
                    .clickable { playingListClickListener() }
                    .padding(5.dp)
                    .size(20.dp)
            )
        }
    }
}

@Composable
private fun PlayerProgress(
    progressState: StateFlow<Long>,
    isPlaying: Boolean,
    totalProgress: Long,
    playerClickListener: () -> Unit
) {
    val progressStateValue = progressState.collectAsState(
        initial = 0L
    ).value
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(5.dp)
                .size(22.dp),
            color = LocalCustomColor.current.text,
            trackColor = LocalCustomColor.current.playerTrackBackground,
            strokeWidth = 1.5.dp,
            progress = (progressStateValue.toFloat() / totalProgress.toFloat()),
        )
        Icon(
            painter = painterResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
            contentDescription = "播放状态",
            modifier = Modifier
                .clickable {
                    playerClickListener()
                }
                .padding(start = if (isPlaying) 0.dp else 2.dp)
                .size(15.dp),
            tint = LocalCustomColor.current.text
        )
    }
}