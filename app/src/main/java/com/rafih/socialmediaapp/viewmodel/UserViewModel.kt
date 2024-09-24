package com.rafih.socialmediaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rafih.socialmediaapp.model.UserList
import com.rafih.socialmediaapp.repository.UserRepository

class UserViewModel: ViewModel() {
    private val repository = UserRepository()

    lateinit var userLiveData : LiveData<UserList>

    init {
        userLiveData = repository.getUserFromApi()
    }

    fun getUserLiveData(): LiveData<UserList> {
        return userLiveData
    }
}