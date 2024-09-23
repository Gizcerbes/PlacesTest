package com.uogames.balinasoft.core.model


sealed interface StatusModel {
    object OK: StatusModel
    class Error(val exception: Exception): StatusModel
}