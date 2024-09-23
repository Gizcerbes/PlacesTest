package com.uogames.balinasoft.test.database

import android.content.Context
import androidx.room.Room
import com.uogames.balinasoft.core.database.CommentRepository
import com.uogames.balinasoft.core.database.Database
import com.uogames.balinasoft.core.database.DatabaseRepository
import com.uogames.balinasoft.core.database.ImageRepository
import com.uogames.balinasoft.test.database.LocationDatabase.Companion.configure
import com.uogames.balinasoft.test.database.repository.CommentRepositoryImpl
import com.uogames.balinasoft.test.database.repository.ImageRepositoryImpl


fun Database.create(
    context: Context,
    dbPath: String
): DatabaseRepository {
    return object : LocationRepository(
        Room.databaseBuilder<LocationDatabase>(
            context = context,
            name = dbPath,
        ).configure()
    ) {}
}

abstract class LocationRepository(
    database: LocationDatabase
) : DatabaseRepository {

    override val imageRepository: ImageRepository = ImageRepositoryImpl(database.imageDAO)

    override val commentRepository: CommentRepository = CommentRepositoryImpl(database.commentDAO)

}