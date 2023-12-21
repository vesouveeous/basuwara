package com.dicoding.basuwara.ui.screen.LoginPage

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val isError: String? = ""
)