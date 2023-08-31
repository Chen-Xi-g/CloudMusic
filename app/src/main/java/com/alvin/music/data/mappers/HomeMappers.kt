package com.alvin.music.data.mappers

import androidx.media3.common.MediaItem
import com.alvin.music.data.dto.home.AlbumSongsDto
import com.alvin.music.data.dto.home.HomeBannerInfoDto
import com.alvin.music.data.dto.home.HomeIconInfoDto
import com.alvin.music.data.dto.home.HomeNewestAlbumInfoDto
import com.alvin.music.data.dto.home.HomePersonalizedInfoDto
import com.alvin.music.data.dto.home.HomePersonalizedSongInfoDto
import com.alvin.music.data.dto.home.HomeTopInfoDto
import com.alvin.music.data.dto.home.SongDetailDto
import com.alvin.music.domain.home.HomeAlbumEntity
import com.alvin.music.domain.home.HomeBannerEntity
import com.alvin.music.domain.home.HomeIconEntity
import com.alvin.music.domain.home.HomePersonalizedEntity
import com.alvin.music.domain.home.HomePersonalizedSongEntity
import com.alvin.music.domain.home.HomeSongEntity
import com.alvin.music.domain.home.HomeTopEntity

/**
 * @Description： 首页Banner数据传输对象转换为首页Banner实体
 *
 * @return 首页Banner实体 [HomeBannerEntity]
 */
fun List<HomeBannerInfoDto>.toHomeBannerList(): List<HomeBannerEntity> {
    return map {
        HomeBannerEntity(
            it.imageUrl,
            it.targetId,
            it.targetType,
            it.titleColor,
            it.typeTitle
        )
    }
}

/**
 * @Description： 首页Icon数据传输对象转换为首页Icon实体
 *
 * @return 首页Icon实体 [HomeIconEntity]
 */
fun List<HomeIconInfoDto>.toHomeIconList(): List<HomeIconEntity> {
    return map {
        HomeIconEntity(
            it.id,
            it.iconUrl,
            it.name,
            it.url
        )
    }
}

/**
 * @Description： 首页推荐歌单数据传输对象转换为首页推荐歌单实体
 *
 * @return 首页推荐歌单实体 [HomePersonalizedEntity]
 */
fun List<HomePersonalizedInfoDto>.toHomePersonalizedList(): List<HomePersonalizedEntity> {
    // 将前四条数据转换为首页推荐歌单实体的子数据，后面的数据转换为首页推荐歌单实体
    if (size > 4) {
        val childList = subList(0, 4).map {
            HomePersonalizedEntity(
                it.id,
                it.name,
                it.picUrl,
                it.playCount,
                it.type
            )
        }
        val parentList = subList(4, size).mapIndexed { index, homePersonalizedInfoDto ->
            if (index == 0) {
                HomePersonalizedEntity(
                    -1,
                    "滚动推荐歌单",
                    "",
                    -1,
                    -1,
                    childList
                )
            } else {
                HomePersonalizedEntity(
                    homePersonalizedInfoDto.id,
                    homePersonalizedInfoDto.name,
                    homePersonalizedInfoDto.picUrl,
                    homePersonalizedInfoDto.playCount,
                    homePersonalizedInfoDto.type
                )
            }
        }
        return parentList
    }
    return map {
        HomePersonalizedEntity(
            it.id,
            it.name,
            it.picUrl,
            it.playCount,
            it.type
        )
    }
}

/**
 * @Description： 首页推荐歌单歌曲数据传输对象转换为首页推荐歌单歌曲实体
 *
 * @return 首页推荐歌单歌曲实体 [HomePersonalizedSongEntity]
 */
fun List<HomePersonalizedSongInfoDto>.toHomePersonalizedSongList(): List<HomePersonalizedSongEntity> {
    return map {
        HomePersonalizedSongEntity(
            it.id,
            it.name,
            it.artist.joinToString(" - ") {artist ->
                artist.name
            },
            it.album.id,
            it.album.name,
            it.album.picUrl,
            it.mvId
        )
    }
}

/**
 * @Description： 歌单歌曲列表转换为播放歌曲数据
 */
fun List<HomePersonalizedSongInfoDto>.toHomeSongs(): List<HomeSongEntity>{
    return map {
        HomeSongEntity(
            id = it.id,
            name = it.name,
            artist = it.artist.joinToString(" - "){ joinTo ->
                joinTo.name
            } + " - " + it.album.name,
            picUrl = it.album.picUrl
        )
    }
}

/**
 * @Description: 专辑歌曲数据转换为播放歌曲数据
 */
fun List<AlbumSongsDto>.toHomeAlbumSongs(): List<HomeSongEntity>{
    return map {
        HomeSongEntity(
            id = it.id,
            name = it.name,
            artist = it.artist.joinToString(" - "){ joinTo ->
                joinTo.name
            } + " - " + it.album.name,
            picUrl = it.album.picUrl
        )
    }
}

/**
 * @Description： 首页榜单数据传输对象转换为首页榜单实体
 *
 * @return 首页榜单实体 [HomeTopEntity]
 */
fun HomeTopInfoDto.toHomeTopEntity(child: List<HomePersonalizedSongInfoDto>): HomeTopEntity{
    return HomeTopEntity(
        id,
        name,
        playCount,
        child.toHomePersonalizedSongList()
    )
}

/**
 * @Description： 首页最新专辑数据传输对象转换为首页最新专辑实体
 *
 * @return 首页最新专辑实体 [HomeAlbumEntity]
 */
fun List<HomeNewestAlbumInfoDto>.toHomeAlbumEntity(): List<HomeAlbumEntity> {
    return map {
        HomeAlbumEntity(
            it.id,
            it.name,
            it.picUrl,
            it.artists.joinToString(" - ") { artist ->
                artist.name
            }
        )
    }
}

/**
 * @Description： 获取歌曲播放地址数据传输对象转换为歌曲播放地址
 *
 * @param list 歌曲详情实体
 *
 * @return 歌曲播放地址 [HomeSongEntity]
 */
fun List<SongDetailDto>.toHomeSongEntity(list: List<HomeSongEntity>): List<HomeSongEntity> {
    forEach { songDetailDto ->
        list.forEach { homeSongEntity ->
            if (songDetailDto.id == homeSongEntity.id) {
                homeSongEntity.url = songDetailDto.url
                homeSongEntity.time = songDetailDto.time
                homeSongEntity.payed = songDetailDto.payed != 0
            }
        }
    }
    return list
}

/**
 * @Description： 首页推荐歌单歌曲数据传输对象转换为歌曲播放地址
 *
 * @return 歌曲播放地址 [HomeSongEntity]
 */
fun List<HomePersonalizedSongEntity>.toHomeSongEntity(): List<HomeSongEntity> {
    return map {
        HomeSongEntity(
            id = it.id,
            name = it.name,
            artist = it.artist,
            picUrl = it.picUrl
        )
    }
}

/**
 * @Description： 歌曲信息转换为媒体信息
 */
fun List<HomeSongEntity>.toMediaInfo(): List<MediaItem> {
    return map {
        MediaItem.fromUri(it.url ?: "")
    }
}