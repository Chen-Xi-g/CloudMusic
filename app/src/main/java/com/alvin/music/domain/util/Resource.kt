package com.alvin.music.domain.util

/**
 * @Description ：   网络请求返回的数据封装类
 *
 * @param T 返回的数据类型
 * @property data 返回的数据
 * @property message 返回的信息
 * @constructor Create empty Resource
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {

    /**
     * @Description ：   成功
     *
     * @param T 返回的数据类型
     * @constructor Create empty Success
     *
     * @param data 返回的数据
     */
    class Success<T>(data: T?) : Resource<T>(data)

    /**
     * @Description ：   失败
     *
     * @param T 返回的数据类型
     * @constructor Create empty Error
     *
     * @param message 返回的信息
     * @param data 返回的数据
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

}