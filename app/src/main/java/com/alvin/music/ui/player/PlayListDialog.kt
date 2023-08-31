package com.alvin.music.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.alvin.music.R
import com.alvin.music.domain.home.HomeSongEntity
import com.alvin.music.ui.theme.COLOR_D4205D
import com.alvin.music.ui.theme.LocalCustomColor

/**
 * 正在播放列表
 *
 * @param list 正在播放列表
 */
@Composable
fun PlayListDialog(
    list: List<HomeSongEntity>,
    currentSong: HomeSongEntity?,
    onClickItem: (HomeSongEntity) -> Unit,
    onRemoveSong: (HomeSongEntity) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDismissRequest() }, contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .clickable { }
                    .height(configuration.screenHeightDp.dp * 0.7f)
                    .background(
                        LocalCustomColor.current.cardBackground, RoundedCornerShape(12.dp)
                    )
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = LocalCustomColor.current.text,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("正在播放")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = LocalCustomColor.current.subText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("(${list.size})")
                        }
                    },
                    modifier = Modifier.padding(15.dp),
                    color = LocalCustomColor.current.text
                )
                LazyColumn {
                    items(list.size) {
                        PlayListDialogItem(list[it], currentSong, onClickItem, onRemoveSong)
                    }
                }
            }
        }

    }
}

@Composable
fun PlayListDialogItem(
    item: HomeSongEntity,
    currentSong: HomeSongEntity?,
    onClickItem: (HomeSongEntity) -> Unit,
    onRemoveSong: (HomeSongEntity) -> Unit,
) {
    val isPlaying = item.id == currentSong?.id
    Row(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxSize()
            .clickable { onClickItem(item) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(15.dp))
        if (isPlaying) {
            /*正在播放*/
            Icon(
                painter = painterResource(id = R.drawable.ic_list_item_playing),
                contentDescription = null,
                tint = COLOR_D4205D,
                modifier = Modifier.size(10.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = if (isPlaying) COLOR_D4205D else LocalCustomColor.current.text,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append(item.name)
                }
                withStyle(
                    style = SpanStyle(
                        color = if (isPlaying) COLOR_D4205D else LocalCustomColor.current.subText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append(" - ${item.artist}")
                }
            }, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                .weight(1F)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "close",
            tint = LocalCustomColor.current.searchBackground,
            modifier = Modifier
                .clickable { onRemoveSong(item) }
                .padding(horizontal = 15.dp)
                .size(10.dp)
        )
    }
}