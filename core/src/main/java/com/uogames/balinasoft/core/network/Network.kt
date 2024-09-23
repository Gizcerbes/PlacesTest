package com.uogames.balinasoft.core.network

object Network


interface NetworkProvider{

    val authProvider: AuthProvider

    val commentProvider: CommentProvider

    val imageProvider: ImageProvider

}