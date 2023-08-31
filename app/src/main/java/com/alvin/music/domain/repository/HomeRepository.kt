package com.alvin.music.domain.repository

import com.alvin.music.data.dto.home.HomeTopListDto
import com.alvin.music.domain.home.HomeAlbumEntity
import com.alvin.music.domain.home.HomeBannerEntity
import com.alvin.music.domain.home.HomeIconEntity
import com.alvin.music.domain.home.HomePersonalizedEntity
import com.alvin.music.domain.home.HomePersonalizedSongEntity
import com.alvin.music.domain.home.HomeSongEntity
import com.alvin.music.domain.home.HomeTopEntity
import com.alvin.music.domain.util.Resource

/**
 * @Description ：   首页数据仓库
 * @Date        ：   2023/7/2
 * @author      ：   高国峰
 */
interface HomeRepository {

    /**
     * 获取首页Banner数据
     */
    suspend fun getBanner(): Resource<List<HomeBannerEntity>>

    /**
     * 获取首页发现Icon数据
     */
    suspend fun getDragonBall(): Resource<List<HomeIconEntity>>

    /**
     * 获取首页推荐歌单数据
     */
    suspend fun getPersonalized(limit: Int): Resource<List<HomePersonalizedEntity>>

    /**
     * 获取歌单详情
     */
    suspend fun getPersonalizedSongs(id: Long, limit: Int): Resource<List<HomePersonalizedSongEntity>>

    /**
     * 获取排行榜数据
     */
    suspend fun getTopList(): Resource<List<HomeTopEntity>>

    /**
     * 获取最新专辑数据
     */
    suspend fun getNewestAlbum(): Resource<List<HomeAlbumEntity>>

    /**
     * 获取歌曲
     */
    suspend fun getSongUrl(list: List<HomeSongEntity>): Resource<List<HomeSongEntity>>

    /**
     * 根据歌单获取歌曲列表详情
     *
     * @param id 歌单id
     */
    suspend fun getSongListDetail(id: Long): Resource<List<HomeSongEntity>>

    /**
     * 获取专辑歌曲列表
     *
     * @param id 专辑id
     */
    suspend fun getAlbumSongList(id: Long): Resource<List<HomeSongEntity>>

}