package com.uogames.balinasoft.test

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.flow.MutableStateFlow

object Config {

    private const val SP_NAME = "SP_NAME"
    private const val ACCESS_TOKEN = "ACCESS_TOKEN"
    private const val USER_ID = "USER_ID"
    private const val USERNAME = "USERNAME"


    val accessToken = MutableStateFlow<String?>(null)
    val userID = MutableStateFlow<Int?>(null)
    val username = MutableStateFlow<String?>(null)

    fun save(context: Context){
        val sp = getEncryptedSharedPreferences(context)
        val editor = sp.edit()
        editor.putString(ACCESS_TOKEN, accessToken.value)
        editor.putInt(USER_ID, userID.value ?: -1)
        editor.putString(USERNAME, username.value)
        editor.apply()
    }

    operator fun invoke(context: Context){
        val sp = getEncryptedSharedPreferences(context)
        accessToken.value = sp.getString(ACCESS_TOKEN, null)
        userID.value = sp.getInt(USER_ID, -1)
        if (userID.value == -1) userID.value = null
        username.value = sp.getString(USERNAME, null)

    }


    private fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
        val key = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            SP_NAME,
            key,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

}