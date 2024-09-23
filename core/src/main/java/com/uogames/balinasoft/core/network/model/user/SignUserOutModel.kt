package com.uogames.balinasoft.core.network.model.user

data class SignUserOutModel(
    val userID: Int,
    val login: String,
    val token: String
)