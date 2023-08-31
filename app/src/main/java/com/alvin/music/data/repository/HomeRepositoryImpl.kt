package com.alvin.music.data.repository

import com.alvin.music.data.mappers.toHomeAlbumEntity
import com.alvin.music.data.mappers.toHomeBannerList
import com.alvin.music.data.mappers.toHomeIconList
import com.alvin.music.data.mappers.toHomePersonalizedList
import com.alvin.music.data.mappers.toHomePersonalizedSongList
import com.alvin.music.data.mappers.toHomeSongEntity
import com.alvin.music.data.mappers.toHomeAlbumSongs
import com.alvin.music.data.mappers.toHomeSongs
import com.alvin.music.data.mappers.toHomeTopEntity
import com.alvin.music.data.remote.HomeApi
import com.alvin.music.domain.home.HomeAlbumEntity
import com.alvin.music.domain.home.HomeBannerEntity
import com.alvin.music.domain.home.HomeIconEntity
import com.alvin.music.domain.home.HomePersonalizedEntity
import com.alvin.music.domain.home.HomePersonalizedSongEntity
import com.alvin.music.domain.home.HomeSongEntity
import com.alvin.music.domain.home.HomeTopEntity
import com.alvin.music.domain.repository.HomeRepository
import com.alvin.music.domain.util.Resource
import javax.inject.Inject

/**
 * @Description ：   首页数据仓库实现类
 * @Date        ：   2023/7/2
 * @author      ：   高国峰
 */
class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi
) : HomeRepository {
    override suspend fun getBanner(): Resource<List<HomeBannerEntity>> {
        return try {
            val response = api.getBanner()
            if (response.isSuccess()) {
                Resource.Success(response.banners.toHomeBannerList())
            } else {
                Resource.Error("未知错误")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "未知错误")
        }
    }

    override suspend fun getDragonBall(): Resource<List<HomeIconEntity>> {
        return try {
            val response = api.getDragonBall()
            if (response.isSuccess()) {
                Resource.Success(response.data.toHomeIconList())
            } else {
                Resource.Error(response.message ?: "未知错误")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "未知错误")
        }
    }

    override suspend fun getPersonalized(limit: Int): Resource<List<HomePersonalizedEntity>> {
        return try {
            val response = api.getRecommendPlayList(limit = limit)
            if (response.isSuccess()) {
                Resource.Success(response.result.toHomePersonalizedList())
            } else {
                Resource.Error(response.message ?: "未知错误")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "未知错误")
        }
    }

    override suspend fun getPersonalizedSongs(
        id: Long,
        limit: Int
    ): Resource<List<HomePersonalizedSongEntity>> {
        return try {
            val response = api.getPlayListDetail(id, limit)
            if (response.isSuccess()) {
                Resource.Success(response.songs.toHomePersonalizedSongList())
            } else {
                Resource.Error(response.message ?: "未知错误")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "未知错误")
        }
    }

    override suspend fun getTopList(): Resource<List<HomeTopEntity>> {
        return try {
            val response = api.getTopList()
            if (response.isSuccess()) {
                val newList = mutableListOf<HomeTopEntity>()
                if (response.list.size > 8) {
                    response.list.subList(0, 8)
                } else {
                    response.list
                }.forEach {
                    val detailResponse = api.getPlayListDetail(it.id, 3)
                    newList.add(it.toHomeTopEntity(detailResponse.songs))
                }
                Resource.Success(newList)
            } else {
                Resource.Error(response.message ?: "未知错误")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "未知错误")
        }
    }

    override suspend fun getNewestAlbum(): Resource<List<HomeAlbumEntity>> {
        return try {
            val response = api.getNewestAlbum()
            if (response.isSuccess()) {
                Resource.Success(response.albums.toHomeAlbumEntity())
            } else {
                Resource.Error(response.message ?: "未知错误")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "未知错误")
        }
    }

    override suspend fun getSongUrl(list: List<HomeSongEntity>): Resource<List<HomeSongEntity>> {
        return try {
            val response = api.getSongUrl(list.map { it.id }.joinToString(","))
            if (response.isSuccess()) {
                Resource.Success(response.data.toHomeSongEntity(list))
            } else {
                Resource.Error(response.message ?: "未知错误")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "未知错误")
        }
    }

    override suspend fun getSongListDetail(id: Long): Resource<List<HomeSongEntity>> {
        return try {
            val response = api.getPlayListDetail(id, limit = Int.MAX_VALUE)
            val songIds = response.songs.map { it.id }.joinToString(",")
            val songUrlResponse = api.getSongUrl(songIds)
            if (response.isSuccess()) {
                Resource.Success(songUrlResponse.data.toHomeSongEntity(response.songs.toHomeSongs()))
            } else {
                Resource.Error(response.message ?: "未知错误")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "未知错误")
        }
    }

    override suspend fun getAlbumSongList(id: Long): Resource<List<HomeSongEntity>> {
        return try {
            val response = api.getAlbumDetail(id)
            val songIds = response.songs.map { it.id }.joinToString(",")
            val songUrlResponse = api.getSongUrl(songIds)
            if (response.isSuccess()) {
                Resource.Success(songUrlResponse.data.toHomeSongEntity(response.songs.toHomeAlbumSongs()))
            } else {
                Resource.Error(response.message ?: "未知错误")
            }
        }catch (e: Exception){
            Resource.Error(e.message ?: "未知错误")
        }
    }
}