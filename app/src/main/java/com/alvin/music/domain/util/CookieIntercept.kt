package com.alvin.music.domain.util

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Description ：   存储cookie的拦截器
 * @Date        ：   2023/6/26
 * @author      ：   高国峰
 */
class CookieIntercept : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        // 添加cookie到请求头
        val request = chain.request().newBuilder()
            .addHeader("Cookie", BaseKV.User.cookie ?: "")
            .build()
        return chain.proceed(request)
    }
}