package com.uogames.balinasoft.test

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Config(this)
        MapKitFactory.setApiKey("c54d044e-a274-4e29-a6a0-4100ed7c19b4")
        MapKitFactory.initialize(this)
        super.onCreate()
    }

}