package com.uogames.balinasoft.core.usecase.comment

import com.uogames.balinasoft.core.database.CommentRepository
import com.uogames.balinasoft.core.model.StatusModel
import com.uogames.balinasoft.core.network.CommentProvider

class CommentUpdateOnPageUseCase(
    private val commentProvider: CommentProvider,
    private val commentRepository: CommentRepository
) {

    class GotEmpty : Exception()
    class NotFound : Exception()

    suspend operator fun invoke(
        imageID: Int,
        page: Int
    ): StatusModel {
        return try {
            val comments = commentProvider.get(imageID, page)
            if (comments.status == 404) return StatusModel.Error(NotFound())
            if (comments.status != 200) return StatusModel.Error(Exception(comments.error.orEmpty()))
            if (comments.data == null) return StatusModel.Error(Exception(comments.error.orEmpty()))
            if (comments.data.isEmpty()) {
                commentRepository.deleteOnPage(Int.MAX_VALUE)
                return StatusModel.Error(GotEmpty())
            }
            commentRepository.saveComments(*comments.data.toTypedArray())
            commentRepository.deleteInstead(comments.data.map { it.id }, page, imageID)
            StatusModel.OK
        } catch (e: Exception) {
            StatusModel.Error(e)
        }
    }

}