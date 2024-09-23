package com.uogames.balinasoft.core.network.model.image

data class ImageModelIn(
    val base64Image: String,
    val date: Long,
    val lat: Double,
    val lng: Double
)