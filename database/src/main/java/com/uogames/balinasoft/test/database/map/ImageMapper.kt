package com.uogames.balinasoft.test.database.map

import kotlin.math.ln

typealias ImageDTO = com.uogames.balinasoft.core.model.image.Image
typealias ImageEntity = com.uogames.balinasoft.test.database.entity.ImageEntity

object ImageMapper {


    fun ImageDTO.toEntity() = ImageEntity(
        id = id,
        url = url,
        date = date,
        lat = lat,
        lng = lng,
        page = page
    )

    fun ImageEntity.toDTO() = ImageDTO(
        id = id,
        url = url,
        date = date,
        lat = lat,
        lng = lng,
        page = page
    )

}