package com.rafih.socialmediaapp.model.databases

data class UserPost(
    val UserId: Int,
    val datetime: String,
    val id: String,
    val image: String?,
    val title: String,
    val userNamePost: String,
    val userLike: Int
)