package com.alvin.music

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp

/**
 * @Description ：   Application
 * @Date        ：   2023/6/26
 * @author      ：   高国峰
 */
@HiltAndroidApp
class BaseApp: Application(){

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var CONTEXT: Context
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = this
        MMKV.initialize(this)
    }
}