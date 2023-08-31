package com.alvin.music.ui.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.alvin.music.data.mappers.toMediaInfo
import com.alvin.music.domain.home.HomeSongEntity
import com.alvin.music.domain.home.HomeSongRequestEntity
import com.alvin.music.domain.repository.HomeRepository
import com.alvin.music.domain.repository.PlayerStates
import com.alvin.music.domain.util.BaseKV
import com.alvin.music.domain.util.CustomPlayer
import com.alvin.music.domain.util.Resource
import com.alvin.music.domain.util.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

/**
 * @Description ：   播放ViewModel
 * @Date        ：   2023/7/30
 * @author      ：   高国峰
 */
@HiltViewModel
class PlayViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val player: CustomPlayer
) : ViewModel() {

    /**
     * 播放列表状态
     */
    var state by mutableStateOf(PlayState())
        private set

    /**
     * 用户操作
     */
    private val uiAction: MutableSharedFlow<PlayAction> = MutableSharedFlow()

    /**
     * 播放进度
     */
    private val _playProgress: MutableStateFlow<Long> = MutableStateFlow(0L)

    val playProgress: StateFlow<Long> = _playProgress

    private var progressJob: Job? = null

    init {
        viewModelScope.launch {
            uiAction.collect{ action ->
                handleAction(action)
            }
        }
        viewModelScope.launch {
            player.playerState.collect { state ->
                handlePlayerState(state)
            }
        }
    }

    /**
     * 处理用户操作
     *
     * @param action 用户操作
     */
    private fun handleAction(action: PlayAction){
        when(action){
            is PlayAction.Init -> onInit(action.list, action.id)
            is PlayAction.PlayPause -> onPlayPauseClick()
            is PlayAction.Previous -> onPreviousClick()
            is PlayAction.Next -> onNextClick()
            is PlayAction.SeekTo -> onSeekTo(action.position)
            is PlayAction.SwitchSong -> onSwitchSong(action.songEntity)
            is PlayAction.ChangeMode -> onChangeRepeatMode(action.mode)
            is PlayAction.RemoveSong -> onRemoveSong(action.songEntity)
        }
    }

    /**
     * 处理播放器状态
     *
     * @param state 播放器状态
     */
    private fun handlePlayerState(state: PlayerStates) {
        when (state) {
            PlayerStates.STATE_IDLE -> {
            }

            PlayerStates.STATE_READY ->{
            }

            PlayerStates.STATE_BUFFERING -> {
            }

            PlayerStates.STATE_PLAYING ->{
                if (this.state.list.isNotEmpty()){
                    this.state = this.state.copy(isPlaying = true, currentSong = this.state.list[player.getCurrentTrackIndex()])
                }
            }

            PlayerStates.STATE_PAUSE ->{
                this.state = this.state.copy(isPlaying = false)
            }

            PlayerStates.STATE_NEXT_TRACK ->{
                val currentSong = this.state.list[player.getCurrentTrackIndex()]
                currentSong.time = player.getTrackDuration()
                this.state = this.state.copy(isPlaying = true, currentSong = currentSong)
            }

            PlayerStates.STATE_END -> {
                this.state = this.state.copy(isPlaying = false, currentSong = null)
            }

            PlayerStates.STATE_ERROR -> {
                this.state = this.state.copy(error =  "播放发生错误")
            }
        }
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            do {
                _playProgress.emit(player.currentPlaybackPosition)
                delay(1.seconds)
            }while (state == PlayerStates.STATE_PLAYING && isActive)
        }
    }

    /**
     * 获取歌曲播放地址
     *
     * @param entity HomeSongRequestEntity
     */
    fun getSongList(entity: HomeSongRequestEntity) {
        when(entity.type){
            0 ->{
                // 歌曲
                getSongList(entity.list, entity.songId)
            }
            1 ->{
                // 歌单
                getSongList(entity.playlistId, entity.songId)
            }
            2 ->{
                // 专辑
                getAlbumList(entity.playlistId)
            }
        }
    }

    /**
     * 获取歌曲列表
     *
     * @param list 需要播放的歌曲列表
     * @param id 当前需要播放的歌曲ID
     *
     */
    private fun getSongList(list: List<HomeSongEntity>, id: Long = 0) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when(val response = repository.getSongUrl(list)){
                is Resource.Success ->{
                    state = state.copy(list = response.data?: listOf(), isLoading = false)
                    if (!response.data.isNullOrEmpty()){
                        sendAction(PlayAction.Init(response.data, id))
                        sendAction(PlayAction.ChangeMode(BaseKV.Playing.playMode))
                    }
                }
                is Resource.Error ->{
                    state = state.copy(error = response.message, isLoading = false)
                }
            }
        }
    }

    /**
     * 根据歌单ID获取歌曲列表
     *
     * @param id 歌单ID
     * @param songId 当前需要播放的歌曲ID
     */
    private fun getSongList(id: Long, songId: Long = 0) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val response = repository.getSongListDetail(id)) {
                is Resource.Success -> {
                    state = state.copy(list = response.data ?: listOf(), isLoading = false)
                    if (!response.data.isNullOrEmpty()){
                        sendAction(PlayAction.Init(response.data, songId))
                        sendAction(PlayAction.ChangeMode(BaseKV.Playing.playMode))
                    }
                }

                is Resource.Error -> {
                    state = state.copy(error = response.message, isLoading = false)
                }
            }
        }
    }

    /**
     * 根据专辑ID获取歌曲列表
     *
     * @param id 专辑ID
     */
    private fun getAlbumList(id: Long) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val response = repository.getAlbumSongList(id)) {
                is Resource.Success -> {
                    state = state.copy(list = response.data ?: listOf(), isLoading = false)
                    if (!response.data.isNullOrEmpty()){
                        sendAction(PlayAction.Init(response.data))
                        sendAction(PlayAction.ChangeMode(BaseKV.Playing.playMode))
                    }
                }

                is Resource.Error -> {
                    state = state.copy(error = response.message, isLoading = false)
                }
            }
        }
    }

    /**
     * 发送用户操作
     *
     * @param action 用户操作
     */
    fun sendAction(action: PlayAction){
        viewModelScope.launch {
            uiAction.emit(action)
        }
    }

    /**
     * 初始化
     */
    private fun onInit(list: List<HomeSongEntity>, id: Long) {
        player.initPlayer(
            list.toMediaInfo()
        )
        if (list.isNotEmpty()){
            sendAction(PlayAction.SwitchSong(list.find { it.id == id } ?: list[0]))
        }
    }

    /**
     * 播放或暂停
     */
    private fun onPlayPauseClick() {
        player.playPause()
    }

    /**
     * 上一首
     */
    private fun onPreviousClick() {
        val index = getCurrentIndex() - 1
        if (index < 0) {
            "已经是第一首了".toast()
            return
        }
        player.setUpTrack(index, true)
    }

    /**
     * 下一首
     */
    private fun onNextClick() {
        val index = getCurrentIndex() + 1
        if (index >= state.list.size) {
            "已经是最后一首了".toast()
            return
        }
        player.setUpTrack(index, true)
    }

    /**
     * 拖动进度条
     *
     * @param position 拖动的位置
     */
    private fun onSeekTo(position: Long) {
        player.seekToPosition(position)
    }

    /**
     * 切换歌曲
     *
     * @param songEntity 歌曲实体
     */
    private fun onSwitchSong(songEntity: HomeSongEntity) {
        state = state.copy(currentSong = songEntity)
        player.setUpTrack(getCurrentIndex(), true)
    }

    /**
     * 修改播放模式
     */
    private fun onChangeRepeatMode(mode: Int) {
        BaseKV.Playing.playMode = mode
        state = state.copy(repeatMode = mode)
        player.setRepeatMode(mode)
    }

    /**
     * 删除歌曲
     */
    private fun onRemoveSong(songEntity: HomeSongEntity) {
        if (state.currentSong == songEntity){
            "当前歌曲正在播放，无法删除".toast()
            return
        }
        val list = state.list.toMutableList()
        val index = list.indexOf(songEntity)
        list.remove(songEntity)
        state = state.copy(list = list)
        player.removeTrack(index)
    }

    /**
     * 当前播放的索引
     */
    private fun getCurrentIndex(): Int {
        return state.list.indexOf(state.currentSong)
    }

}

/**
 * 播放状态
 *
 * @property isLoading 是否加载中
 * @property list 歌曲列表
 * @property error 错误信息
 * @property currentSong 当前播放的歌曲
 * @property repeatMode 播放模式
 * @property isPlaying 是否正在播放
 * @constructor Create empty Play state
 */
data class PlayState(
    val isLoading: Boolean = false,
    val currentSong: HomeSongEntity? = null,
    val isPlaying: Boolean = false,
    val repeatMode: Int = Player.REPEAT_MODE_ALL,
    val list: List<HomeSongEntity> = listOf(),
    val error: String? = null
)

/**
 * 用户操作
 */
sealed class PlayAction {
    /**
     * 初始化
     *
     * @property list 歌曲列表
     * @property id 当前需要播放的歌曲ID
     * @constructor Create empty Init
     */
    data class Init(val list: List<HomeSongEntity>, val id: Long = 0) : PlayAction()

    /**
     * 播放或暂停
     *
     * @constructor Create empty Play pause
     */
    object PlayPause : PlayAction()

    /**
     * 上一首
     *
     * @constructor Create empty Previous
     */
    object Previous : PlayAction()

    /**
     * 下一首
     *
     * @constructor Create empty Next
     */
    object Next : PlayAction()

    /**
     * 拖动进度条
     *
     * @property position 拖动的位置
     * @constructor Create empty Seek to
     */
    data class SeekTo(val position: Long) : PlayAction()

    /**
     * 切换歌曲
     *
     * @property songEntity 歌曲实体
     * @constructor Create empty Switch song
     */
    data class SwitchSong(val songEntity: HomeSongEntity) : PlayAction()

    /**
     * 修改播放模式
     *
     * @property mode 播放模式
     */
    data class ChangeMode(val mode: Int) : PlayAction()

    /**
     * 删除歌曲
     *
     * @property songEntity 歌曲实体
     */
    data class RemoveSong(val songEntity: HomeSongEntity) : PlayAction()
}