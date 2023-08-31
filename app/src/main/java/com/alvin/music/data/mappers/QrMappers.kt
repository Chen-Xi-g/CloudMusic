package com.alvin.music.data.mappers

import com.alvin.music.data.dto.BaseDto
import com.alvin.music.data.dto.login.QrCreateDto
import com.alvin.music.data.dto.login.QrStatusDto
import com.alvin.music.domain.login.QrEntity
import com.alvin.music.domain.login.QrStatusEntity

/**
 * @Description ：   二维码创建数据传输对象转换为UI层使用的二维码数据
 *
 * @param key 二维码key
 *
 * @return UI层使用的二维码数据 [QrEntity]
 */
fun QrCreateDto.toQrEntity(key: String): QrEntity {
    if (!isSuccess()) {
        throw Exception("二维码创建失败")
    }
    return QrEntity(key, data?.qrUrl ?: "")
}

/**
 * @Description ：   二维码扫描状态数据传输对象转换为二维码扫描状态实体
 *
 * @return 二维码扫描状态实体 [QrStatusEntity]
 */
fun QrStatusDto.toQrStatusEntity(): QrStatusEntity {
    return QrStatusEntity(
        code,
        when (code) {
            800 -> "二维码已过期"
            801 -> "请使用网易云音乐app扫码授权登录"
            802 -> "已扫码，等待确认"
            803 -> "授权登录成功"
            else -> message ?: "未知错误"
        },
        nickname,
        avatarUrl
    )
}