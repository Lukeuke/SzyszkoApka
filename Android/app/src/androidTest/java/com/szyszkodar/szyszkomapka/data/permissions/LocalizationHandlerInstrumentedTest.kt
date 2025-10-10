package com.szyszkodar.szyszkomapka.data.permissions

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.maplibre.android.geometry.LatLng

@RunWith(AndroidJUnit4::class)
class LocalizationHandlerInstrumentedTest {
    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @Test
    fun testObserveUserLocation() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val handler = LocalizationHandler(context)

        val location: LatLng? = handler.getUserLocalization()

        println("localization: $location")
        assertNotNull(location)
    }

    @Test
    fun testObserveUseLocation() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val handler = LocalizationHandler(context)

        val location: LatLng? = withTimeoutOrNull(5000) {
            handler.observeUserLocation().first()
        }

        println("localization: $location")
        assertNotNull(location)
    }
}