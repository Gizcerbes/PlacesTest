package com.uogames.balinasoft.test.network.dto.comment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentDtoOut(
    @SerialName("id")
    val id: Int,
    @SerialName("date")
    val date: Long,
    @SerialName("text")
    val text: String
)