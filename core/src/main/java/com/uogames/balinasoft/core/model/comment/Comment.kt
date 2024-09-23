package com.uogames.balinasoft.core.model.comment

data class Comment(
    val id:Int,
    val imageID: Int,
    val date: Long,
    val text: String,
    val page: Int
)