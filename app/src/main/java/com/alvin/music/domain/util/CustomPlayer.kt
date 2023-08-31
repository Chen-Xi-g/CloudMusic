package com.alvin.music.domain.util

import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.alvin.music.domain.repository.PlayerManager
import com.alvin.music.domain.repository.PlayerStates
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * @Description ：   自定义播放器
 * @Date        ：   2023/7/27
 * @author      ：   高国峰
 */
class CustomPlayer @Inject constructor(private val player: ExoPlayer): Player.Listener,
    PlayerManager {

    val playerState = MutableStateFlow(PlayerStates.STATE_IDLE)

    val currentPlaybackPosition: Long
        get() = if (player.currentPosition > 0) player.currentPosition else 0L

    private val _currentTrackDuration: Long
        get() = if (player.duration > 0) player.duration else 0L

    override fun initPlayer(trackList: List<MediaItem>) {
        player.removeListener(this)
        player.addListener(this)
        player.setMediaItems(trackList)
        player.prepare()
    }

    override fun setUpTrack(index: Int, isTrackPlay: Boolean) {
        if (player.playbackState == Player.STATE_IDLE) player.prepare()
        player.seekTo(index, 0)
        if (isTrackPlay) player.playWhenReady = true
    }

    override fun playPause() {
        if (player.playbackState == Player.STATE_IDLE) player.prepare()
        player.playWhenReady = !player.playWhenReady
    }

    override fun releasePlayer() {
        player.removeListener(this)
        player.release()
    }

    override fun seekToPosition(position: Long) {
        player.seekTo(position)
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        playerState.tryEmit(PlayerStates.STATE_ERROR)
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        if (player.playbackState == Player.STATE_READY){
            if (playWhenReady){
                playerState.tryEmit(PlayerStates.STATE_PLAYING)
            }else{
                playerState.tryEmit(PlayerStates.STATE_PAUSE)
            }
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO){
            playerState.tryEmit(PlayerStates.STATE_NEXT_TRACK)
            playerState.tryEmit(PlayerStates.STATE_PLAYING)
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when(playbackState){
            Player.STATE_IDLE -> playerState.tryEmit(PlayerStates.STATE_IDLE)
            Player.STATE_BUFFERING -> playerState.tryEmit(PlayerStates.STATE_BUFFERING)
            Player.STATE_READY -> {
                playerState.tryEmit(PlayerStates.STATE_READY)
                if (player.playWhenReady) {
                    playerState.tryEmit(PlayerStates.STATE_PLAYING)
                } else {
                    playerState.tryEmit(PlayerStates.STATE_PAUSE)
                }
            }
            Player.STATE_ENDED -> playerState.tryEmit(PlayerStates.STATE_END)
        }
    }

    override fun getTrackDuration(): Long {
        return _currentTrackDuration
    }

    override fun getCurrentTrackIndex(): Int {
        return player.currentMediaItemIndex
    }

    override fun setRepeatMode(repeatMode: Int) {
        if (repeatMode == 3){
            player.shuffleModeEnabled = true
        }else{
            player.shuffleModeEnabled = false
            player.repeatMode = repeatMode
        }
    }

    override fun removeTrack(index: Int) {
        player.removeMediaItem(index)
    }

}
