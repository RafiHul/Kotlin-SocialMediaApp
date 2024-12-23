package com.rafih.socialmediaapp.model.response

import com.rafih.socialmediaapp.model.databases.Comment

data class MsgDataComment(
    val status: String,
    val message: String,
    val data: Comment
)
