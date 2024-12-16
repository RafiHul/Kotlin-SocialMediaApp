package com.rafih.socialmediaapp.Utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.format.DateTimeFormatter

fun Uri.toByteArray(contentResolver: ContentResolver): ByteArray {
    val inputStream = contentResolver.openInputStream(this)
    return inputStream?.readBytes() ?: ByteArray(0)
}

fun ByteArray.toBitMap(): Bitmap? {
    return BitmapFactory.decodeByteArray(this,0,this.size)
}

fun stringToImageBitmap(str: String): Bitmap? {
    val decodeBytes = Base64.decode(str, Base64.DEFAULT)
    return decodeBytes.toBitMap() //convert to bitmap
}

fun Uri.convertUriToMultiPart(contentResolver: ContentResolver): MultipartBody.Part {
    val byteArray = this.toByteArray(contentResolver) //convert image to byte
    val mimeType = contentResolver.getType(this) ?: "image/jpeg" //check image type
    val requestBody = byteArray.toRequestBody(mimeType.toMediaTypeOrNull())
    val multipartBody = MultipartBody.Part.createFormData("image", "image123.jpg", requestBody) // agar bisa di kirim ke request

    return multipartBody
}

fun getAndroidVersion(): Int {
    return android.os.Build.VERSION.SDK_INT
}

val formatterDateTime: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")