package com.uogames.balinasoft.core.model.image

data class Image(
    val id: Int,
    val url: String,
    val date: Long,
    val lat: Double,
    val lng: Double,
    val page: Int
)