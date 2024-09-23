package com.uogames.balinasoft.test.network.provider

import com.uogames.balinasoft.core.model.comment.Comment
import com.uogames.balinasoft.core.network.CommentProvider
import com.uogames.balinasoft.core.network.model.ResponseModel
import com.uogames.balinasoft.core.network.model.comment.CommentModelIn
import com.uogames.balinasoft.test.network.map.ResponseDtoMapper
import com.uogames.balinasoft.test.network.map.comment.CommentInMapper
import com.uogames.balinasoft.test.network.map.comment.CommentOutMapper
import com.uogames.balinasoft.test.network.service.CommentService

class CommentProviderImpl(
    private val service: CommentService
) : CommentProvider {

    private val responseMapper = ResponseDtoMapper()
    private val commentDTOMapper = CommentInMapper()

    override suspend fun get(imageID: Int, page: Int): ResponseModel<List<Comment>> {
        return responseMapper.run { service.get(imageID, page).toListModel(CommentOutMapper(imageID, page)) }
    }

    override suspend fun post(dtoIn: CommentModelIn, imageID: Int): ResponseModel<Comment> {
        val dto = commentDTOMapper.run { dtoIn.toDTO() }
        return responseMapper.run { service.post(imageID, dto).toModel(CommentOutMapper(imageID, Int.MAX_VALUE)) }
    }

    override suspend fun delete(commentID: Int, imageID: Int): ResponseModel<Comment> {
        return responseMapper.run { service.delete(imageID,commentID).toModel(CommentOutMapper(imageID, Int.MAX_VALUE)) }
    }
}