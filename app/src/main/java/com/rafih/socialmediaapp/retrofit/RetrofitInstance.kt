package com.rafih.socialmediaapp.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val baseUrl = "http://192.168.1.7:8000"
    private val gson = GsonBuilder().setLenient().create()
    private val retrofit: Retrofit by lazy {
       Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val getUserService: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }
}