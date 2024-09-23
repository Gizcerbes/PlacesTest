package com.uogames.balinasoft.test.network.service

import com.uogames.balinasoft.test.network.dto.ResponseDto
import com.uogames.balinasoft.test.network.dto.comment.CommentDtoIn
import com.uogames.balinasoft.test.network.dto.comment.CommentDtoOut
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CommentService(
    private val client: HttpClient
) {


    suspend fun get(
        imageID: Int,
        page: Int = 1
    ): ResponseDto<List<CommentDtoOut>> = client.get("/api/image/$imageID/comment") {
        parameter("page", page)
    }.body()


    suspend fun post(
        imageID: Int,
        commentDtoIn: CommentDtoIn
    ): ResponseDto<CommentDtoOut> = client.post("/api/image/$imageID/comment"){
        contentType(ContentType.Application.Json)
        setBody(commentDtoIn)
    }.body()

    suspend fun delete(
        imageID: Int,
        commentID: Int
    ): ResponseDto<CommentDtoOut> = client.delete("/api/image/$imageID/comment/$commentID")
        .body()

}