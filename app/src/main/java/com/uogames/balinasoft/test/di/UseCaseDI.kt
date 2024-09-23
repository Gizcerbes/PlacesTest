package com.uogames.balinasoft.test.di

import com.uogames.balinasoft.core.database.DatabaseRepository
import com.uogames.balinasoft.core.network.NetworkProvider
import com.uogames.balinasoft.core.usecase.auth.SignInUseCase
import com.uogames.balinasoft.core.usecase.auth.SignUpUseCase
import com.uogames.balinasoft.core.usecase.comment.CommentDeleteUseCase
import com.uogames.balinasoft.core.usecase.comment.CommentSendUseCase
import com.uogames.balinasoft.core.usecase.comment.CommentUpdateOnPageUseCase
import com.uogames.balinasoft.core.usecase.image.ImageDeleteUseCase
import com.uogames.balinasoft.core.usecase.image.ImageUpdateUseCase
import com.uogames.balinasoft.core.usecase.image.ImageUploadUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UseCaseDI {

    @Provides
    fun signInUseCase(
        networkProvider: NetworkProvider
    ): SignInUseCase = SignInUseCase(networkProvider.authProvider)

    @Provides
    fun signUpUseCase(
        networkProvider: NetworkProvider
    ): SignUpUseCase = SignUpUseCase(networkProvider.authProvider)

    @Provides
    fun commentDeleteUseCase(
        networkProvider: NetworkProvider,
        databaseRepository: DatabaseRepository
    ): CommentDeleteUseCase = CommentDeleteUseCase(
        databaseRepository.commentRepository,
        networkProvider.commentProvider
    )

    @Provides
    fun commentSendUseCase(
        networkProvider: NetworkProvider,
        databaseRepository: DatabaseRepository
    ): CommentSendUseCase = CommentSendUseCase(
        networkProvider.commentProvider,
        databaseRepository.commentRepository
    )

    @Provides
    fun commentUpdatePageUseCase(
        networkProvider: NetworkProvider,
        databaseRepository: DatabaseRepository
    ): CommentUpdateOnPageUseCase = CommentUpdateOnPageUseCase(
        networkProvider.commentProvider,
        databaseRepository.commentRepository
    )

    @Provides
    fun imageDeleteUseCase(
        networkProvider: NetworkProvider,
        databaseRepository: DatabaseRepository
    ): ImageDeleteUseCase = ImageDeleteUseCase(
        databaseRepository.imageRepository,
        networkProvider.imageProvider
    )

    @Provides
    fun imageUploadUseCase(
        networkProvider: NetworkProvider,
        databaseRepository: DatabaseRepository
    ): ImageUploadUseCase = ImageUploadUseCase(
        databaseRepository.imageRepository,
        networkProvider.imageProvider
    )

    @Provides
    fun imageUpdateUseCase(
        networkProvider: NetworkProvider,
        databaseRepository: DatabaseRepository
    ): ImageUpdateUseCase = ImageUpdateUseCase(
        databaseRepository.imageRepository,
        networkProvider.imageProvider
    )

}