package com.dicoding.basuwara.data.model

data class UserModel(
    val id: String? = null,
    val name: String,
    val email: String,
    val password: String,
    val phone: String
)