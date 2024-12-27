package com.rafih.socialmediaapp.model.response

import com.rafih.socialmediaapp.model.databases.User

data class MsgDataUser(
    val data: User?,
    val message: String,
    val status: String
)