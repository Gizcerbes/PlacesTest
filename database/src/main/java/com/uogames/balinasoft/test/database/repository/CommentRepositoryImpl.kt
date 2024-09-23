package com.uogames.balinasoft.test.database.repository

import com.uogames.balinasoft.core.database.CommentRepository
import com.uogames.balinasoft.core.model.comment.Comment
import com.uogames.balinasoft.test.database.dao.CommentDAO
import com.uogames.balinasoft.test.database.map.CommentMap.toDTO
import com.uogames.balinasoft.test.database.map.CommentMap.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CommentRepositoryImpl(
    private val dao: CommentDAO
): CommentRepository {
    override suspend fun saveComments(vararg comments: Comment) {
        dao.save(*comments.map { it.toEntity() }.toTypedArray())
    }

    override suspend fun clearByImageID(imageID: Int) {
        dao.deleteByImageID(imageID)
    }

    override suspend fun clear() {
        dao.clear()
    }

    override suspend fun deleteById(id: Int) {
        dao.deleteByID(id)
    }

    override fun getListFlow(imageID: Int): Flow<List<Comment>> {
        return dao.getListFlow(imageID).map { list -> list.map { it.toDTO() } }
    }

    override suspend fun deleteInstead(ids: List<Int>, page: Int, imageID: Int) {
        dao.deleteInsteadOnPageAndImageId(ids, page, imageID)
    }

    override suspend fun deleteOnPage(page: Int) {
        dao.deleteOnPage(page)
    }
}