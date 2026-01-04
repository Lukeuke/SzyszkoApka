package com.szyszkodar.szyszkomapka.presentation.bookpointInfoBottomSheet

import android.graphics.Bitmap

data class BookpointInfoBottomSheetState(
    val isLoading: Boolean = false,
    val isImageLoading: Boolean = false,
    val imageBitmap: Bitmap? = null,
    val toastMessage: String? = null
)
