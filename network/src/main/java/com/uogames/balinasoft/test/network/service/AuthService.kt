package com.uogames.balinasoft.test.network.service

import com.uogames.balinasoft.test.network.dto.ResponseDto
import com.uogames.balinasoft.test.network.dto.auth.SignUserDtoIn
import com.uogames.balinasoft.test.network.dto.auth.SignUserOutDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthService(
    private val client: HttpClient
) {

    suspend fun signIn(
        userDtoIn: SignUserDtoIn
    ): ResponseDto<SignUserOutDto> = client.post("/api/account/signin"){
        contentType(ContentType.Application.Json)
        setBody(userDtoIn)
    }.body()

    suspend fun signUp(
        userDtoIn: SignUserDtoIn
    ): ResponseDto<SignUserOutDto> = client.post("/api/account/signup"){
        contentType(ContentType.Application.Json)
        setBody(userDtoIn)
    }.body()

}