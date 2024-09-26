package com.rafih.socialmediaapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val baseUrl = "http://192.168.1.7/socialmediaapp/"

    private val retrofit: Retrofit by lazy {
       Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getUserService: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }
}