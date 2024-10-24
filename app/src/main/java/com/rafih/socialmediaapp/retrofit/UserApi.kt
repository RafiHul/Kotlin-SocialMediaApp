package com.rafih.socialmediaapp.retrofit

import com.rafih.socialmediaapp.model.Msg
import com.rafih.socialmediaapp.model.MsgWithToken
import com.rafih.socialmediaapp.model.User
import com.rafih.socialmediaapp.model.UserList
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {

    @FormUrlEncoded
    @POST("/register")
    suspend fun postRegisterUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<Msg>

    @FormUrlEncoded
    @POST("/login")
    suspend fun postLoginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<MsgWithToken>

    @GET("/getuserdata")
    suspend fun getUserData(
        @Header("Authorization") jwt: String
    ): Response<User>

    @FormUrlEncoded
    @POST("/changeprofileemail")
    suspend fun changeProfileEmail(
        @Header("Authorization") jwt_token: String,
        @Field("email") email: String
    ): Response<User>
}