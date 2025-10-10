package com.szyszkodar.szyszkomapka.data.keystore

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlin.getValue
import androidx.core.content.edit
import java.util.UUID

@Suppress("DEPRECATION")
class UserIdStore(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "secure_user_prefs"
        private const val KEY_USER_ID = "x_user_id"
    }

    private val prefs by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getOrCreateUserId(): String {
        synchronized(this) {
            val existing = prefs.getString(KEY_USER_ID, null)
            if (!existing.isNullOrBlank()) return existing

            val newId = UUID.randomUUID().toString()
            prefs.edit { putString(KEY_USER_ID, newId) }
            return newId
        }
    }
}