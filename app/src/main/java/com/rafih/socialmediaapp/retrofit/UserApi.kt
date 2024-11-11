package com.rafih.socialmediaapp.retrofit

import com.rafih.socialmediaapp.model.response.Msg
import com.rafih.socialmediaapp.model.response.MsgData
import com.rafih.socialmediaapp.model.response.MsgWithToken
import com.rafih.socialmediaapp.model.databases.User
import com.rafih.socialmediaapp.model.databases.UserPost
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
    @POST("/changeprofile")
    suspend fun changeProfile(
        @Header("Authorization") jwtToken: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("email") email: String
    ): Response<User>

    @FormUrlEncoded
    @POST("/changepassword")
    suspend fun changePassword(
        @Header("Authorization") jwtToken: String,
        @Field("newPassword") newPassword: String
    ): Response<Msg>

    @Multipart
    @POST("/changeprofilepic")
    suspend fun changeProfilePic(
        @Header("Authorization") jwtToken: String,
        @Part image: MultipartBody.Part
    ): Response<Msg>

    @GET("/getprofilepic")
    suspend fun getProfilePic(
        @Header("Authorization") jwtToken: String,
    ): Response<MsgData>

    @GET("/getUserPost")
    suspend fun getUserPost(): Response<UserPost>
}