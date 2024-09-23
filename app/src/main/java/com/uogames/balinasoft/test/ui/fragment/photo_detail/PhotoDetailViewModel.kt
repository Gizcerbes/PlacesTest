package com.uogames.balinasoft.test.ui.fragment.photo_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uogames.balinasoft.core.database.DatabaseRepository
import com.uogames.balinasoft.core.model.StatusModel
import com.uogames.balinasoft.core.usecase.comment.CommentDeleteUseCase
import com.uogames.balinasoft.core.usecase.comment.CommentSendUseCase
import com.uogames.balinasoft.core.usecase.comment.CommentUpdateOnPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    databaseRepository: DatabaseRepository,
    private val updateOnPageUseCase: CommentUpdateOnPageUseCase,
    private val commentSendUseCase: CommentSendUseCase,
    private val commentDeleteUseCase: CommentDeleteUseCase
) : ViewModel() {

    val imageID = MutableStateFlow(0)
    val message = MutableStateFlow("")

    val image = imageID.map {
        databaseRepository.imageRepository.getByID(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val comments = imageID.flatMapLatest {
        databaseRepository.commentRepository.getListFlow(it)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _isRefreshing = MutableStateFlow(false)

    private val _isSending = MutableStateFlow(false)

    private val _isDeleting = MutableStateFlow(false)

    val busy = _isRefreshing
        .combine(_isSending) { f, s -> f || s }
        .combine(_isDeleting) { f, s -> f || s }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val _errorHandler = MutableSharedFlow<Exception>()
    val errorHandler = _errorHandler.asSharedFlow()

    private val _onClearTextField = MutableSharedFlow<Unit>()
    val onClearTextField = _onClearTextField.asSharedFlow()

    val adapter = PhotoDetailAdapter(
        callSize = { runCatching { comments.value.size }.getOrNull() ?: 0 },
        callItem = { runCatching { comments.value[it] }.getOrNull() }
    )

    private var refreshJob: Job? = null
    private var sendingJob: Job? = null
    private var deletingJob: Job? = null

    init {
        imageID.onEach {
            viewModelScope.launch { refreshWork() }
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            refreshWork()
        }
    }

    private suspend fun refreshWork() {
        _isRefreshing.value = true
        var page = 0
        while (true) {
            val r = updateOnPageUseCase(imageID = imageID.value, page)
            when (r) {
                StatusModel.OK -> page++
                is StatusModel.Error -> {
                    when (r.exception) {
                        is CommentUpdateOnPageUseCase.NotFound -> break
                        is CommentUpdateOnPageUseCase.GotEmpty -> break
                        is CancellationException -> break
                        else -> {
                            _errorHandler.emit(r.exception)
                            break
                        }
                    }
                }
            }
        }
        _isRefreshing.value = false
    }

    fun sendComment() {
        sendingJob?.cancel()
        sendingJob = viewModelScope.launch {
            _isSending.value = true
            when (val r = commentSendUseCase(imageID.value, message.value)) {
                StatusModel.OK -> {
                    message.value = ""
                    _onClearTextField.emit(Unit)
                }

                is StatusModel.Error -> _errorHandler.emit(r.exception)
            }
            _isSending.value = false
        }
    }

    fun deleteComment(id: Int) {
        deletingJob?.cancel()
        deletingJob = viewModelScope.launch {
            _isDeleting.value = true
            when (val r = commentDeleteUseCase(imageID.value, id)) {
                StatusModel.OK -> {}
                is StatusModel.Error -> {
                    _errorHandler.emit(r.exception)
                }
            }
            _isDeleting.value = false
        }
    }


}