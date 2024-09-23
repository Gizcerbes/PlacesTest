package com.uogames.balinasoft.test.network.provider

import com.uogames.balinasoft.core.network.AuthProvider
import com.uogames.balinasoft.core.network.model.ResponseModel
import com.uogames.balinasoft.core.network.model.user.SignUserModelIn
import com.uogames.balinasoft.core.network.model.user.SignUserOutModel
import com.uogames.balinasoft.test.network.map.ResponseDtoMapper
import com.uogames.balinasoft.test.network.map.auth.SignUserInMapper
import com.uogames.balinasoft.test.network.map.auth.SignUserOutMapper
import com.uogames.balinasoft.test.network.service.AuthService

class AuthProviderImpl(
    private val service: AuthService
): AuthProvider {

    private val responseMapper = ResponseDtoMapper()
    private val signUserInMapper = SignUserInMapper()
    private val signUserOutMapper = SignUserOutMapper()

    override suspend fun signIn(sign: SignUserModelIn): ResponseModel<SignUserOutModel> {
        return responseMapper.run { service.signIn(signUserInMapper.run { sign.toDTO() }).toModel(signUserOutMapper) }
    }

    override suspend fun signUp(sign: SignUserModelIn): ResponseModel<SignUserOutModel> {
        return responseMapper.run { service.signUp(signUserInMapper.run { sign.toDTO() }).toModel(signUserOutMapper) }
    }


}