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

class UserRepository {

    var userApi: UserApi

    init {
        userApi = RetrofitInstance().getRetrofitInstance()
            .create(UserApi::class.java)
    }

    fun getUserFromApi(): LiveData<UserList> {
        val data = MutableLiveData<UserList>()

        var userList: UserList

        GlobalScope.launch(Dispatchers.IO){
            val response = userApi.getUser()

            if (response != null) {
                userList = response.body()!!

                //jika di dalam coroutine jangan menggunakan .value, tapai pakai .postValue
                data.postValue(userList)
                Log.d("data", data.value.toString())
                Log.d("response",response.message())
            }
        }
        return data
    }
}