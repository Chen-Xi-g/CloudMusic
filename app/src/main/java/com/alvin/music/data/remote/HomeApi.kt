package com.alvin.music.data.remote

import com.alvin.music.data.dto.home.HomeAlbumSongDto
import com.alvin.music.data.dto.home.HomeBannerDto
import com.alvin.music.data.dto.home.HomeIconDto
import com.alvin.music.data.dto.home.HomeNewestAlbumDto
import com.alvin.music.data.dto.home.HomePersonalizedDto
import com.alvin.music.data.dto.home.HomePersonalizedSongDto
import com.alvin.music.data.dto.home.HomeSongDetailDto
import com.alvin.music.data.dto.home.HomeTopListDto
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

/**
 * @Description ：   首页接口
 * @Date        ：   2023/7/2
 * @author      ：   高国峰
 */
interface HomeApi {

    /**
     * 获取首页Banner数据
     */
    @GET("banner")
    suspend fun getBanner(@Query("timeStamp") timeStamp: Long = Date().time): HomeBannerDto

    /**
     * 获取首页发现Icon数据
     */
    @GET("homepage/dragon/ball")
    suspend fun getDragonBall(@Query("timeStamp") timeStamp: Long = Date().time): HomeIconDto

    /**
     * 获取首页推荐歌单数据
     *
     * @param limit 返回数量
     */
    @GET("personalized")
    suspend fun getRecommendPlayList(
        @Query("limit") limit: Int = 10,
        @Query("timeStamp") timeStamp: Long = Date().time
    ): HomePersonalizedDto

    /**
     * 获取歌单所有歌曲
     *
     * @param id 歌单id
     * @param limit 返回数量
     * @param offset 偏移量
     */
    @GET("playlist/track/all")
    suspend fun getPlayListDetail(
        @Query("id") id: Long,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("timeStamp") timeStamp: Long = Date().time
    ): HomePersonalizedSongDto

    /**
     * 获取首页排行榜数据
     */
    @GET("toplist/detail")
    suspend fun getTopList(@Query("timeStamp") timeStamp: Long = Date().time): HomeTopListDto

    /**
     * 获取最新专辑数据
     */
    @GET("album/newest")
    suspend fun getNewestAlbum(@Query("timeStamp") timeStamp: Long = Date().time): HomeNewestAlbumDto

    /**
     * 获取歌曲
     *
     * @param ids 歌曲id，多个用逗号隔开
     * @param level 音质
     */
    @GET("song/url/v1")
    suspend fun getSongUrl(
        @Query("id") ids: String,
        @Query("level") level: String = "exhigh",
        @Query("timeStamp") timeStamp: Long = Date().time
    ): HomeSongDetailDto

    /**
     * 获取专辑歌曲数据
     *
     * @param id 专辑id
     */
    @GET("album")
    suspend fun getAlbumDetail(
        @Query("id") id: Long,
        @Query("timeStamp") timeStamp: Long = Date().time
    ): HomeAlbumSongDto

}