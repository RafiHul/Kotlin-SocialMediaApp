package com.rafih.socialmediaapp.Utils

import android.content.ContentResolver
import android.net.Uri
import android.util.Log

fun Uri.toByteArray(contentResolver: ContentResolver): ByteArray {
    val inputStream = contentResolver.openInputStream(this)
    Log.d("bytearray util",inputStream?.readBytes().toString())
    return inputStream?.readBytes() ?: ByteArray(0)
}

