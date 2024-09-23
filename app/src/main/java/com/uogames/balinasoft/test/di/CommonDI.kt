package com.uogames.balinasoft.test.di

import android.content.Context
import com.uogames.balinasoft.test.ui.util.LocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CommonDI {

    @Singleton
    @Provides
    fun provideLocationService(
        @ApplicationContext context: Context
    ): LocationService = LocationService(context)

}