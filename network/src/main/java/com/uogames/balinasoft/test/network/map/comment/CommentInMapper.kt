package com.uogames.balinasoft.test.network.map.comment

import com.uogames.balinasoft.core.network.model.comment.CommentModelIn
import com.uogames.balinasoft.test.network.dto.comment.CommentDtoIn
import com.uogames.balinasoft.test.network.map.MapperDTO

class CommentInMapper : MapperDTO<CommentModelIn, CommentDtoIn> {

    override fun CommentModelIn.toDTO(): CommentDtoIn {
        return CommentDtoIn(text = text)
    }

}