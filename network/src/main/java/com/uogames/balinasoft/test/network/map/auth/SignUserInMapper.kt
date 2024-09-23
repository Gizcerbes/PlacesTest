package com.uogames.balinasoft.test.network.map.auth

import com.uogames.balinasoft.core.network.model.user.SignUserModelIn
import com.uogames.balinasoft.test.network.dto.auth.SignUserDtoIn
import com.uogames.balinasoft.test.network.map.MapperDTO

class SignUserInMapper : MapperDTO<SignUserModelIn, SignUserDtoIn> {

    override fun SignUserModelIn.toDTO(): SignUserDtoIn {
        return SignUserDtoIn(
            login = login,
            password = password
        )
    }

}