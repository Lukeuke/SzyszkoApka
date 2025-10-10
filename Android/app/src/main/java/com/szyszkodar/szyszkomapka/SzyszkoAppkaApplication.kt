package com.szyszkodar.szyszkomapka

import android.app.Application
import android.util.Log
import com.szyszkodar.szyszkomapka.data.SessionManager
import com.szyszkodar.szyszkomapka.data.keystore.UserIdStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SzyszkoMapkaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        val userIdStore = UserIdStore(this)

        SessionManager.init{ userIdStore.getOrCreateUserId() }
    }
}