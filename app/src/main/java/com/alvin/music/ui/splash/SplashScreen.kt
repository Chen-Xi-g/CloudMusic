package com.alvin.music.ui.splash

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alvin.music.R
import com.alvin.music.ui.theme.COLOR_222

/**
 * 启动页
 *
 * @param startHomeListener 启动主页
 */
@Composable
fun SplashScreen(startHomeListener: () -> Unit) {
    var isAgree by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(top = 100.dp, start = 50.dp, end = 50.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = COLOR_222,
                disabledContentColor = Color.White,
                disabledContainerColor = COLOR_222
            ),
        ) {
            if (isAgree) {
                Copyright(startHomeListener)
            } else {
                Disclaimer {
                    isAgree = true
                }
            }
        }
        Spacer(modifier = Modifier.weight(1F))
        Icon(
            painter = painterResource(id = R.drawable.ic_splash_tips),
            contentDescription = "欢迎语",
            tint = Color.White,
            modifier = Modifier.padding(bottom = 50.dp, start = 80.dp, end = 80.dp)
        )
    }
}

/**
 * 免责声明
 */
@Composable
fun Disclaimer(agreeOnCLick: () -> Unit) {
    Column(
        modifier = Modifier.background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "免责声明",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 50.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "1. 本软件只用作个人学习研究，禁止用于商业及非法用途，如产生法律纠纷与本人无关",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "2. 本软件api来自于github，非官方版api，本软件不提供任何音频存储服务，如需下载音频，请支持正版！",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "3. 音乐版权归各网站所有，本软件不承担任何法律责任和连带责任。如果已经涉及到您的版权，请速与本站管理员联系，我们将第一时间为你处理。",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "4. 本软件并不是一个破解软件，不提供下载任何歌曲服务，其中包括付费歌曲！",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Button(onClick = agreeOnCLick, modifier = Modifier.padding(20.dp)) {
            Text(text = "我已阅读并同意以上内容", color = Color.White)
        }
    }
}

/**
 * 版权声明
 */
@Composable
fun Copyright(agreeOnCLick: () -> Unit) {
    Column(
        modifier = Modifier.background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "版权声明",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 50.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "“网易云”、“网易云音乐”等文字、图形和商业标识，其著作权或商标权归网易所有。 网易云音乐享有对其平台授权音乐的版权，请勿随意下载，复制版权内容。具体内容请参考网易云音乐用户协议。",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            Button(
                onClick = agreeOnCLick,
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            ) {
                Text(
                    text = "我已阅读并同意以上内容和网易云音乐用户协议",
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}