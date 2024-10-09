package com.rafih.socialmediaapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}