package com.uogames.balinasoft.test.network.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUserDtoIn(
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String
)