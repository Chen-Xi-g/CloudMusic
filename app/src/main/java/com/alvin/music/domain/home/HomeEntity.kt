package com.alvin.music.domain.home

/**
 * 首页Banner数据
 *
 * @property imageUrl 图片url
 * @property targetId 目标id
 * @property targetType 目标类型
 * @property titleColor 标题颜色
 * @property typeTitle 类型标题
 */
data class HomeBannerEntity(
    val imageUrl: String,
    val targetId: Long,
    val targetType: Int,
    val titleColor: String,
    val typeTitle: String
)

/**
 * 首页Icon数据
 *
 * @property iconUrl 图标url
 * @property name 名称
 * @property url 链接
 */
data class HomeIconEntity(
    val id: Long,
    val iconUrl: String,
    val name: String,
    val url: String
)

/**
 * 首页推荐歌单数据
 *
 * @property id id
 * @property name 名称
 * @property picUrl 图片url
 * @property playCount 播放次数
 * @property type 类型
 * @property child 子数据
 */
data class HomePersonalizedEntity(
    val id: Long,
    val name: String,
    val picUrl: String,
    val playCount: Int,
    val type: Int,
    val child: List<HomePersonalizedEntity>? = null
)

/**
 * 歌曲数据
 *
 * @property id id
 * @property name 名称
 * @property artist 歌手
 * @property albumId 专辑id
 * @property album 专辑
 * @property picUrl 歌单图片url
 * @property mvId mvId
 */
data class HomePersonalizedSongEntity(
    val id: Long,
    val name: String,
    val artist: String,
    val albumId: Long,
    val album: String,
    val picUrl: String,
    val mvId: Long
)

/**
 * 排行榜数据
 *
 * @property id id
 * @property name 名称
 * @property playCount 播放次数
 * @property list 榜单信息
 */
data class HomeTopEntity(
    val id: Long,
    val name: String,
    val playCount: Long,
    val list: List<HomePersonalizedSongEntity>
)

/**
 * 最新专辑数据
 *
 * @property id id
 * @property name 名称
 * @property picUrl 图片url
 * @property artist 歌手
 */
data class HomeAlbumEntity(
    val id: Long,
    val name: String,
    val picUrl: String,
    val artist: String
)

/**
 * 歌曲数据，用于获取播放地址
 *
 * @property id id
 * @property name 名称
 * @property artist 歌手
 * @property picUrl 歌曲图片url
 * @property url 歌曲url
 * @property time 歌曲时长
 * @property payed 是否付费
 */
data class HomeSongEntity(
    val id: Long,
    val name: String,
    val artist: String,
    val picUrl: String,
    var url: String? = null,
    var time: Long = 0,
    var payed: Boolean = false
)

/**
 * 获取歌曲地址请求数据类型
 *
 * @property type 0：歌曲 1：歌单 2：专辑
 * @property list 歌曲列表
 * @property playlistId 歌单id
 * @property songId 歌曲id
 */
data class HomeSongRequestEntity(
    val type: Int,
    val list: List<HomeSongEntity> = listOf(),
    val playlistId: Long = 0,
    val songId: Long = 0
)