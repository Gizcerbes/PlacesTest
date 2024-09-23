package com.uogames.balinasoft.test.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.uogames.balinasoft.test.database.dao.CommentDAO
import com.uogames.balinasoft.test.database.dao.ImageDAO
import com.uogames.balinasoft.test.database.entity.CommentEntity
import com.uogames.balinasoft.test.database.entity.ImageEntity
import kotlinx.coroutines.Dispatchers

@Database(
    version = 2,
    entities = [
        ImageEntity::class,
        CommentEntity::class
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class LocationDatabase : RoomDatabase(){

    abstract val imageDAO: ImageDAO

    abstract val commentDAO: CommentDAO

    companion object {

        fun RoomDatabase.Builder<LocationDatabase>.configure() : LocationDatabase {
            return setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        }

    }

}