package com.alvin.music.domain.repository

import androidx.media3.common.MediaItem

/**
 * @Description ：   播放辅助类
 * @Date        ：   2023/7/27
 * @author      ：   高国峰
 */
interface PlayerManager {

    /**
     * 初始化播放器
     *
     * @param trackList 播放列表
     */
    fun initPlayer(trackList: List<MediaItem>)

    /**
     * 设置轨迹
     *
     * @param index 轨迹索引
     * @param isTrackPlay 是否播放
     */
    fun setUpTrack(index: Int, isTrackPlay: Boolean)

    /**
     * 播放暂停
     */
    fun playPause()

    /**
     * 释放播放器
     */
    fun releasePlayer()

    /**
     * 从指定位置播放
     *
     * @param position 指定位置
     */
    fun seekToPosition(position: Long)

    /**
     * 获取当前轨迹时长
     */
    fun getTrackDuration(): Long

    /**
     * 获取当前播放索引
     */
    fun getCurrentTrackIndex(): Int

    /**
     * 设置播放模式
     *
     * @param repeatMode 播放模式，顺序播放[androidx.media3.common.Player.REPEAT_MODE_OFF]、单曲循环[androidx.media3.common.Player.REPEAT_MODE_ONE]、列表重复[androidx.media3.common.Player.REPEAT_MODE_ALL]
     */
    fun setRepeatMode(repeatMode: Int)

    /**
     * 删除歌曲
     *
     * @param index 索引
     */
    fun removeTrack(index: Int)
}

enum class PlayerStates {
    /**
     * 播放器空闲状态，准备好播放。
     */
    STATE_IDLE,

    /**
     * 播放器准备开始播放的状态。
     */
    STATE_READY,

    /**
     * 播放器缓冲内容的状态。
     */
    STATE_BUFFERING,

    /**
     * 播放器遇到错误时的状态。
     */
    STATE_ERROR,

    /**
     * 播放结束的状态。
     */
    STATE_END,

    /**
     * 播放器正在播放内容的状态。
     */
    STATE_PLAYING,

    /**
     * 播放器暂停播放的状态。
     */
    STATE_PAUSE,

    /**
     * 播放器移动到下一个曲目时的状态。
     */
    STATE_NEXT_TRACK
}