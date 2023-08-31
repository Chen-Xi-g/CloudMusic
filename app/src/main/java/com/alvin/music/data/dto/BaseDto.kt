package com.alvin.music.data.dto

import kotlinx.serialization.Serializable

/**
 * @Description ：   响应数据基类
 * @Date        ：   2023/6/26
 * @author      ：   高国峰
 */
@Serializable
open class BaseDto(
    val code: Int = 0,
    val message: String? = null
){
    fun isSuccess() = code == 200
}