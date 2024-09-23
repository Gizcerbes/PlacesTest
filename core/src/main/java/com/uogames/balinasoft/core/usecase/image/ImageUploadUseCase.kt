package com.uogames.balinasoft.core.usecase.image

import com.uogames.balinasoft.core.database.ImageRepository
import com.uogames.balinasoft.core.model.StatusModel
import com.uogames.balinasoft.core.network.ImageProvider
import com.uogames.balinasoft.core.network.model.image.ImageModelIn
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class ImageUploadUseCase(
    private val imageRepository: ImageRepository,
    private val imageProvider: ImageProvider
) {

    companion object {
        val maxImageSize = 2_000_000
        val preferredImageSize = 1_500_000
    }

    class TooBig : Exception()
    class TooSmall : Exception()


    @OptIn(ExperimentalEncodingApi::class)
    suspend operator fun invoke(
        image: ByteArray,
        date: Long,
        lat: Double,
        lng: Double,
        onProgress: (Float) -> Unit = { }
    ): StatusModel {
        if (image.size > maxImageSize) return StatusModel.Error(TooBig())
        if (image.size < 20) return StatusModel.Error(TooSmall())

        return try {
            val r = imageProvider.upload(
                ImageModelIn(
                    base64Image = Base64.encode(image, 0, image.size),
                    date = date,
                    lat = lat,
                    lng = lng
                ),
                onProgress
            )
            if (r.status != 200) return StatusModel.Error(Exception(r.error.orEmpty()))
            if (r.data == null) return StatusModel.Error(Exception(r.error.orEmpty()))
            imageRepository.saveImages(r.data)
            StatusModel.OK
        } catch (e: Exception) {
            StatusModel.Error(e)
        }
    }


}