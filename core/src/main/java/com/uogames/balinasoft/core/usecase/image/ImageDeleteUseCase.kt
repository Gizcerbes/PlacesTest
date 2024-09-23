package com.uogames.balinasoft.core.usecase.image

import com.uogames.balinasoft.core.database.ImageRepository
import com.uogames.balinasoft.core.model.StatusModel
import com.uogames.balinasoft.core.network.ImageProvider

class ImageDeleteUseCase(
    private val imageRepository: ImageRepository,
    private val imageProvider: ImageProvider
) {


    suspend operator fun invoke(
        imageID: Int
    ): StatusModel {

        return try {
            val r = imageProvider.delete(imageID)
            if (r.status != 200) return StatusModel.Error(Exception(r.error.orEmpty()))
            imageRepository.deleteByID(imageID)
            StatusModel.OK
        } catch (e: Exception) {
            StatusModel.Error(e)
        }

    }


}