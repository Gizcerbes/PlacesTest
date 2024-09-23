package com.uogames.balinasoft.test.ui.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocationService private constructor(
    private val client: FusedLocationProviderClient
) {

    companion object {
        operator fun invoke(context: Context): LocationService {
            return LocationService(LocationServices.getFusedLocationProviderClient(context))
        }
    }

    private val _isStarted = MutableStateFlow(false)


    private val _location = MutableStateFlow(Location())
    val location = _location.asStateFlow()

    data class Location(
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val accuracy: Float = Float.MAX_VALUE,
        val bearing: Float = 0f,
        val altitude: Double = 0.0,
        val bearingAccuracyDegrees: Float = 0f,
        val elapsedRealtimeNanos: Long = 0L,
        val speed: Float = 0f
    )

    @SuppressLint("MissingPermission")
    fun startService() {
        if (_isStarted.value) return
        val locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    _location.value = Location(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        accuracy = location.accuracy,
                        bearing = location.bearing,
                        altitude = location.altitude,
                        bearingAccuracyDegrees = location.bearingAccuracyDegrees,
                        elapsedRealtimeNanos = location.elapsedRealtimeNanos,
                        speed = location.speed
                    )
                }
            }
        }
        client.requestLocationUpdates(
            getRequest(),
            locationCallBack,
            Looper.getMainLooper()
        )
        _isStarted.value = true
    }

    private fun getRequest() = LocationRequest.Builder(
        Priority.PRIORITY_BALANCED_POWER_ACCURACY, 1000,
    ).build()

}