package com.rafih.socialmediaapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafih.socialmediaapp.model.UserList
import com.rafih.socialmediaapp.retrofit.RetrofitInstance
import com.rafih.socialmediaapp.retrofit.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class UserRepository {
    val userServices = RetrofitInstance.getUserService

    suspend fun getUserApi(): Response<UserList> {
        return userServices.getUser()
    }
}