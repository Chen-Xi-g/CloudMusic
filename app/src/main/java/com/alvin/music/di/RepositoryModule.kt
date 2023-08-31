package com.alvin.music.di

import com.alvin.music.data.repository.HomeRepositoryImpl
import com.alvin.music.data.repository.QrRepositoryImpl
import com.alvin.music.domain.repository.HomeRepository
import com.alvin.music.domain.repository.QrRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

/**
 * @Description ：   存储库模块
 *
 * @constructor Create empty Repository module
 */
@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * @Description ：   绑定首页存储库
     *
     * @param homeRepositoryImpl 首页存储库实现
     * @return HomeRepository
     */
    @Binds
    @Singleton
    abstract fun bindHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    /**
     * @Description ：   绑定二维码存储库
     *
     * @param qrRepositoryImpl 二维码存储库实现
     * @return QrRepository
     */
    @Binds
    @Singleton
    abstract fun bindQrRepository(qrRepositoryImpl: QrRepositoryImpl): QrRepository

}