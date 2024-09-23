package com.uogames.balinasoft.test.network.map.image

import com.uogames.balinasoft.core.model.image.Image
import com.uogames.balinasoft.test.network.dto.image.ImageDtoOut
import com.uogames.balinasoft.test.network.map.MapperModel

class ImageOutMapper(
    private val page: Int
) : MapperModel<ImageDtoOut, Image> {

    override fun ImageDtoOut.toModel(): Image = Image(
        id = id,
        url = url,
        date = date,
        lat = lat,
        lng = lng,
        page = page
    )

}