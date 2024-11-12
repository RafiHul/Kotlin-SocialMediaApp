package com.rafih.socialmediaapp.repository

import com.rafih.socialmediaapp.model.databases.Post
import com.rafih.socialmediaapp.model.response.Msg
import com.rafih.socialmediaapp.retrofit.RetrofitInstance
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class PostRepository {
    val postService = RetrofitInstance.getPostService

    suspend fun getPost(): Response<Post> {
        return postService.getPost()
    }

    suspend fun userNewPost(jwtToken: String, title: RequestBody, image: MultipartBody.Part?): Response<Msg> {
        return postService.userNewPost(jwtToken,title,image)
    }
}