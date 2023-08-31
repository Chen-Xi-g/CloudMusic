package com.alvin.music.ui.home

import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.Player
import com.alvin.music.R
import com.alvin.music.data.mappers.toHomeSongEntity
import com.alvin.music.domain.home.HomeAlbumEntity
import com.alvin.music.domain.home.HomeBannerEntity
import com.alvin.music.domain.home.HomeIconEntity
import com.alvin.music.domain.home.HomePersonalizedEntity
import com.alvin.music.domain.home.HomePersonalizedSongEntity
import com.alvin.music.domain.home.HomeSongRequestEntity
import com.alvin.music.domain.home.HomeTopEntity
import com.alvin.music.domain.util.loadImage
import com.alvin.music.domain.util.toast
import com.alvin.music.ui.player.BottomPlayerTab
import com.alvin.music.ui.player.PlayAction
import com.alvin.music.ui.player.PlayDetailScreen
import com.alvin.music.ui.player.PlayListDialog
import com.alvin.music.ui.player.PlayViewModel
import com.alvin.music.ui.theme.COLOR_15_EB3B1C
import com.alvin.music.ui.theme.COLOR_7A7FA0
import com.alvin.music.ui.theme.COLOR_80_000
import com.alvin.music.ui.theme.COLOR_C17D56
import com.alvin.music.ui.theme.COLOR_D19C45
import com.alvin.music.ui.theme.LocalCustomColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.ceil
import kotlin.time.Duration.Companion.seconds


/**
 * @Description ：   主页面
 * @Date        ：   2023/6/26
 * @author      ：   高国峰
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    viewModel: HomeViewModel = hiltViewModel(),
    playViewModel: PlayViewModel = hiltViewModel(),
) {
    val systemUi = rememberSystemUiController()
    val isDark = LocalCustomColor.current.isDark
    val context = LocalContext.current
    // 页面数据
    val state = viewModel.state
    val playState = playViewModel.state
    // 底部弹窗状态
    val bottomState = rememberBottomSheetScaffoldState(
        rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    // 底部是否可见
    val bottomVisibleState = remember {
        MutableTransitionState(true).apply {
            targetState = true
        }
    }
    // 播放列表弹窗
    var isListDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    // 监听底部播放的隐藏动画是否执行完毕
    LaunchedEffect(
        key1 = bottomVisibleState.currentState
    ) {
        if (!bottomVisibleState.currentState && bottomVisibleState.isIdle) {
            scope.launch {
                // 执行完毕，开始展开底部弹窗
                bottomState.bottomSheetState.expand()
            }
        }
    }
    // 监听底部弹窗是否关闭
    LaunchedEffect(key1 = bottomState.bottomSheetState.isVisible) {
        if (!bottomState.bottomSheetState.isVisible) {
            // 弹窗已关闭，开始执行底部播放的显示动画
            bottomVisibleState.targetState = true
            systemUi.systemBarsDarkContentEnabled = !isDark
        } else {
            systemUi.systemBarsDarkContentEnabled = false
        }
    }
    // 监听Back键
    BackHandler(enabled = true) {
        if (bottomState.bottomSheetState.isVisible) {
            // 底部弹窗已展开，关闭弹窗
            scope.launch {
                bottomState.bottomSheetState.hide()
            }
        } else {
            // 返回桌面，不关闭应用，使用Intent.ACTION_MAIN
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addCategory(Intent.CATEGORY_HOME)
            context.startActivity(intent)
        }
    }
    if (state.isLoading || playState.isLoading) {
        Loading()
    }
    BottomSheetScaffold(
        scaffoldState = bottomState,
        sheetPeekHeight = 0.dp,
        sheetDragHandle = null,
        sheetContent = {
            if (playState.list.isNotEmpty()) {
                // 展开时的内容
                PlayDetailScreen(
                    playState = playState,
                    progressState = playViewModel.playProgress,
                    seekTo = {
                        playViewModel.sendAction(PlayAction.SeekTo(it))
                    },
                    previous = {
                        playViewModel.sendAction(PlayAction.Previous)
                    },
                    next = {
                        playViewModel.sendAction(PlayAction.Next)
                    },
                    playOrPause = {
                        playViewModel.sendAction(PlayAction.PlayPause)
                    },
                    switchSong = {
                        if (playState.currentSong != it) {
                            playViewModel.sendAction(PlayAction.SwitchSong(it))
                        }
                    }, changeMode = {
                        when (it) {
                            Player.REPEAT_MODE_ONE -> {
                                "单曲循环".toast()
                            }
                            Player.REPEAT_MODE_ALL -> {
                                "列表循环".toast()
                            }
                            3 -> {
                                "随机播放".toast()
                            }
                        }
                        playViewModel.sendAction(PlayAction.ChangeMode(it))
                    }, removeSong = {
                        playViewModel.sendAction(PlayAction.RemoveSong(it))
                    }) {
                    // 关闭底部弹窗
                    scope.launch {
                        bottomState.bottomSheetState.hide()
                    }
                }
            }
        }, modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                LocalCustomColor.current.homeTopBackground,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )
            HomeContent(
                banners = state.banners,
                icons = state.icons,
                personalized = state.personalized,
                isPersonalizedSingRefresh = state.isPersonalizedSingRefresh,
                personalizedSing = state.personalizedSing,
                personalizedSings = state.personalizedSingSong,
                tops = state.topList,
                albums = state.newestAlbums,
                refreshPersonalizedListener = {
                    viewModel.refreshPersonalizedSongs()
                }
            ) {
                playViewModel.getSongList(it)
            }
            if (playState.currentSong != null) {
                AnimatedVisibility(
                    visibleState = bottomVisibleState,
                    enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                    exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }),
                    modifier = Modifier.align(Alignment.BottomCenter),
                ) {
                    BottomPlayerTab(
                        songEntity = playState.currentSong,
                        progressState = playViewModel.playProgress,
                        isPlaying = playState.isPlaying,
                        playerClickListener = {
                            scope.launch {
                                playViewModel.sendAction(PlayAction.PlayPause)
                            }
                        }, playingListClickListener = {
                            isListDialog = true
                        }) {
                        bottomVisibleState.targetState = false
                    }
                }
            }
        }
    }
    if (isListDialog){
        PlayListDialog(playState.list, playState.currentSong, onClickItem = {
            if (playState.currentSong != it) {
                playViewModel.sendAction(PlayAction.SwitchSong(it))
            }
        }, onRemoveSong = {
            playViewModel.sendAction(PlayAction.RemoveSong(it))
        }){
            isListDialog = false
        }
    }
}

/**
 * 首页内容
 *
 * @param banners 轮播图数据
 * @param icons 首页菜单数据
 * @param personalized 推荐歌单数据
 * @param isPersonalizedSingRefresh 是否刷新推荐歌单
 * @param personalizedSing 推荐歌单数据
 * @param personalizedSings 推荐歌单数据
 * @param tops 排行榜数据
 * @param albums 新碟上架数据
 * @param playClickListener 播放点击事件（true请求歌曲列表数据/false请求歌单列表数据，list歌曲列表数据，id歌单列表数据）
 */
@Composable
private fun HomeContent(
    banners: List<HomeBannerEntity>,
    icons: List<HomeIconEntity>,
    personalized: List<HomePersonalizedEntity>,
    isPersonalizedSingRefresh: Boolean,
    personalizedSing: HomePersonalizedEntity?,
    personalizedSings: List<HomePersonalizedSongEntity>?,
    tops: List<HomeTopEntity>?,
    albums: List<HomeAlbumEntity>?,
    refreshPersonalizedListener: () -> Unit,
    playClickListener: (HomeSongRequestEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        // 首页顶部菜单及搜索框
        HomeTopBar()
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // 首页轮播图
            HomeBanner(banners)
            Spacer(modifier = Modifier.height(6.dp))
            // 首页发现
            HomeDiscovery(icons)
            Spacer(modifier = Modifier.height(20.dp))
            // 首页推荐歌单
            HomeRecommended(personalized) {
                playClickListener(
                    HomeSongRequestEntity(
                        1,
                        playlistId = it,
                    )
                )
            }
            Spacer(modifier = Modifier.height(35.dp))
            // 首页推荐歌曲
            if (personalizedSing != null && personalizedSings != null) {
                HomeRecommendedSong(
                    isPersonalizedSingRefresh,
                    personalizedSing,
                    personalizedSings,
                    songItemClickListener = {
                        // 播放歌曲
                        playClickListener(
                            HomeSongRequestEntity(
                                1,
                                songId = it,
                                playlistId = personalizedSing.id
                            )
                        )
                    }) {
                    // 刷新歌曲
                    refreshPersonalizedListener()
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
            // 首页排行榜
            HomeLeaderboardSong(tops, playClickListener)
            Spacer(modifier = Modifier.height(35.dp))
            // 新歌碟片
            HomeAlbum(albums, playClickListener)
            Spacer(modifier = Modifier.height(75.dp))
        }
    }
}

/**
 * 首页顶部菜单及搜索框
 *
 * @param modifier
 */
@Composable
private fun HomeTopBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "菜单",
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .size(28.dp, 28.dp),
            tint = LocalCustomColor.current.iconTint
        )
        Row(
            modifier = Modifier
                .padding(end = 25.dp)
                .weight(1F)
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(LocalCustomColor.current.searchBackground)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "搜索",
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "搜索",
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 首页Banner
 *
 * @param bannerList 轮播图数据
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeBanner(bannerList: List<HomeBannerEntity>) {
    val pageCount = bannerList.size
    val startIndex = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(
        initialPage = startIndex
    )
    var pageCounter by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(key1 = pageCounter, block = {
        delay(3.seconds)
        if (pagerState.currentPage == Int.MAX_VALUE - 1) {
            pagerState.animateScrollToPage(page = 0)
        } else {
            pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
        }
        pageCounter++
    })
    HorizontalPager(
        pageCount = Int.MAX_VALUE,
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(135.dp)
    ) { index ->
        val page = (index - startIndex).floorMod(pageCount)
        if (bannerList.isNotEmpty()) {
            key(bannerList[page].imageUrl) {
                CoilImage(
                    imageModel = { bannerList[page].imageUrl },
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth()
                        .height(135.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

/**
 * 首页发现
 *
 * @param list 发现列表
 */
@Composable
private fun HomeDiscovery(list: List<HomeIconEntity>) {
    val displayMetrics = LocalContext.current.resources.displayMetrics
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(list) { item ->
            /*注意：发现Item宽度为屏幕的宽度-间距的1/5*/
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.width(
                    ((displayMetrics.widthPixels - 30.dp.value) / 5).toInt().pxToDp()
                )
            ) {
                HomeDiscoverIcon(item.iconUrl, item.id)
                Text(
                    text = item.name,
                    color = LocalCustomColor.current.subText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

/**
 * 首页发现的Icon图标，将图片绘制为渐变色
 *
 * @param url 图片地址
 * @param iconId 图标ID
 */
@OptIn(ExperimentalTextApi::class)
@Composable
private fun HomeDiscoverIcon(url: String, iconId: Long) {
    val startColor = LocalCustomColor.current.iconDiscoverStart
    val endColor = LocalCustomColor.current.iconDiscoverEnd
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.titleSmall.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.ExtraBold
    )
    CoilImage(
        imageModel = {
            url
        }, imageOptions = ImageOptions(contentScale = ContentScale.FillBounds), modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .size(50.dp, 48.dp)
            .graphicsLayer { alpha = 0.99F }
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.linearGradient(
                        listOf(
                            startColor,
                            endColor
                        ),
                        start = Offset(Float.POSITIVE_INFINITY, 0F),
                        end = Offset(0F, Float.POSITIVE_INFINITY)
                    ),
                    blendMode = BlendMode.SrcIn
                )
                /*IconId为-1时，当前的Icon为每日推荐。绘制当前日期的日*/
                if (iconId == -1L) {
                    // 获取当前日期的日
                    val day = Calendar
                        .getInstance()
                        .get(Calendar.DAY_OF_MONTH)
                        .toString()
                    // 使用drawText居中绘制文字
                    drawText(
                        textMeasurer,
                        day,
                        style = textStyle,
                        topLeft = Offset(
                            (size.width - textMeasurer.measure(day, textStyle).size.width) / 2,
                            (size.height - textMeasurer.measure(
                                day,
                                textStyle
                            ).size.height) / 2 + 2.dp.toPx()
                        ),
                        blendMode = BlendMode.Clear
                    )
                }
            }
    )
}

/**
 * 首页推荐歌单
 *
 * @param list 推荐歌单列表
 */
@Composable
private fun HomeRecommended(list: List<HomePersonalizedEntity>, clickItemListener: (Long) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "推荐歌单",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = LocalCustomColor.current.text,
                modifier = Modifier.padding(start = 15.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "更多",
                tint = LocalCustomColor.current.text,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .size(11.dp)
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 15.dp)
        ) {
            items(list) { item ->
                if (item.child.isNullOrEmpty()) {
                    HomeRecommendedItem(item, clickItemListener)
                } else {
                    HomeRecommendedScrollItem(item, clickItemListener)
                }
            }
        }
    }
}

/**
 * 首页推荐歌单的Item
 */
@Composable
private fun HomeRecommendedItem(item: HomePersonalizedEntity, clickItemListener: (Long) -> Unit) {
    Column(
        modifier = Modifier
            .padding(end = 10.dp)
            .width(120.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 13.dp)
                .size(120.dp, 124.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(LocalCustomColor.current.cardShadow)
            )
            /*歌单封面*/
            CoilImage(
                imageModel = {
                    item.picUrl.loadImage(120, 120)
                }, modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
            /*播放量*/
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                COLOR_80_000,
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
                    .padding(vertical = 10.dp, horizontal = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = "播放量",
                    modifier = Modifier.size(10.dp),
                    tint = White
                )
                Text(
                    text = if (item.playCount > 10000) {
                        "${item.playCount / 10000}万"
                    } else {
                        item.playCount.toString()
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = White,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
            /*播放事件*/
            Icon(
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = "播放",
                modifier = Modifier
                    .size(38.dp)
                    .clickable { clickItemListener(item.id) }
                    .padding(8.dp)
                    .align(
                        Alignment.BottomEnd
                    ),
                tint = White
            )
        }
        Text(
            text = item.name,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal
            ),
            color = LocalCustomColor.current.text,
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
        )
    }

}

/**
 * 首页推荐歌单的滚动Item
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
private fun HomeRecommendedScrollItem(
    item: HomePersonalizedEntity,
    clickItemListener: (Long) -> Unit
) {
    if (item.child.isNullOrEmpty()) return
    val pageCount = item.child.size
    val startIndex = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(
        initialPage = startIndex
    )
    var pageCounter by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(key1 = pageCounter, block = {
        delay(5.seconds)
        if (pagerState.currentPage == Int.MAX_VALUE - 1) {
            pagerState.animateScrollToPage(page = 0)
        } else {
            pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
        }
        pageCounter++
    })

    Column(
        modifier = Modifier
            .padding(end = 10.dp)
            .width(120.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 13.dp)
                .size(120.dp, 124.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(LocalCustomColor.current.cardShadow)
            )
            /*动态切换歌单*/
            VerticalPager(
                pageCount = Int.MAX_VALUE,
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) { index ->
                val page = (index - startIndex).floorMod(pageCount)
                Box(modifier = Modifier
                    .fillMaxSize()
                    .clickable { clickItemListener(item.child[page].id) }) {
                    /*歌单封面*/
                    CoilImage(
                        imageModel = {
                            item.child[page].picUrl.loadImage(120, 120)
                        }, modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopEnd)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                COLOR_80_000,
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_unlimited),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 8.dp, end = 8.dp)
                        .size(30.dp, 15.dp)
                        .align(Alignment.TopEnd),
                    tint = White
                )
            }
        }
        /*歌单标题修改动画*/
        AnimatedContent(targetState = pagerState.currentPage, transitionSpec = {
            fadeIn() with fadeOut()
        }) { targetState ->
            Text(
                text = item.child[(targetState - startIndex).floorMod(pageCount)].name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = LocalCustomColor.current.text,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
            )
        }
    }
}

/**
 * 首页推荐歌曲
 *
 * @param isPersonalizedSingRefresh 是否刷新
 * @param personalizedSing 推荐歌曲
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeRecommendedSong(
    isPersonalizedSingRefresh: Boolean,
    personalizedSing: HomePersonalizedEntity,
    personalizedSingSong: List<HomePersonalizedSongEntity>,
    songItemClickListener: (id: Long) -> Unit,
    clickRefresh: () -> Unit
) {
    // 创建刷新的旋转动画
    val infiniteTransition = rememberInfiniteTransition()
    var isRefresh by remember {
        mutableStateOf(false)
    }
    isRefresh = isPersonalizedSingRefresh
    val rotate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(end = 15.dp)
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_refresh),
                contentDescription = "刷新",
                tint = LocalCustomColor.current.text,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .size(18.dp)
                    .clickable {
                        clickRefresh()
                    }
                    .rotate(if (isRefresh) rotate else 0f)
            )
            Text(
                text = personalizedSing.name ?: "",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LocalCustomColor.current.text,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .weight(1F)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(LocalCustomColor.current.searchBackground)
                    .padding(start = 6.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = "播放",
                    tint = LocalCustomColor.current.text,
                    modifier = Modifier.size(9.dp)
                )
                Text(
                    text = "播放",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = LocalCustomColor.current.iconTint,
                        fontSize = 10.sp
                    ),
                    modifier = Modifier.padding(start = 3.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(22.dp))
        val pageCount = if (personalizedSingSong.isEmpty()) {
            // 数据为空，显示0页
            0
        } else {
            // 向上取整
            ceil(personalizedSingSong.size / 3.0).toInt()
        }
        HorizontalPager(
            pageCount = pageCount,
            contentPadding = PaddingValues(horizontal = 15.dp)
        ) { page ->
            Column {
                personalizedSingSong.subList(page * 3, (page + 1) * 3).forEach {
                    HomeRecommendedSongItem(it, songItemClickListener)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

/**
 * 首页推荐歌曲的Item
 */
@Composable
private fun HomeRecommendedSongItem(
    song: HomePersonalizedSongEntity,
    songItemClickListener: (id: Long) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(end = 20.dp)
            .clickable { songItemClickListener(song.id) }
            .fillMaxWidth()
    ) {
        CoilImage(
            imageModel = { song.picUrl.loadImage(55, 55) }, modifier = Modifier
                .size(55.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1F)
        ) {
            Text(
                text = song.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            // 提示文字
            Text(
                text = song.artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = LocalCustomColor.current.searchText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (song.mvId > 0) {
            Icon(
                painter = painterResource(R.drawable.ic_video_play),
                contentDescription = "播放MV",
                modifier = Modifier.size(16.dp),
                tint = LocalCustomColor.current.searchText
            )
        }
    }
}


/**
 * 首页排行榜歌曲
 *
 * @param list 排行榜歌曲列表
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeLeaderboardSong(
    list: List<HomeTopEntity>?,
    playClickListener: (HomeSongRequestEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "排行榜",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = LocalCustomColor.current.text,
                modifier = Modifier.padding(start = 15.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "更多",
                tint = LocalCustomColor.current.text,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .size(11.dp)
            )
        }
        Spacer(modifier = Modifier.height(22.dp))
        HorizontalPager(
            pageCount = list?.size ?: 0,
            contentPadding = PaddingValues(horizontal = 15.dp)
        ) { page ->
            if (!list.isNullOrEmpty()) {
                HomeLeaderboardSongPage(list[page], playClickListener)
            }
        }
    }
}

/**
 * 首页排行榜歌曲的Page
 */
@Composable
private fun HomeLeaderboardSongPage(
    item: HomeTopEntity,
    playClickListener: (HomeSongRequestEntity) -> Unit
) {
    Column(
        Modifier
            .padding(end = 20.dp)
            .fillMaxWidth()
            .shadow(
                ambientColor = LocalCustomColor.current.cardShadow,
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .background(LocalCustomColor.current.cardBackground, RoundedCornerShape(8.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = LocalCustomColor.current.text
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "更多",
                tint = LocalCustomColor.current.text,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .size(11.dp)
            )
            Spacer(modifier = Modifier.weight(1F))
            Text(
                text = "${item.playCount / 10000}万人在听",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = LocalCustomColor.current.searchText
                )
            )
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            item.list.forEachIndexed { index, homePersonalizedSongEntity ->
                HomeLeaderboardSongItem(index, homePersonalizedSongEntity) { id ->
                    playClickListener(HomeSongRequestEntity(1, playlistId = item.id, songId = id))
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

/**
 * 首页排行榜歌曲的Item
 *
 * @param index 排名
 * @param item 歌曲
 * @param songItemClickListener 歌曲点击事件（id歌曲id）
 */
@Composable
private fun HomeLeaderboardSongItem(
    index: Int,
    item: HomePersonalizedSongEntity,
    songItemClickListener: (id: Long) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .clickable { songItemClickListener(item.id) }
    ) {
        CoilImage(
            imageModel = { item.picUrl.loadImage(40, 40) }, modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Icon(
            painter = painterResource(id = if (index == 0) R.drawable.ic_top_one else if (index == 1) R.drawable.ic_top_two else R.drawable.ic_top_three),
            contentDescription = "排名$index",
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .size(12.dp)
                .graphicsLayer { alpha = 0.99F }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        color = if (index == 0) COLOR_D19C45 else if (index == 1) COLOR_7A7FA0 else COLOR_C17D56,
                        blendMode = BlendMode.SrcIn
                    )
                }
        )
        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = item.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = LocalCustomColor.current.searchText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
        Text(
            text = "霸榜",
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.Red,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp
            ),
            modifier = Modifier.padding(15.dp)
        )
    }
}

/**
 * 最新专辑
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeAlbum(
    list: List<HomeAlbumEntity>?,
    playClickListener: (HomeSongRequestEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "最新专辑",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = LocalCustomColor.current.text,
                modifier = Modifier.padding(start = 15.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "更多",
                tint = LocalCustomColor.current.text,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .size(11.dp)
            )
        }
        Spacer(modifier = Modifier.height(22.dp))
        val pageCount = if (list.isNullOrEmpty()) {
            // 数据为空，显示0页
            0
        } else {
            // 向上取整
            ceil(list.size / 3.0).toInt()
        }
        HorizontalPager(
            pageCount = pageCount,
            contentPadding = PaddingValues(horizontal = 15.dp)
        ) { page ->
            Column(modifier = Modifier.fillMaxWidth()) {
                list?.subList(page * 3, (page + 1) * 3)?.forEach { item ->
                    HomeAlbumItem(item) {
                        playClickListener(
                            HomeSongRequestEntity(
                                2, playlistId = it
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

/**
 * 数字专辑、新碟上架的Item
 */
@Composable
private fun HomeAlbumItem(item: HomeAlbumEntity, clickItemListener: (Long) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(end = 20.dp)
            .fillMaxWidth()
            .clickable {
                clickItemListener(item.id)
            }
    ) {
        CoilImage(
            imageModel = { item.picUrl.loadImage(55, 55) }, modifier = Modifier
                .size(55.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1F)
        ) {
            Text(
                text = item.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(COLOR_15_EB3B1C)
                        .padding(2.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "超过72%的人播放", style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 9.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "热门播放",
                        modifier = Modifier.size(6.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                // 提示文字
                Text(
                    text = item.artist,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = LocalCustomColor.current.searchText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }
        }
    }
}

/**
 * 计算当前Page
 *
 * @param other Int
 * @return
 */
private fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}

@Composable
fun Loading() {
    Dialog(
        onDismissRequest = { },
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(White, shape = RoundedCornerShape(8.dp))
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Preview(
    showBackground = true,
    name = "首页",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    heightDp = 1080
)
@Composable
fun HomeLightPreview() {
    HomePage()
}