package com.uogames.balinasoft.core.usecase.auth

import com.uogames.balinasoft.core.model.ResultModel
import com.uogames.balinasoft.core.network.AuthProvider
import com.uogames.balinasoft.core.network.model.user.SignUserModelIn
import com.uogames.balinasoft.core.network.model.user.SignUserOutModel

class SignUpUseCase(
    private val authProvider: AuthProvider
) {

    class LoginRangeException : Exception()
    class PasswordRangeException : Exception()
    class LoginPatternException : Exception()
    class PasswordNotRepeat : Exception()
    class SecuritySignupLoginAlreadyUse: Exception()

    suspend operator fun invoke(
        login: String,
        password: String,
        passwordRepeat: String
    ): ResultModel<SignUserOutModel> {
        val loginRange = 4..32
        if (login.length !in loginRange) return ResultModel.Error(LoginRangeException())
        val passwordRange = 8..500
        if (password.length !in passwordRange) return ResultModel.Error(PasswordRangeException())
        if (!login.matches(Regex("[a-z0-9_\\-.@]+"))) return ResultModel.Error(
            LoginPatternException()
        )
        if (password != passwordRepeat) return ResultModel.Error(PasswordNotRepeat())
        return try {
            val r = authProvider.signUp(SignUserModelIn(login, password))
            if (r.status == 400) return ResultModel.Error(SecuritySignupLoginAlreadyUse())
            if (r.data == null) return ResultModel.Error(Exception(r.error.orEmpty()))
            else ResultModel.Data(r.data)
        } catch (e: Exception){
            ResultModel.Error(e)
        }
    }


}