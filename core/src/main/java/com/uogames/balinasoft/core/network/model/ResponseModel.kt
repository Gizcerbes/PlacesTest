package com.uogames.balinasoft.core.network.model

data class ResponseModel<T>(
    val status: Int,
    val data: T? = null,
    val error: String? = null,
    val valid: List<Map<String, String>>? = null
)