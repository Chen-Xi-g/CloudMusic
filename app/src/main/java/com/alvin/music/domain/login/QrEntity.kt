package com.alvin.music.domain.login

/**
 * @Description ：   UI层使用的二维码数据
 * @Date        ：   2023/6/26
 * @author      ：   高国峰
 *
 * @param key 二维码key
 * @param qrUrl 二维码图片地址
 */
data class QrEntity(
    val key: String = "",
    val qrUrl: String = ""
)

/**
 * @Description ：   二维码扫描状态
 * @Date        ：   2023/6/26
 * @Author      ：   高国峰
 *
 * @param status 800：二维码已过期 801：请使用网易云音乐app扫码授权登录 802：已扫码，等待确认 803：授权登录成功
 * @param message 二维码扫描信息
 * @param nickname 昵称
 * @param avatarUrl 头像
 */
data class QrStatusEntity(
    val status: Int = 0,
    val message: String = "",
    val nickname: String = "",
    val avatarUrl: String = ""
)