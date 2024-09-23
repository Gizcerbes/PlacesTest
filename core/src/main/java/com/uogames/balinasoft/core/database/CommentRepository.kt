package com.uogames.balinasoft.core.database

import com.uogames.balinasoft.core.model.comment.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    suspend fun saveComments(vararg comments: Comment)

    suspend fun clearByImageID(imageID: Int)

    suspend fun clear()

    suspend fun deleteById(id: Int)

    fun getListFlow(imageID: Int): Flow<List<Comment>>

    suspend fun deleteInstead(ids: List<Int>, page: Int, imageID: Int)

    suspend fun deleteOnPage(page: Int)

}