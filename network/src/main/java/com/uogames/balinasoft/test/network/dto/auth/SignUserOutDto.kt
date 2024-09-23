package com.uogames.balinasoft.test.network.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUserOutDto(
    @SerialName("userId")
    val userID: Int,
    @SerialName("login")
    val login: String,
    @SerialName("token")
    val token: String
)