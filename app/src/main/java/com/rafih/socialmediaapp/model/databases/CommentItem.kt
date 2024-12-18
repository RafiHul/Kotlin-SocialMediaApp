package com.rafih.socialmediaapp.model.databases

data class CommentItem(
    val comments_by_user: CommentsUserData,
    val id: Int,
    val post_id: Int,
    val text: String,
    val times: String,
    val user_id: Int
)