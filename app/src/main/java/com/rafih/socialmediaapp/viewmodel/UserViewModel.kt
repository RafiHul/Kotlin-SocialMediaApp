package com.rafih.socialmediaapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.socialmediaapp.model.User
import com.rafih.socialmediaapp.model.UserList
import com.rafih.socialmediaapp.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {
    private val repository = UserRepository()

    private val _users = MutableLiveData<UserList>()
    val users: LiveData<UserList> = _users

    fun setUser(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val us = repository.getUserApi()
                if (us.isSuccessful && us.body() != null) {
                    Log.d("TAG", "Data: ${us.body()}")
                    _users.postValue(us.body())
                } else {
                    Log.e("TAG", "Error: ${us.message()}")
                }
            } catch (e: Exception) {
                Log.e("TAG", "Exception: ${e.message}")
            }
        }
    }

    fun postUser(user: User){
        viewModelScope.launch {
            try {
                val post = repository.postUserApi(user)
                if (post.isSuccessful) {
                    Log.d("Berhasil", "Data: ${post.body()}")
                    _users.value?.add(post.body()!!)
                } else {
                    Log.e("Gagal", "Error: ${post.message()}")
                }
            } catch (e:Exception){
                Log.d("Exeption", "Exception: ${e.message}")
            }
        }
    }
}