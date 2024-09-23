package com.uogames.balinasoft.test.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto<T>(
    @SerialName("status")
    val status: Int,
    @SerialName("data")
    val data: T? = null,
    @SerialName("error")
    val error: String? = null,
    @SerialName("valid")
    val valid: List<Map<String, String>>? = null
)