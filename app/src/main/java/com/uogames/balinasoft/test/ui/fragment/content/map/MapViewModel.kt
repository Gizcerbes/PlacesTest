package com.uogames.balinasoft.test.ui.fragment.content.map

import androidx.lifecycle.ViewModel
import com.uogames.balinasoft.core.database.DatabaseRepository
import com.uogames.balinasoft.test.ui.util.LocationService
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    locationService: LocationService,
    databaseRepository: DatabaseRepository
) : ViewModel() {

    val points = databaseRepository.imageRepository.getListFlow()

    val location = locationService.location

    val defaultPosition = MutableStateFlow(true)

    val selected = MutableStateFlow<CameraPosition?>(null)


    fun getMyPosition() = CameraPosition(
        Point(location.value.latitude, location.value.longitude),
        16f,
        0f,
        30f
    )

}