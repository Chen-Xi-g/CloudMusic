package com.alvin.music.data.dto.home
import com.alvin.music.data.dto.BaseDto
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName

/**
 * @Description ：   首页Banner数据传输对象
 *
 * @property banners 轮播图数据
 * @property code 响应码
 * @constructor Create empty Home icon dto
 */
@Serializable
data class HomeBannerDto(
    @SerialName("banners")
    val banners: List<HomeBannerInfoDto> = emptyList()
): BaseDto()

/**
 * @Description ：   首页Icon数据传输对象
 *
 * @property data 首页Icon数据
 * @constructor Create empty Home icon dto
 */
@Serializable
data class HomeIconDto(
    val data: List<HomeIconInfoDto> = emptyList()
): BaseDto()

/**
 * @Description ：   首页推荐歌单数据传输对象
 *
 * @property result 首页推荐歌单数据
 * @constructor Create empty Home personalized dto
 */
@Serializable
data class HomePersonalizedDto(
    val result: List<HomePersonalizedInfoDto> = emptyList()
): BaseDto()

/**
 * @Description ：   首页推荐歌单歌曲数据传输对象详情
 *
 * @property songs
 * @constructor Create empty Home personalized song dto
 */
@Serializable
data class HomePersonalizedSongDto(
    val songs: List<HomePersonalizedSongInfoDto> = emptyList(),
): BaseDto()

/**
 * @Description ：   首页排行榜数据传输对象
 *
 * @property list
 * @constructor Create empty Home top list dto
 */
@Serializable
data class HomeTopListDto(
    val list: List<HomeTopInfoDto> = emptyList(),
): BaseDto()

/**
 * @Description ：   首页最新专辑数据传输对象
 *
 * @property albums
 */
@Serializable
data class HomeNewestAlbumDto(
    val albums: List<HomeNewestAlbumInfoDto> = emptyList(),
): BaseDto()

/**
 * @Description ：   歌曲详情数据传输对象
 */
@Serializable
data class HomeSongDetailDto(
    val data: List<SongDetailDto> = emptyList(),
): BaseDto()

/**
 * @Description ：   专辑歌曲列表数据传输对象
 */
@Serializable
data class HomeAlbumSongDto(
    val songs: List<AlbumSongsDto> = emptyList(),
): BaseDto()

/**
 * @Description ：   首页Banner数据传输对象详情
 *
 * @property bannerBizType banner业务类型
 * @property encodeId 编码id
 * @property exclusive 是否独家
 * @property imageUrl 图片url
 * @property scm scm
 * @property targetId 目标id
 * @property targetType 目标类型
 * @property titleColor 标题颜色
 * @property typeTitle 类型标题
 * @property url 链接
 * @constructor Create empty Home banner info dto
 */
@Serializable
data class HomeBannerInfoDto(
    @SerialName("bannerBizType")
    val bannerBizType: String = "",
    @SerialName("encodeId")
    val encodeId: String = "",
    @SerialName("exclusive")
    val exclusive: Boolean = false,
    @SerialName("imageUrl")
    val imageUrl: String = "",
    @SerialName("scm")
    val scm: String = "",
    @SerialName("targetId")
    val targetId: Long = 0,
    @SerialName("targetType")
    val targetType: Int = 0,
    @SerialName("titleColor")
    val titleColor: String = "",
    @SerialName("typeTitle")
    val typeTitle: String = "",
    @SerialName("url")
    val url: String = ""
)

/**
 * 首页发现Icon数据传输对象
 *
 * @property homepageMode homepageMode
 * @property iconUrl 图标url
 * @property id id
 * @property name 名称
 * @property skinSupport 是否支持皮肤
 * @property url 链接
 * @constructor Create empty Home icon info dto
 */
@Serializable
data class HomeIconInfoDto(
    @SerialName("homepageMode")
    val homepageMode: String = "",
    @SerialName("iconUrl")
    val iconUrl: String = "",
    @SerialName("id")
    val id: Long = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("skinSupport")
    val skinSupport: Boolean = false,
    @SerialName("url")
    val url: String = ""
)

/**
 * 首页推荐歌单数据传输对象
 *
 * @property copywriter 文案
 * @property highQuality 是否高质量
 * @property id id
 * @property name 名称
 * @property picUrl 图片url
 * @property playCount 播放次数
 * @property type 类型
 * @constructor Create empty Home personalized info dto
 */
@Serializable
data class HomePersonalizedInfoDto(
    @SerialName("copywriter")
    val copywriter: String = "",
    @SerialName("highQuality")
    val highQuality: Boolean = false,
    @SerialName("id")
    val id: Long = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("picUrl")
    val picUrl: String = "",
    @SerialName("playCount")
    val playCount: Int = 0,
    @SerialName("type")
    val type: Int = 0
)

/**
 * 推荐歌单歌曲数据传输对象
 *
 * @property album 专辑
 * @property artist 歌手
 * @property id id
 * @property name 名称
 * @property mvId mvId
 * @constructor Create empty Home personalized song info dto
 */
@Serializable
data class HomePersonalizedSongInfoDto(
    @SerialName("al")
    val album: Album = Album(),
    @SerialName("ar")
    val artist: List<Artist> = listOf(),
    @SerialName("id")
    val id: Long = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("mv")
    val mvId: Long = 0,
) {

    /**
     * 专辑
     *
     * @property id id
     * @property name 专辑名称
     * @property picUrl 专辑图片url
     * @constructor Create empty Album
     */
    @Serializable
    data class Album(
        @SerialName("id")
        val id: Long = 0,
        @SerialName("name")
        val name: String = "",
        @SerialName("picUrl")
        val picUrl: String = ""
    )

    /**
     * 歌手
     *
     * @property id id
     * @property name 歌手名称
     * @constructor Create empty Artist
     */
    @Serializable
    data class Artist(
        @SerialName("id")
        val id: Long = 0,
        @SerialName("name")
        val name: String = ""
    )
}

/**
 * 首页排行榜数据传输对象
 *
 * @property coverImgUrl 封面图片url
 * @property description 描述
 * @property id id
 * @property name 名称
 * @property playCount 播放次数
 * @property updateFrequency 更新频率
 * @constructor Create empty Home top info dto
 */
@Serializable
data class HomeTopInfoDto(
    @SerialName("coverImgUrl")
    val coverImgUrl: String = "",
    @SerialName("description")
    val description: String = "",
    @SerialName("id")
    val id: Long = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("playCount")
    val playCount: Long = 0,
    @SerialName("updateFrequency")
    val updateFrequency: String = ""
)

/**
 * 首页最新专辑数据传输对象
 *
 * @property alias 别名
 * @property artist 歌手
 * @property artists 歌手列表
 * @property blurPicUrl 模糊图片url
 * @property briefDesc 简介
 * @property commentThreadId 评论id
 * @property company 公司
 * @property companyId 公司id
 * @property copyrightId 版权ID
 * @property description 描述
 * @property id id
 * @property name 名称
 * @property onSale 是否上架
 * @property paid 是否付费
 * @property pic 图片
 * @property picId 图片id
 * @property picIdStr 图片id字符串
 * @property picUrl 图片url
 * @property publishTime 发布时间
 * @property size 大小
 * @property status 状态
 * @property tags 标签
 * @property type 类型
 * @constructor Create empty Home newest album dto
 */
@Serializable
data class HomeNewestAlbumInfoDto(
    @SerialName("alias")
    val alias: List<String> = listOf(),
    @SerialName("artist")
    val artist: Artist = Artist(),
    @SerialName("artists")
    val artists: List<Artist> = listOf(),
    @SerialName("blurPicUrl")
    val blurPicUrl: String = "",
    @SerialName("briefDesc")
    val briefDesc: String = "",
    @SerialName("commentThreadId")
    val commentThreadId: String = "",
    @SerialName("company")
    val company: String = "",
    @SerialName("companyId")
    val companyId: Long = 0,
    @SerialName("copyrightId")
    val copyrightId: Long = 0,
    @SerialName("description")
    val description: String = "",
    @SerialName("id")
    val id: Long = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("onSale")
    val onSale: Boolean = false,
    @SerialName("paid")
    val paid: Boolean = false,
    @SerialName("pic")
    val pic: Long = 0,
    @SerialName("picId")
    val picId: Long = 0,
    @SerialName("picId_str")
    val picIdStr: String = "",
    @SerialName("picUrl")
    val picUrl: String = "",
    @SerialName("publishTime")
    val publishTime: Long = 0,
    @SerialName("size")
    val size: Int = 0,
    @SerialName("status")
    val status: Int = 0,
    @SerialName("tags")
    val tags: String = "",
    @SerialName("type")
    val type: String = ""
) {

    /**
     * 歌手
     *
     * @property albumSize 专辑数量
     * @property alias 别名
     * @property briefDesc 简介
     * @property id id
     * @property img1v1Id 图片1v1Id
     * @property img1v1IdStr 图片1v1Id字符串
     * @property img1v1Url 图片1v1Url
     * @property musicSize 音乐数量
     * @property name 名称
     * @property picId 图片id
     * @property picIdStr 图片id字符串
     * @property picUrl 图片url
     * @property topicPerson 话题人
     * @property trans 翻译
     * @property transNames 翻译名称
     * @constructor Create empty Artist
     */
    @Serializable
    data class Artist(
        @SerialName("albumSize")
        val albumSize: Int = 0,
        @SerialName("alias")
        val alias: List<String> = listOf(),
        @SerialName("briefDesc")
        val briefDesc: String = "",
        @SerialName("id")
        val id: Long = 0,
        @SerialName("img1v1Id")
        val img1v1Id: Long = 0,
        @SerialName("img1v1Id_str")
        val img1v1IdStr: String = "",
        @SerialName("img1v1Url")
        val img1v1Url: String = "",
        @SerialName("musicSize")
        val musicSize: Int = 0,
        @SerialName("name")
        val name: String = "",
        @SerialName("picId")
        val picId: Long = 0,
        @SerialName("picId_str")
        val picIdStr: String = "",
        @SerialName("picUrl")
        val picUrl: String = "",
        @SerialName("topicPerson")
        val topicPerson: Int = 0,
        @SerialName("trans")
        val trans: String = "",
        @SerialName("transNames")
        val transNames: List<String> = listOf()
    )
}

/**
 * 首页最新音乐数据传输对象
 *
 * @property br 音质
 * @property canExtend 是否可扩展
 * @property code 状态码
 * @property encodeType 编码类型
 * @property expi 过期
 * @property fee 费用
 * @property flag 标志
 * @property gain 自增
 * @property id id
 * @property level 等级
 * @property md5 MD5
 * @property payed 是否付费
 * @property rightSource 版权来源
 * @property size 大小
 * @property time 时间
 * @property type 类型
 * @property url url
 * @property urlSource url来源
 * @constructor Create empty Song detail dto
 */
@Serializable
data class SongDetailDto(
    @SerialName("br")
    val br: Int = 0,
    @SerialName("canExtend")
    val canExtend: Boolean = false,
    @SerialName("code")
    val code: Int = 0,
    @SerialName("encodeType")
    val encodeType: String = "",
    @SerialName("expi")
    val expi: Int = 0,
    @SerialName("fee")
    val fee: Int = 0,
    @SerialName("flag")
    val flag: Int = 0,
    @SerialName("gain")
    val gain: Double = 0.0,
    @SerialName("id")
    val id: Long = 0,
    @SerialName("level")
    val level: String = "",
    @SerialName("md5")
    val md5: String = "",
    @SerialName("payed")
    val payed: Int = 0,
    @SerialName("rightSource")
    val rightSource: Int = 0,
    @SerialName("size")
    val size: Int = 0,
    @SerialName("time")
    val time: Long = 0,
    @SerialName("type")
    val type: String = "",
    @SerialName("url")
    val url: String = "",
    @SerialName("urlSource")
    val urlSource: Int = 0
)

/**
 * 专辑歌曲列表数据
 *
 * @property album 专辑
 * @property artist 歌手
 * @property id id
 * @property name 名称
 * @constructor Create empty Album songs dto
 */
@Serializable
data class AlbumSongsDto(
    @SerialName("al")
    val album: HomePersonalizedSongInfoDto.Album = HomePersonalizedSongInfoDto.Album(),
    @SerialName("ar")
    val artist: List<HomePersonalizedSongInfoDto.Artist> = listOf(),
    @SerialName("id")
    val id: Long = 0,
    @SerialName("name")
    val name: String = "",
)