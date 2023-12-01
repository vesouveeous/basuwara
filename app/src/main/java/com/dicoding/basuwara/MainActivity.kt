package com.dicoding.basuwara

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dicoding.basuwara.ui.Login
import com.dicoding.basuwara.ui.onboard.Onboarding
import com.dicoding.basuwara.ui.register.Register
import com.dicoding.basuwara.ui.screen.home.HomeScreen
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "onboarding_page"
            ) {
                composable("onboarding_page") {
                    Onboarding(
                        navController = navController,
                        onGettingStartedClick = {
                            // Handle "Get Started" click
                        },
                        onSkipClicked = {
                            // Handle "Skip" click
                        }
                    )
                }
                composable("login_page") {
                    Login(navController = navController)
                }
                composable(
                    "register_page",
                    content = {
                        Register(navController = navController)
                    }
                )
                composable("homepage"){
                    HomeScreen()
                }
            }
        }
    }
}