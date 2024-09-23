package com.uogames.balinasoft.core.database

object Database


interface DatabaseRepository{

    val commentRepository: CommentRepository

    val imageRepository: ImageRepository

}