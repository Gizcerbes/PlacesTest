package com.uogames.balinasoft.test.ui.fragment.home

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uogames.balinasoft.core.usecase.image.ImageUploadUseCase
import com.uogames.balinasoft.test.Config
import com.uogames.balinasoft.test.R
import com.uogames.balinasoft.test.ui.util.ImageLoader
import com.uogames.balinasoft.test.ui.util.ImageLoader.changeSize
import com.uogames.balinasoft.test.ui.util.ImageLoader.compressToSize
import com.uogames.balinasoft.test.ui.util.LocalDateTimeExchange.currentEpochMilliseconds
import com.uogames.balinasoft.test.ui.util.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val uploadUseCase: ImageUploadUseCase,
    private val locationService: LocationService
) : ViewModel() {

    val navVisibility = MutableStateFlow(false)

    val selectedItem = MutableStateFlow(R.id.photos)

    val mso = MutableStateFlow<Bitmap?>(null)

    private val _errorHandler = MutableSharedFlow<Exception>()
    val errorHandler = _errorHandler.asSharedFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading = _isUploading.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress = _progress.asStateFlow()

    val username = Config.username.asStateFlow()


    fun load(activity: Activity, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            _isUploading.value = true
            val image = ImageLoader.load(activity, uri)
            val resized = image.changeSize(1280)
            val compressed = resized.compressToSize(ImageUploadUseCase.preferredImageSize)
            val loc = locationService.location.value
            uploadUseCase(
                image = compressed,
                date = LocalDateTime.currentEpochMilliseconds() / 1000,
                lat = loc.latitude,
                lng = loc.longitude,
                onProgress = { _progress.value = it }
            )
            _isUploading.value = false
        }
    }
}


