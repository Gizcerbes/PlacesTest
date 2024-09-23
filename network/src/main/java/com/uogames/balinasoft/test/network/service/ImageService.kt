package com.uogames.balinasoft.test.network.service

import com.uogames.balinasoft.test.network.dto.ResponseDto
import com.uogames.balinasoft.test.network.dto.image.ImageDtoIn
import com.uogames.balinasoft.test.network.dto.image.ImageDtoOut
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ImageService(
    private val client: HttpClient
) {

    suspend fun get(
        page: Int
    ): ResponseDto<List<ImageDtoOut>> = client.get("/api/image") {
        parameter("page", page)
    }.body()

    suspend fun post(
        imageDtoIn: ImageDtoIn,
        onProgress: (Float) -> Unit = {}
    ): ResponseDto<ImageDtoOut> = client.post("/api/image") {
        contentType(ContentType.Application.Json)
        setBody(imageDtoIn)
        onUpload { a, b -> onProgress(a.toFloat() / b) }
    }.body()

    suspend fun delete(
        id: Int
    ): ResponseDto<ImageDtoOut> = client.delete("/api/image/$id")
        .body()
}