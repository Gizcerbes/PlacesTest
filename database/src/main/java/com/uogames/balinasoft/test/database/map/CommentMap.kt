package com.uogames.balinasoft.test.database.map

typealias CommentEntity = com.uogames.balinasoft.test.database.entity.CommentEntity
typealias CommentDTO = com.uogames.balinasoft.core.model.comment.Comment

object CommentMap{

    fun CommentEntity.toDTO() = CommentDTO(
        id = id,
        date = date,
        text = text,
        imageID = imageID,
        page = page
    )

    fun CommentDTO.toEntity() = CommentEntity(
        id = id,
        imageID = imageID,
        date = date,
        text = text,
        page = page
    )

}