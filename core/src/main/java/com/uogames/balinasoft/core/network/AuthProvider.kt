package com.uogames.balinasoft.core.network

import com.uogames.balinasoft.core.network.model.ResponseModel
import com.uogames.balinasoft.core.network.model.user.SignUserModelIn
import com.uogames.balinasoft.core.network.model.user.SignUserOutModel

interface AuthProvider {

    suspend fun signIn(
        sign: SignUserModelIn
    ) : ResponseModel<SignUserOutModel>

    suspend fun signUp(
        sign: SignUserModelIn
    ): ResponseModel<SignUserOutModel>

}