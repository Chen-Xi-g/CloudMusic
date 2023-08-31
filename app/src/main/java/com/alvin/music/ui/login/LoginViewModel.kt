package com.alvin.music.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvin.music.domain.login.QrEntity
import com.alvin.music.domain.login.QrStatusEntity
import com.alvin.music.domain.repository.QrRepository
import com.alvin.music.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

/**
 * @Description ：   登录界面的ViewModel
 * @Date        ：   2023/6/26
 * @author      ：   高国峰
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: QrRepository
) : ViewModel() {

    /**
     * 登录状态管理
     */
    var state by mutableStateOf(LoginState())
        private set

    /**
     * 获取登录二维码数据
     */
    fun loadQrData(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when(val result = repository.getQrData()){
                is Resource.Success ->{
                    state = state.copy(
                        qrEntity = result.data,
                        isLoading = false,
                        error = null
                    )
                    result.data?.key?.let {
                        checkQrCode(it)
                    }
                }
                is Resource.Error ->{
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        qrEntity = null
                    )
                }
            }
        }
    }

    /**
     * 校验二维码扫描状态
     */
    fun checkQrCode(key: String) {
        viewModelScope.launch {
            while (state.qrStatusEntity?.status != 803){
                delay(2.seconds)
                when (val result = repository.checkQrCode(key)) {
                    is Resource.Success -> {
                        // 这里判断昵称是否改变，原因：返回的头像加载地址不同，会导致头像闪烁，有时返回p1.music.126.net，有时返回p2.music.126.net...
                        if (result.data?.nickname != state.qrStatusEntity?.nickname){
                            state = state.copy(
                                qrStatusEntity = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                        if (result.data?.status == 800){
                            loadQrData()
                            return@launch
                        }
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            error = result.message,
                            isLoading = false,
                            qrStatusEntity = null
                        )
                    }
                }
            }
        }
    }

}

/**
 * 登录状态
 *
 * @property qrEntity 二维码数据
 * @property qrStatusEntity 二维码扫描状态
 * @property isLoading 是否正在加载
 * @property error 错误信息
 * @constructor Create empty Login state
 */
data class LoginState(
    val qrEntity: QrEntity? = null,
    val qrStatusEntity: QrStatusEntity? = QrStatusEntity(status = 801, message = "请使用网易云音乐app扫码授权登录"),
    val isLoading: Boolean = false,
    val error: String? = null
)