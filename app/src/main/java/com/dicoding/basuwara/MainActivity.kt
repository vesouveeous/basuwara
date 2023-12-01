package com.dicoding.basuwara

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dicoding.basuwara.ui.Login
import com.dicoding.basuwara.ui.navigation.NavigationItem
import com.dicoding.basuwara.ui.navigation.Screen
import com.dicoding.basuwara.ui.screen.onboard.Onboarding
import com.dicoding.basuwara.ui.screen.register.Register
import com.dicoding.basuwara.ui.screen.home.HomeScreen
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var isShowBottomBar by remember { mutableStateOf(false) }

                    val navBackstackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackstackEntry?.destination?.route
                    Scaffold(
                        topBar = {  },
                        bottomBar = { if (currentRoute == Screen.Homepage.route) BottomBar(navController = navController) }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Onboarding.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.Onboarding.route) {
                                Onboarding(
                                    onGettingStartedClick = {
                                        isShowBottomBar = false
                                        navController.navigate("login_page") {
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    },
                                    onSkipClicked = {
                                        isShowBottomBar = false
                                        navController.navigate(Screen.Login.route) {
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                            composable(Screen.Login.route) {
                                Login(
                                    onCreateAccountClick = {
                                        isShowBottomBar = false
                                        navController.navigate(Screen.Register.route){
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    },
                                    onLoginClick = {
                                        isShowBottomBar = true
                                        navController.navigate(Screen.Homepage.route) {
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                            composable(Screen.Register.route,) {
                                Register(onSignInClick = {
                                    isShowBottomBar = false
                                    navController.navigate(Screen.Login.route){
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                })
                            }
                            composable(Screen.Homepage.route){
                                HomeScreen()
                            }
                            composable(Screen.Games.route) {
                                HomeScreen()
                            }
                            composable(Screen.Profile.route){
                                HomeScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            NavigationItem(
                title = "Home",
                icon = Icons.Default.Home,
                screen = Screen.Homepage
            ),
            NavigationItem(
                title = "Game",
                icon = Icons.Default.Gamepad,
                screen = Screen.Games
            ),
            NavigationItem(
                title = "profile",
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profile
            )
        )

        navigationItems.map {
            NavigationBarItem(
                selected = it.screen.route == currentRoute,
                onClick = {
                    navController.navigate(it.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = it.icon, contentDescription = it.title)
                },
                label = {
                    Text(text = it.title)
                }
            )
        }
    }
}