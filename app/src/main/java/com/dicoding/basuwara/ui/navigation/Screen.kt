package com.dicoding.basuwara.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding: Screen("onboarding_page")
    object Register: Screen("register_page")
    object Login: Screen("login_page")
    object Homepage: Screen("home_page")
    object Study: Screen("study/{course_type}") {
        fun createRoute(courseType: String) = "study/$courseType"
    }
    object Quiz: Screen("quiz_page/{quiz_type}") {
        fun createRoute(quizType: String) = "quiz_page/$quizType"
    }
    object ChooseQuiz: Screen("choose_quiz_page")
    object QuizResult: Screen("quiz_result/{result}") {
        fun createRoute(result: Int) = "quiz_result/$result"
    }
    object Profile: Screen("profile_page")

    object Camera: Screen("camera")
}