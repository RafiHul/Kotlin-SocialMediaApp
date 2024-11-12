package com.rafih.socialmediaapp.repository

import com.rafih.socialmediaapp.model.databases.Post
import com.rafih.socialmediaapp.retrofit.RetrofitInstance
import retrofit2.Response

class PostRepository {
    val userServices = RetrofitInstance.getUserService

    suspend fun getPost(): Response<Post> {
        return userServices.getPost()
    }
}