package com.alvin.music.ui.route

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alvin.music.R
import com.alvin.music.domain.util.BaseKV
import com.alvin.music.ui.home.HomePage
import com.alvin.music.ui.home.HomeViewModel
import com.alvin.music.ui.login.LoginScreen
import com.alvin.music.ui.player.PlayDetailScreen
import com.alvin.music.ui.splash.SplashScreen
import com.alvin.music.ui.theme.COLOR_091227
import com.alvin.music.ui.theme.COLOR_EB3B1C
import com.alvin.music.ui.theme.COLOR_F0F6FF
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

/**
 * @Description ：   页面路由
 *
 * @param navController 导航
 */
@Composable
fun HomeRoute(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val homeViewModel: HomeViewModel = hiltViewModel()
    /*起始路由固定，原因是此软件只是学习使用，使用此软件必须阅读免责声明。*/
    NavHost(navController = navController, startDestination = RouteConfig.SPLASH) {
        /*启动页*/
        composable(RouteConfig.SPLASH){
            SplashScreen{
                scope.launch {
                    if (BaseKV.User.isLogin){
                        // 已登录，跳转到首页
                        navController.navigate(RouteConfig.HOME){
                            popUpTo(RouteConfig.SPLASH){
                                inclusive = true
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }else{
                        // 未登录，跳转到登录页
                        navController.navigate(RouteConfig.LOGIN){
                            popUpTo(RouteConfig.SPLASH){
                                inclusive = true
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        }
        /*首页*/
        composable(RouteConfig.HOME) {
            val systemUiController = rememberSystemUiController()
            val background = MaterialTheme.colorScheme.background
            SideEffect {
//                showSystemUI(systemUiController, background)
            }
            HomePage(homeViewModel)
        }
        /*登录页*/
        composable(RouteConfig.LOGIN) {
            LoginScreen{
                // 登录成功，跳转到首页
                BaseKV.User.isLogin = true
                navController.navigate(RouteConfig.HOME){
                    popUpTo(RouteConfig.LOGIN){
                        inclusive = true
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }
}