package com.uogames.balinasoft.test.ui.util

import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


object PermissionUtil {


    fun hasPermission(
        permission: String,
        application: Application
    ): Boolean {
        return ContextCompat.checkSelfPermission(
            application,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


}