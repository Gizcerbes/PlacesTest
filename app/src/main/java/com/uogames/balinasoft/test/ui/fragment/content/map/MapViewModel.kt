package com.uogames.balinasoft.test.ui.fragment.content.map

import androidx.lifecycle.ViewModel
import com.uogames.balinasoft.core.database.DatabaseRepository
import com.uogames.balinasoft.test.ui.util.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationService: LocationService,
    private val databaseRepository: DatabaseRepository
): ViewModel() {

    val points = databaseRepository.imageRepository.getListFlow()

    val location = locationService.location


}