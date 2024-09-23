package com.uogames.balinasoft.test.di

import android.content.Context
import com.uogames.balinasoft.core.database.Database
import com.uogames.balinasoft.core.database.DatabaseRepository
import com.uogames.balinasoft.test.database.create
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseDI {


    private val dbName: String = "locations.db"

    @Singleton
    @Provides
    fun provideDatabaseProvider(
        @ApplicationContext context: Context
    ): DatabaseRepository = Database.create(context, context.getDatabasePath(dbName).absolutePath)



}