package com.rafih.socialmediaapp.model.databases

data class UserPostItem(
    val UserId: Int,
    val datetime: String,
    val id: String,
    val image: String?,
    val title: String,
    val usernamePost: String,
    val userLike: Int,
    val userProfilePicturePost : String?
)