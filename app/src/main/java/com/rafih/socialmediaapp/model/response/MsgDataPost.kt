package com.rafih.socialmediaapp.model.response

import com.rafih.socialmediaapp.model.databases.PostItem

data class MsgDataPost(
    val data: PostItem,
    val message: String,
    val status: String
)