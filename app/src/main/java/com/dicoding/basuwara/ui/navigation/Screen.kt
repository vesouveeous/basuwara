package com.dicoding.basuwara.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding: Screen("onboarding_page")
    object Register: Screen("register_page")
    object Login: Screen("login_page")
    object Homepage: Screen("home_page")
    object Games: Screen("game_page")
    object Profile: Screen("profile_page")
}