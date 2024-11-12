package com.rafih.socialmediaapp.retrofit

import com.rafih.socialmediaapp.model.databases.Post
import com.rafih.socialmediaapp.model.response.Msg
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PostApi {
    @GET("/getpost")
    suspend fun getPost(): Response<Post>

    @Multipart
    @POST("/usernewpost")
    suspend fun userNewPost(
        @Header("Authorization") jwtToken: String,
        @Part("title") title: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<Msg>
}