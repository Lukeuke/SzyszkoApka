package com.szyszkodar.szyszkomapka.domain.intentsHandling

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import com.szyszkodar.szyszkomapka.domain.errorHandling.IntentError
import com.szyszkodar.szyszkomapka.domain.errorHandling.Result

abstract class IntentHandler(protected val context: Context) {
    protected fun openIntent(intent: Intent): Result<Unit, IntentError> {
        return try {
            context.startActivity(intent)

            Result.Success(Unit)
        } catch (e: ActivityNotFoundException) {
            Result.Error(IntentError.ACTIVITY_NOT_FOUND)
        } catch (e: SecurityException) {
            Result.Error(IntentError.SECURITY_EXCEPTION)
        } catch (e: NullPointerException) {
            Result.Error(IntentError.NULL_POINTER_EXCEPTION)
        } catch (e: IllegalStateException) {
            Result.Error(IntentError.ILLEGAL_ARGUMENT_EXCEPTION)
        } catch (e: Exception) {
            Result.Error(IntentError.UNKNOWN)
        }
    }
}