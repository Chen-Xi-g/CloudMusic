package com.alvin.music.data.dto.login

import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.alvin.music.data.dto.BaseDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @Description ：   二维码数据传输对象
 *
 * @property data 二维码数据 [QrCreateInfoDto]
 * @constructor Create empty Qr create dto
 */
@Serializable
data class QrCreateDto(val data: QrCreateInfoDto? = null) : BaseDto()

/**
 * @Description ：   二维码key数据传输对象
 *
 * @property data 二维码key数据 [QrKeyInfoDto]
 * @constructor Create empty Qr key data dto
 */
@Serializable
data class QrKeyDto(val data: QrKeyInfoDto? = null) : BaseDto()

/**
 * @Description ：   二维码状态数据传输对象
 *
 * @property nickname 昵称
 * @property avatarUrl 头像
 * @property cookie cookie
 * @constructor Create empty Qr status dto
 */
@Serializable
data class QrStatusDto(val nickname: String = "", val avatarUrl: String = "", val cookie: String = "") : BaseDto()

/**
 * @Description ：   二维码创建数据传输对象
 *
 * @property qrImg 二维码图片
 * @property qrUrl 二维码链接
 * @constructor Create empty Qr create dto
 */
@Serializable
data class QrCreateInfoDto(@SerialName("qrimg") val qrImg: String = "", @SerialName("qrurl") val qrUrl: String = "")

/**
 * @Description ：   二维码key数据传输对象
 * @Date        ：   2023/6/26
 * @author      ：   高国峰
 *
 * @param code 二维码状态码
 * @param uniKey 二维码key
 */
@Serializable
data class QrKeyInfoDto(val code: Int = 0, @SerialName("unikey") val uniKey: String = "")