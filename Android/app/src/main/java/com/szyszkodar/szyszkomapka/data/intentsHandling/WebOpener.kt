package com.szyszkodar.szyszkomapka.data.intentsHandling

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.szyszkodar.szyszkomapka.domain.errorHandling.Result
import com.szyszkodar.szyszkomapka.domain.intentsHandling.IntentHandler
import dagger.hilt.android.qualifiers.ApplicationContext

class WebOpener(
    @ApplicationContext context: Context
) : IntentHandler(context) {
    fun openWebPage(uri: Uri): String? {
        val intent = Intent(Intent.ACTION_VIEW, uri)

        return when(val result = openIntent(intent)) {
            is Result.Success -> null
            is Result.Error -> result.error.message
        }
    }
}