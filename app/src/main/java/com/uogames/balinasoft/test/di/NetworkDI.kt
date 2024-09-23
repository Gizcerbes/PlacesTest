package com.uogames.balinasoft.test.di

import com.uogames.balinasoft.core.network.Network
import com.uogames.balinasoft.core.network.NetworkProvider
import com.uogames.balinasoft.test.Config
import com.uogames.balinasoft.test.network.create
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkDI {


    @Singleton
    @Provides
    fun provideNetworkProvider(): NetworkProvider = Network.create { Config.accessToken.value }


}