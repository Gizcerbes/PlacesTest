package com.uogames.balinasoft.core.network

import com.uogames.balinasoft.core.network.model.ResponseModel
import com.uogames.balinasoft.core.network.model.comment.CommentModelIn
import com.uogames.balinasoft.core.model.comment.Comment

interface CommentProvider {

    suspend fun get(
        imageID: Int,
        page:Int
    ): ResponseModel<List<Comment>>

    suspend fun post(
        dtoIn: CommentModelIn,
        imageID: Int
    ) : ResponseModel<Comment>


    suspend fun delete(
        commentID: Int,
        imageID: Int
    ) : ResponseModel<Comment>
}