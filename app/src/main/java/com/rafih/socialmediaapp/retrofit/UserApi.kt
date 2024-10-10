package com.rafih.socialmediaapp.retrofit

import com.rafih.socialmediaapp.model.User
import com.rafih.socialmediaapp.model.UserList
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @GET("/userdata")
    suspend fun getUser(): Response<UserList>

    @FormUrlEncoded
    @POST("/userdata")
    suspend fun postUser(
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("email") email: String
    ): Response<User>
}