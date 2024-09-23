package com.uogames.balinasoft.test.network.provider

import com.uogames.balinasoft.core.model.image.Image
import com.uogames.balinasoft.core.network.ImageProvider
import com.uogames.balinasoft.core.network.model.ResponseModel
import com.uogames.balinasoft.core.network.model.image.ImageModelIn
import com.uogames.balinasoft.test.network.map.ResponseDtoMapper
import com.uogames.balinasoft.test.network.map.image.ImageInMapper
import com.uogames.balinasoft.test.network.map.image.ImageOutMapper
import com.uogames.balinasoft.test.network.service.ImageService

class ImageProviderImpl(
    private val service: ImageService
) : ImageProvider {

    private val responseMapper = ResponseDtoMapper()
    private val imageModelMapper = ImageInMapper()

    override suspend fun get(page: Int): ResponseModel<List<Image>> {
        return responseMapper.run { service.get(page).toListModel(ImageOutMapper(page)) }
    }

    override suspend fun upload(
        imageIn: ImageModelIn,
        onProgress: (Float) -> Unit
    ): ResponseModel<Image> {
        return responseMapper.run {
            service.post(
                imageModelMapper.run { imageIn.toDTO() },
                onProgress
            ).toModel(ImageOutMapper(Int.MAX_VALUE))
        }
    }

    override suspend fun delete(id: Int): ResponseModel<Image> {
        return responseMapper.run {
            service.delete(id).toModel(ImageOutMapper(Int.MAX_VALUE))
        }
    }
}