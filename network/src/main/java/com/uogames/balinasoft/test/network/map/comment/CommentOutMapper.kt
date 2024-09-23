package com.uogames.balinasoft.test.network.map.comment

import com.uogames.balinasoft.core.model.comment.Comment
import com.uogames.balinasoft.test.network.dto.comment.CommentDtoOut
import com.uogames.balinasoft.test.network.map.MapperModel

class CommentOutMapper(
    private val imageID: Int,
    private val page: Int
) : MapperModel<CommentDtoOut, Comment> {

    override fun CommentDtoOut.toModel(): Comment = Comment(
        id = id,
        date = date,
        text = text,
        imageID = imageID,
        page = page
    )

}