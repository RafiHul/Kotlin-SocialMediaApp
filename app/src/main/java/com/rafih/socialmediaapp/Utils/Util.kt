package com.rafih.socialmediaapp.Utils

import android.content.ContentResolver
import android.net.Uri

fun Uri.toByteArray(contentResolver: ContentResolver): ByteArray {
    val inputStream = contentResolver.openInputStream(this)
    return inputStream?.readBytes() ?: ByteArray(0)
}

