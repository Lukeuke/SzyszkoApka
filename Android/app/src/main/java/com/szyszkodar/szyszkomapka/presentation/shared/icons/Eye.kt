package com.szyszkodar.szyszkomapka.presentation.shared.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Eye: ImageVector
    get() {
        if (_Eye != null) return _Eye!!

        _Eye = ImageVector.Builder(
            name = "Eye",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(7.99993f, 6.00316f)
                curveTo(9.47266f, 6.00316f, 10.6666f, 7.19708f, 10.6666f, 8.66981f)
                curveTo(10.6666f, 10.1426f, 9.47266f, 11.3365f, 7.99993f, 11.3365f)
                curveTo(6.52715f, 11.3365f, 5.33324f, 10.1426f, 5.33324f, 8.66981f)
                curveTo(5.33324f, 7.19708f, 6.52715f, 6.00316f, 7.99993f, 6.00316f)
                close()
                moveTo(7.99993f, 7.00315f)
                curveTo(7.07946f, 7.00315f, 6.33324f, 7.74935f, 6.33324f, 8.66981f)
                curveTo(6.33324f, 9.59028f, 7.07946f, 10.3365f, 7.99993f, 10.3365f)
                curveTo(8.9204f, 10.3365f, 9.6666f, 9.59028f, 9.6666f, 8.66981f)
                curveTo(9.6666f, 7.74935f, 8.9204f, 7.00315f, 7.99993f, 7.00315f)
                close()
                moveTo(7.99993f, 3.66675f)
                curveTo(11.0756f, 3.66675f, 13.7307f, 5.76675f, 14.4673f, 8.70968f)
                curveTo(14.5344f, 8.97755f, 14.3716f, 9.24908f, 14.1037f, 9.31615f)
                curveTo(13.8358f, 9.38315f, 13.5643f, 9.22041f, 13.4973f, 8.95248f)
                curveTo(12.8713f, 6.45205f, 10.6141f, 4.66675f, 7.99993f, 4.66675f)
                curveTo(5.38454f, 4.66675f, 3.12664f, 6.45359f, 2.50182f, 8.95555f)
                curveTo(2.43491f, 9.22341f, 2.16348f, 9.38635f, 1.89557f, 9.31948f)
                curveTo(1.62766f, 9.25255f, 1.46471f, 8.98115f, 1.53162f, 8.71321f)
                curveTo(2.26701f, 5.76856f, 4.9229f, 3.66675f, 7.99993f, 3.66675f)
                close()
            }
        }.build()

        return _Eye!!
    }

private var _Eye: ImageVector? = null

