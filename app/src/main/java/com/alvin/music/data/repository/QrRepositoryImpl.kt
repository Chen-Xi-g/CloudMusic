package com.alvin.music.data.repository

import com.alvin.music.data.mappers.toQrEntity
import com.alvin.music.data.mappers.toQrStatusEntity
import com.alvin.music.data.remote.QrApi
import com.alvin.music.domain.login.QrEntity
import com.alvin.music.domain.login.QrStatusEntity
import com.alvin.music.domain.repository.QrRepository
import com.alvin.music.domain.util.BaseKV
import com.alvin.music.domain.util.Resource
import javax.inject.Inject

/**
 * @Description ：   二维码数据仓库实现
 * @Date        ：   2023/6/26
 * @author      ：   高国峰
 */
class QrRepositoryImpl @Inject constructor(
    private val api: QrApi
) : QrRepository {
    override suspend fun getQrData(): Resource<QrEntity> {
        return try {
            val generateQrKey = api.generateQrKey()
            if (generateQrKey.isSuccess() && generateQrKey.data?.code == 200) {
                return Resource.Success(
                    data = api.createQrCode(generateQrKey.data.uniKey)
                        .toQrEntity(generateQrKey.data.uniKey)
                )
            }
            Resource.Success(null)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "未知错误")
        }
    }

    override suspend fun checkQrCode(key: String): Resource<QrStatusEntity> {
        return try {
            val checkQrCode = api.checkQrCode(key)
            BaseKV.User.cookie = checkQrCode.cookie
            Resource.Success(checkQrCode.toQrStatusEntity())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "未知错误")
        }
    }

    override suspend fun getLoginStatus(): Resource<Any> {
        return Resource.Success(null)
    }
}