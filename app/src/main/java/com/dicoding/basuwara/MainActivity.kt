package com.dicoding.basuwara


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Quiz
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
import androidx.compose.ui.text.font.FontWeight
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dicoding.basuwara.ui.Login
import com.dicoding.basuwara.ui.navigation.NavigationItem
import com.dicoding.basuwara.ui.navigation.Screen
import com.dicoding.basuwara.ui.screen.onboard.Onboarding
import com.dicoding.basuwara.ui.screen.register.Register
import com.dicoding.basuwara.ui.screen.home.HomeScreen
import com.dicoding.basuwara.ui.screen.image.CameraContent
import com.dicoding.basuwara.ui.screen.profile.ProfileScreen
import com.dicoding.basuwara.ui.screen.quiz.ChooseQuizScreen
import com.dicoding.basuwara.ui.screen.quiz.QuizResultScreen
import com.dicoding.basuwara.ui.screen.quiz.QuizScreen
import com.dicoding.basuwara.ui.screen.study.StudyScreen
import com.dicoding.basuwara.ui.theme.Material3ComposeTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            Material3ComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var isShowBottomBar by remember { mutableStateOf(false) }
                    var isShowTopBar by remember { mutableStateOf(false) }

                    val navBackstackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackstackEntry?.destination?.route
                    Scaffold(
                        topBar = { if (isShowTopBar) TopBar(title = getTopBarTitle(currentRoute!!)) },
                        bottomBar = { if (isShowBottomBar) BottomBar(navController = navController) },
                        floatingActionButton = {
                            if (currentRoute == Screen.Homepage.route) MyFab(navController = navController)
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Homepage.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.Onboarding.route) {
                                isShowTopBar = false
                                isShowBottomBar = false
                                Onboarding(
                                    onGettingStartedClick = {
                                        navController.navigate("login_page") {
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    },
                                    onSkipClicked = {
                                        navController.navigate(Screen.Login.route) {
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                            composable(Screen.Login.route) {
                                isShowTopBar = false
                                isShowBottomBar = false
                                Login(
                                    onCreateAccountClick = {
                                        navController.navigate(Screen.Register.route){
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    },
                                    onLoginClick = {
                                        navController.navigate(Screen.Homepage.route) {
                                            popUpTo(Screen.Onboarding.route){
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                            composable(Screen.Register.route,) {
                                isShowTopBar = false
                                isShowBottomBar = false
                                Register(onSignInClick = {
                                    navController.navigate(Screen.Login.route){
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                })
                            }
                            composable(Screen.Homepage.route){
                                isShowTopBar = false
                                isShowBottomBar = true
                                HomeScreen(dummyClick = {
                                    navController.navigate(Screen.Login.route) {
                                            popUpTo(navController.graph.startDestinationId){
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    },
                                    goToOnboardingPage = {
                                        navController.navigate(Screen.Onboarding.route){
                                            popUpTo(navController.graph.startDestinationId){
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    },
                                    onCourseChosen = {
                                        navController.navigate(Screen.Study.createRoute(it)) {
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                            composable(Screen.ChooseQuiz.route) {
                                isShowTopBar = true
                                isShowBottomBar = true
                                ChooseQuizScreen(onQuizSelected = {
                                    navController.navigate(Screen.Quiz.createRoute(it))
                                })
                            }
                            composable(
                                route = Screen.Quiz.route,
                                arguments = listOf(navArgument("quiz_type") { type = NavType.StringType}),
                                ) {
                                isShowTopBar = true
                                isShowBottomBar = false
                                QuizScreen(
                                    quizType = it.arguments?.getString("quiz_type") ?: "",
                                    onFinishClick = {
                                        navController.navigate(Screen.QuizResult.createRoute(it)) {
                                            popUpTo(Screen.ChooseQuiz.route)
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                            composable(Screen.Profile.route){
                                isShowTopBar = false
                                isShowBottomBar = true
                                ProfileScreen()
                            }
                            composable(
                                route = Screen.QuizResult.route,
                                arguments = listOf(navArgument("result") { type = NavType.IntType}),
                                ) {
                                isShowTopBar = false
                                isShowBottomBar = false
                                QuizResultScreen(
                                    score = it.arguments?.getInt("result") ?: 0,
                                    onDoneClick = {
                                        navController.navigate(Screen.ChooseQuiz.route) {
                                            popUpTo(Screen.ChooseQuiz.route)
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                            composable(Screen.Camera.route) {
                                isShowTopBar = false
                                isShowBottomBar = false
                                CameraContent()
                            }
                            composable(
                                route = Screen.Study.route,
                                arguments = listOf(navArgument("course_type") { type = NavType.StringType})
                            ) {
                                isShowTopBar = true
                                isShowBottomBar = false
                                StudyScreen(
                                    courseType = it.arguments?.getString("course_type") ?: ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(
    title: String
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        backgroundColor = MaterialTheme.colorScheme.primary
    )
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
                title = "Quiz",
                icon = Icons.Default.Quiz,
                screen = Screen.ChooseQuiz
            ),
            NavigationItem(
                title = "Profile",
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

@Composable
fun MyFab(
    navController: NavHostController
) {
    FloatingActionButton(onClick = {
        navController.navigate(Screen.Camera.route) {
            launchSingleTop = true
        }
    }) {
        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "camera")
    }
}

private fun getTopBarTitle(route: String): String {
    return when(route) {
        Screen.ChooseQuiz.route -> "Choose Quiz"
        Screen.Quiz.route -> "Quiz"
        Screen.Study.route -> "Study"
        else -> ""
    }
}