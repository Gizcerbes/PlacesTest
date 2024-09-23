package com.uogames.balinasoft.test.ui.fragment.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uogames.balinasoft.core.model.ResultModel
import com.uogames.balinasoft.core.usecase.auth.SignInUseCase
import com.uogames.balinasoft.core.usecase.auth.SignUpUseCase
import com.uogames.balinasoft.test.Config
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModule @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    val selectedTab = MutableStateFlow(0)

    val commonError = MutableStateFlow("")

    val login = MutableStateFlow("")
    val loginError = MutableStateFlow<String?>(null)

    val password = MutableStateFlow("")
    val passwordError = MutableStateFlow<String?>(null)

    val repeatPassword = MutableStateFlow("")
    val repeatPasswordError =  MutableStateFlow<String?>(null)

    private val _errorHandler = MutableSharedFlow<Exception>()
    val errorHandler = _errorHandler.asSharedFlow()

    private val _signIn = MutableStateFlow(false)
    private val _signUp = MutableStateFlow(false)

    val busy = _signIn.combine(_signUp) { f, s -> f || s }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private var signInJob: Job? = null

    init {
        login.onEach {
            loginError.value = null
            commonError.value = ""
        }.launchIn(viewModelScope)
        password.onEach {
            passwordError.value = null
            commonError.value = ""
        }.launchIn(viewModelScope)
        repeatPassword.onEach {
            repeatPasswordError.value = null
            commonError.value = ""
        }.launchIn(viewModelScope)

    }

    fun signIn() {
        signInJob?.cancel()
        signInJob = viewModelScope.launch {
            _signIn.value = true
            val r = signInUseCase(
                login = login.value.trim(),
                password = password.value.trim()
            )
            when (r) {
                is ResultModel.Data -> {
                    Config.accessToken.value = r.obj.token
                    Config.username.value = r.obj.login
                    Config.userID.value = r.obj.userID
                }
                is ResultModel.Error -> _errorHandler.emit(r.exception)
            }
            _signIn.value = false
        }

    }

    fun signUp() {
        signInJob?.cancel()
        signInJob = viewModelScope.launch {
            _signUp.value = true
            val r = signUpUseCase(
                login = login.value.trim(),
                password = password.value.trim(),
                passwordRepeat = repeatPassword.value.trim()
            )
            when (r) {
                is ResultModel.Data -> {
                    Config.accessToken.value = r.obj.token
                    Config.username.value = r.obj.login
                    Config.userID.value = r.obj.userID
                }
                is ResultModel.Error -> _errorHandler.emit(r.exception)
            }
            _signUp.value = false
        }

    }


}