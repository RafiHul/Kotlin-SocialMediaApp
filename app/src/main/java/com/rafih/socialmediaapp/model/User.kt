package com.rafih.socialmediaapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val username: String,
    val password: String
): Parcelable