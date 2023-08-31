package com.alvin.music.di

import android.app.Application
import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.alvin.music.data.remote.HomeApi
import com.alvin.music.data.remote.QrApi
import com.alvin.music.domain.util.CustomPlayer
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

/**
 * @Description ：   描述
 * @Date        ：   2023/6/26
 * @author      ：   高国峰
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    private val json = Json {
        ignoreUnknownKeys = true // JSON和数据模型字段可以不匹配
        coerceInputValues = true // 如果JSON字段是Null则使用默认值
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    /**
     * 提供二维码接口
     *
     * @return QrApi
     */
    @Provides
    @Singleton
    fun provideQrApi(): QrApi {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
        return Retrofit.Builder()
            .baseUrl("https://api.gaoguofeng.top/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideHomeApi(): HomeApi{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
        return Retrofit.Builder()
            .baseUrl("https://api.gaoguofeng.top/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideExoPlayer(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }

    @Provides
    @Singleton
    fun provideCustomPlayer(player: ExoPlayer): CustomPlayer {
        return CustomPlayer(player)
    }

}