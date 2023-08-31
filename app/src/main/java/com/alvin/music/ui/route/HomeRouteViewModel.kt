package com.alvin.music.ui.route

import androidx.lifecycle.ViewModel
import com.alvin.music.domain.util.CustomPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Description ：   首页路由ViewModel
 * @Date        ：   2023/7/27
 * @author      ：   高国峰
 *
 * @param player 播放器
 */
@HiltViewModel
class HomeRouteViewModel @Inject constructor(private val player: CustomPlayer) : ViewModel(){

}