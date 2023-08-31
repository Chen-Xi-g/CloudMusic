package com.alvin.music.data.remote

import com.alvin.music.data.dto.BaseDto
import com.alvin.music.data.dto.login.QrCreateDto
import com.alvin.music.data.dto.login.QrKeyDto
import com.alvin.music.data.dto.login.QrStatusDto
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

/**
 * @Description ：   二维码接口
 * @Date        ：   2023/6/26
 * @author      ：   高国峰
 */
interface QrApi {

    /**
     * 生成二维码key
     */
    @GET("login/qr/key")
    suspend fun generateQrKey(@Query("timeStamp") timeStamp: Long = Date().time): QrKeyDto

    /**
     * 创建二维码
     *
     * @param key 二维码key
     */
    @GET("login/qr/create")
    suspend fun createQrCode(@Query("key") key: String,@Query("timeStamp") timeStamp: Long = Date().time): QrCreateDto

    /**
     * 校验二维码扫描状态
     *
     * @param key 二维码key
     */
    @GET("login/qr/check")
    suspend fun checkQrCode(@Query("key") key: String,@Query("timeStamp") timeStamp: Long = Date().time): QrStatusDto

    /**
     * 获取登录状态
     */
    @GET("login/status")
    suspend fun getLoginStatus(@Query("timeStamp") timeStamp: Long = Date().time)
}