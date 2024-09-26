package com.rafih.socialmediaapp.retrofit

import com.rafih.socialmediaapp.model.UserList
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {
    @GET("socialmediaappapi.php")
    suspend fun getUser(): Response<UserList>
}