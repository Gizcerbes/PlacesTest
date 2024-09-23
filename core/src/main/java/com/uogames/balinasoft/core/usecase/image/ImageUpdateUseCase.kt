package com.uogames.balinasoft.core.usecase.image

import com.uogames.balinasoft.core.database.ImageRepository
import com.uogames.balinasoft.core.model.StatusModel
import com.uogames.balinasoft.core.network.ImageProvider

class ImageUpdateUseCase(
    private val imageRepository: ImageRepository,
    private val imageProvider: ImageProvider
) {

    class GotEmpty : Exception()


    suspend operator fun invoke(
        page: Int
    ): StatusModel {
        return try {
            val r = imageProvider.get(page)
            if (r.status != 200) return StatusModel.Error(Exception(r.error.orEmpty()))
            if (r.data == null) return StatusModel.Error(Exception(r.error.orEmpty()))
            if (r.data.isEmpty()) {
                imageRepository.deleteOnPage(Int.MAX_VALUE)
                return StatusModel.Error(GotEmpty())
            }
            imageRepository.saveImages(*r.data.toTypedArray())
            imageRepository.deleteInstead(r.data.map { it.id }, page)
            StatusModel.OK
        } catch (e: Exception) {
            StatusModel.Error(e)
        }
    }


}