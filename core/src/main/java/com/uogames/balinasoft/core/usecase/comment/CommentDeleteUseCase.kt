package com.uogames.balinasoft.core.usecase.comment

import com.uogames.balinasoft.core.database.CommentRepository
import com.uogames.balinasoft.core.model.StatusModel
import com.uogames.balinasoft.core.network.CommentProvider

class CommentDeleteUseCase(
    private val commentRepository: CommentRepository,
    private val commentProvider: CommentProvider
) {

    suspend operator fun invoke(
        imageID: Int,
        commentID: Int
    ): StatusModel{
        return try {
            val comments = commentProvider.delete(commentID, imageID)
            if (comments.status != 200) return StatusModel.Error(Exception(comments.error.orEmpty()))
            commentRepository.deleteById(commentID)
            StatusModel.OK
        }catch (e: Exception){
            StatusModel.Error(e)
        }
    }


}