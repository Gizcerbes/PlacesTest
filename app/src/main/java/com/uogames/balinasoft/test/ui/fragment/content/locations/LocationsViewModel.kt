package com.uogames.balinasoft.test.ui.fragment.content.locations

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uogames.balinasoft.core.database.DatabaseRepository
import com.uogames.balinasoft.core.model.StatusModel
import com.uogames.balinasoft.core.usecase.image.ImageDeleteUseCase
import com.uogames.balinasoft.core.usecase.image.ImageUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    databaseRepository: DatabaseRepository,
    private val imageUpdateUseCase: ImageUpdateUseCase,
    private val imageDeleteUseCase: ImageDeleteUseCase
) : ViewModel() {

    val imageListFlow = databaseRepository.imageRepository.getListFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _errorFlow = MutableSharedFlow<Exception>()
    val errorFlow = _errorFlow.asSharedFlow()

    private val _isUpdating = MutableStateFlow(false)
    private val _isDeleting = MutableStateFlow(false)

    val isBusy = _isUpdating.asStateFlow()

    var recyclerStat: Parcelable? = null

    val adapter = LocationsAdapter(
        callSize = { runCatching { imageListFlow.value.size }.getOrNull() ?: 0 },
        callItem = { runCatching { imageListFlow.value.getOrNull(it) }.getOrNull() }
    )

    private var updateJob: Job? = null
    private var deleteJob: Job? = null


    init {
        refresh()
    }

    fun refresh() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            _isUpdating.value = true
            var page = 0
            while (true) {
                when (val r = imageUpdateUseCase(page)) {
                    is StatusModel.Error -> {
                        when (r.exception) {
                            is ImageUpdateUseCase.GotEmpty -> break
                            else -> {
                                _errorFlow.emit(r.exception)
                                break
                            }
                        }
                    }

                    StatusModel.OK -> page++
                }
            }
            _isUpdating.value = false
        }
    }

    fun delete(id: Int) {
        deleteJob?.cancel()
        deleteJob = viewModelScope.launch {
            _isDeleting.value = true
            when (val r = imageDeleteUseCase(id)) {
                StatusModel.OK -> {}
                is StatusModel.Error -> {
                    _errorFlow.emit(r.exception)
                }
            }
            _isDeleting.value = false
        }
    }


}