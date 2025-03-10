package com.rafih.socialmediaapp.model.databases

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val username: String,
    val password: String,
    val profile_pic: String?,
    val img_type: String?
): Parcelable