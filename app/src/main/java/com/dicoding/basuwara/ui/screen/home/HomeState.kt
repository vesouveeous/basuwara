package com.dicoding.basuwara.ui.screen.home

import com.dicoding.basuwara.data.model.UserModel

data class HomeState(
    val isLoading: Boolean = false,
    val isSuccess: UserModel? = null,
    val isError: String? = ""
)