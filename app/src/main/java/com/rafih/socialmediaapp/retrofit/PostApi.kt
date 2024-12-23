package com.rafih.socialmediaapp.retrofit

import com.rafih.socialmediaapp.model.databases.Comment
import com.rafih.socialmediaapp.model.databases.Post
import com.rafih.socialmediaapp.model.response.Msg
import com.rafih.socialmediaapp.model.response.MsgDataComment
import com.rafih.socialmediaapp.model.response.MsgDataPost
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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

    @FormUrlEncoded
    @POST("/getpostdetailbyid")
    suspend fun getPostById(
        @Field("post_id") postId: String
    ): Response<MsgDataPost>


    @FormUrlEncoded
    @POST("/deletepost")
    suspend fun deletePost(
        @Header("Authorization") jwtToken: String,
        @Field("post_id") postId: String
    ): Response<Msg>

    @FormUrlEncoded
    @POST("/getpostcomment")
    suspend fun getPostComment(
        @Field("post_id") postId: String
    ): Response<MsgDataComment>

    @FormUrlEncoded
    @POST("/getpostuser")
    suspend fun getPostUser(
        @Field("user_id") userId: String
    ): Response<Post>

    @FormUrlEncoded
    @POST("/usercomments")
    suspend fun userComment(
        @Header("Authorization") jwtToken: String,
        @Field("post_id") postId: String,
        @Field("text") text: String
    ): Response<MsgDataComment>
}