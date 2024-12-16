package com.rafih.socialmediaapp.model.databases

data class PostItem(
    val UserId: Int,
    val datetime: String,
    val id: String,
    val image: String?,
    val title: String,
    val usernamePost: String,
    val userLike: Int,
    val userProfilePicturePost : String?,
    val imageMimeType: String?
)