package com.uogames.balinasoft.core.usecase.comment

import com.uogames.balinasoft.core.database.CommentRepository
import com.uogames.balinasoft.core.model.StatusModel
import com.uogames.balinasoft.core.network.CommentProvider
import com.uogames.balinasoft.core.network.model.comment.CommentModelIn

class CommentSendUseCase(
    private val commentProvider: CommentProvider,
    private val commentRepository: CommentRepository
) {

    class Forbidden : Exception()
    class TextEmpty : Exception()


    suspend operator fun invoke(
        imageID: Int,
        text: String
    ): StatusModel {

        if (text.isEmpty()) return StatusModel.Error(TextEmpty())

        return try {
            val r = commentProvider.post(CommentModelIn(text), imageID)
            if (r.status == 403) return StatusModel.Error(Forbidden())
            if (r.status != 200) return StatusModel.Error(Exception(r.error.orEmpty()))
            if (r.data == null) return StatusModel.Error(Exception(r.error.orEmpty()))
            commentRepository.saveComments(r.data)
            StatusModel.OK
        } catch (e: Exception) {
            StatusModel.Error(Exception())
        }
    }


}