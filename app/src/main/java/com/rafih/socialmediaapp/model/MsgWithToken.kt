package com.rafih.socialmediaapp.model

data class MsgWithToken(
    val status: String,
    val access_token: String,
    val msg: String
)