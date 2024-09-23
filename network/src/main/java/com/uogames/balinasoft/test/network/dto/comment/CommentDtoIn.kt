package com.uogames.balinasoft.test.network.dto.comment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CommentDtoIn(
    @SerialName("text")
    val text: String
)