package com.alvin.music.domain.repository

import com.alvin.music.domain.login.QrEntity
import com.alvin.music.domain.login.QrStatusEntity
import com.alvin.music.domain.util.Resource

/**
 * @Description ：   二维码数据仓库
 * @Date        ：   2023/6/26
 * @author      ：   高国峰
 */
interface QrRepository {

    /**
     * 获取二维码数据
     *
     * @return 二维码数据
     */
    suspend fun getQrData(): Resource<QrEntity>

    /**
     * 检查二维码扫描状态
     *
     * @param key 二维码key
     * @return cookie
     */
    suspend fun checkQrCode(key: String): Resource<QrStatusEntity>

    /**
     * 获取登录状态
     *
     * @return 登录状态
     */
    suspend fun getLoginStatus(): Resource<Any>
}