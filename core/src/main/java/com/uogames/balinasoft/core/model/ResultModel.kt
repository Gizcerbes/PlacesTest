package com.uogames.balinasoft.core.model

sealed interface ResultModel<T> {
    class Data<T>(val obj: T): ResultModel<T>
    class Error<T>(val exception: Exception): ResultModel<T>
}