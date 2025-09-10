package com.szyszkodar.szyszkomapka.presentation.shared.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Filter: ImageVector
    get() {
        if (_Filter != null) return _Filter!!

        _Filter = ImageVector.Builder(
            name = "Filter_alt",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(440f, 800f)
                quadToRelative(-17f, 0f, -28.5f, -11.5f)
                reflectiveQuadTo(400f, 760f)
                verticalLineToRelative(-240f)
                lineTo(168f, 224f)
                quadToRelative(-15f, -20f, -4.5f, -42f)
                reflectiveQuadToRelative(36.5f, -22f)
                horizontalLineToRelative(560f)
                quadToRelative(26f, 0f, 36.5f, 22f)
                reflectiveQuadToRelative(-4.5f, 42f)
                lineTo(560f, 520f)
                verticalLineToRelative(240f)
                quadToRelative(0f, 17f, -11.5f, 28.5f)
                reflectiveQuadTo(520f, 800f)
                close()
                moveToRelative(40f, -308f)
                lineToRelative(198f, -252f)
                horizontalLineTo(282f)
                close()
                moveToRelative(0f, 0f)
            }
        }.build()

        return _Filter!!
    }

private var _Filter: ImageVector? = null

