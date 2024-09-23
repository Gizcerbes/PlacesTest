package com.uogames.balinasoft.core.network

import com.uogames.balinasoft.core.network.model.ResponseModel
import com.uogames.balinasoft.core.network.model.image.ImageModelIn
import com.uogames.balinasoft.core.model.image.Image

interface ImageProvider {


    suspend fun get(
        page: Int
    ): ResponseModel<List<Image>>

    suspend fun delete(
        id: Int
    ): ResponseModel<Image>

    suspend fun upload(
        imageIn: ImageModelIn,
        onProgress: (Float) -> Unit = { }
    ): ResponseModel<Image>
}