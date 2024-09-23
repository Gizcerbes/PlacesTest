package com.uogames.balinasoft.test.network.map

import com.uogames.balinasoft.core.network.model.ResponseModel
import com.uogames.balinasoft.test.network.dto.ResponseDto


class ResponseDtoMapper {

    fun <T, S> ResponseDto<T>.toModel(mapper: MapperModel<T, S>): ResponseModel<S> = ResponseModel(
        status = status,
        data = mapper.run { data?.toModel() },
        error = error,
        valid = valid
    )

    fun <T, S> ResponseDto<List<T>>.toListModel(mapper: MapperModel<T, S>): ResponseModel<List<S>> =
        ResponseModel(
            status = status,
            data = mapper.run { data?.map { it.toModel() } },
            error = error,
            valid = valid
        )

}