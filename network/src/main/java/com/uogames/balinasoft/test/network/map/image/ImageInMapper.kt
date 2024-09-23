package com.uogames.balinasoft.test.network.map.image

import com.uogames.balinasoft.core.network.model.image.ImageModelIn
import com.uogames.balinasoft.test.network.dto.image.ImageDtoIn
import com.uogames.balinasoft.test.network.map.MapperDTO

class ImageInMapper : MapperDTO<ImageModelIn, ImageDtoIn> {

    override fun ImageModelIn.toDTO(): ImageDtoIn = ImageDtoIn(
        base64Image = base64Image,
        date = date,
        lat = lat,
        lng = lng
    )


}