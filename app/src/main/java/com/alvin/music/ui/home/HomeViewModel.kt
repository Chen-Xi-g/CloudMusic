package com.alvin.music.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvin.music.data.dto.home.HomeTopListDto
import com.alvin.music.domain.home.HomeAlbumEntity
import com.alvin.music.domain.home.HomeBannerEntity
import com.alvin.music.domain.home.HomeIconEntity
import com.alvin.music.domain.home.HomePersonalizedEntity
import com.alvin.music.domain.home.HomePersonalizedSongEntity
import com.alvin.music.domain.home.HomeTopEntity
import com.alvin.music.domain.repository.HomeRepository
import com.alvin.music.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Description ：   描述
 * @Date        ：   2023/7/2
 * @author      ：   高国峰
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
): ViewModel() {

    /**
     * 首页Banner数据
     */
    var state by mutableStateOf(HomeState())
        private set

    init {
        loadHomeData()
    }

    /**
     * 加载首页数据
     */
    fun loadHomeData() {
        state = state.copy(isLoading = true)
        loadBanner()
        loadDragonBall()
        loadPersonalized()
        loadMorePersonalized()
        loadTopList()
        loadNewestAlbum()
    }

    /**
     * 刷新首页推荐歌单歌曲
     */
    fun refreshPersonalizedSongs() {
        loadMorePersonalized()
    }

    /**
     * 加载首页Banner数据
     */
    private fun loadBanner() {
        viewModelScope.launch {
            when(val result = repository.getBanner()){
                is Resource.Success ->{
                    state = state.copy(
                        banners = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error ->{
                    state = state.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * 加载首页Icon数据
     */
    private fun loadDragonBall() {
        viewModelScope.launch {
            when(val result = repository.getDragonBall()){
                is Resource.Success ->{
                    state = state.copy(
                        icons = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error ->{
                    state = state.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * 加载首页推荐歌单数据
     *
     * @param limit 加载数量
     */
    private fun loadPersonalized(limit: Int = 10){
        viewModelScope.launch {
            when(val result = repository.getPersonalized(limit)){
                is Resource.Success ->{
                    state = state.copy(
                        personalized = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error ->{
                    state = state.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * 加载推荐歌单数据
     *
     * @param limit 加载数量
     */
    private fun loadMorePersonalized(limit: Int = 1) {
        viewModelScope.launch {
            state = state.copy(isPersonalizedSingRefresh = true)
            when (val result = repository.getPersonalized(limit)) {
                is Resource.Success -> {
                    if (result.data.isNullOrEmpty()){
                        return@launch
                    }
                    loadPersonalizedSong(result.data[0], result.data[0].id)
                }

                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        isPersonalizedSingRefresh = false
                    )
                }
            }
        }
    }

    /**
     * 获取歌单中的15首歌曲
     *
     * @param id 歌单id
     */
    private fun loadPersonalizedSong(personalizedSing: HomePersonalizedEntity, id: Long) {
        viewModelScope.launch {
            when (val result = repository.getPersonalizedSongs(id, 15)) {
                is Resource.Success -> {
                    state = state.copy(
                        personalizedSing = personalizedSing,
                        personalizedSingSong = result.data,
                        isLoading = false,
                        error = null,
                        isPersonalizedSingRefresh = false
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        isPersonalizedSingRefresh = false
                    )
                }
            }
        }
    }

    /**
     * 获取榜单数据
     */
    private fun loadTopList() {
        viewModelScope.launch {
            when (val result = repository.getTopList()) {
                is Resource.Success -> {
                    state = state.copy(
                        topList = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * 获取最新专辑数据
     */
    private fun loadNewestAlbum(){
        viewModelScope.launch {
            when(val result = repository.getNewestAlbum()){
                is Resource.Success ->{
                    state = state.copy(
                        newestAlbums = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error ->{
                    state = state.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}

/**
 * 首页状态管理
 *
 * @param isLoading 是否正在加载
 * @param isPersonalizedSingRefresh 是否正在刷新推荐歌单
 * @param error 错误信息
 * @param banners 首页Banner数据
 * @param icons 首页Icon数据
 * @param personalized 首页推荐歌单数据
 * @param personalizedSing 首页推荐的一条歌单数据
 * @param personalizedSingSong 首页推荐的一条歌单的歌曲数据
 * @param newestAlbums 最新专辑数据
 */
data class HomeState(
    val isLoading: Boolean = false,
    val isPersonalizedSingRefresh: Boolean= false,
    val error: String? = null,
    val banners: List<HomeBannerEntity> = emptyList(),
    val icons: List<HomeIconEntity> = emptyList(),
    val personalized: List<HomePersonalizedEntity> = emptyList(),
    val personalizedSing: HomePersonalizedEntity? = null,
    val personalizedSingSong: List<HomePersonalizedSongEntity>? = null,
    val topList: List<HomeTopEntity>? = null,
    val newestAlbums: List<HomeAlbumEntity>? = null
)