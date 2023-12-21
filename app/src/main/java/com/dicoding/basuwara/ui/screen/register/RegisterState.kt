package com.dicoding.basuwara.ui.screen.register

data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean? = false,
    val isError: String? = ""
)