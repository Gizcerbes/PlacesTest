package com.uogames.balinasoft.test.network.map.auth

import com.uogames.balinasoft.core.network.model.user.SignUserOutModel
import com.uogames.balinasoft.test.network.dto.auth.SignUserOutDto
import com.uogames.balinasoft.test.network.map.MapperModel

class SignUserOutMapper: MapperModel<SignUserOutDto, SignUserOutModel> {

    override fun SignUserOutDto.toModel(): SignUserOutModel {
        return SignUserOutModel(
            userID = userID,
            login = login,
            token = token
        )
    }
}