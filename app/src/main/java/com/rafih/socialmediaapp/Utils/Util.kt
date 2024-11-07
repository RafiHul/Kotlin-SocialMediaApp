package com.rafih.socialmediaapp.Utils

import android.content.ContentResolver
import android.net.Uri
import android.util.Base64

fun Uri.toByteArray(contentResolver: ContentResolver): ByteArray {
    val inputStream = contentResolver.openInputStream(this)
    return inputStream?.readBytes() ?: ByteArray(0)
}

fun String.decodeToByteArray(): ByteArray {
    return Base64.decode(this, Base64.DEFAULT)
}
