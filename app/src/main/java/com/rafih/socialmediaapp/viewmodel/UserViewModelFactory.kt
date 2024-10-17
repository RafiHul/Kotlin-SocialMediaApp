package com.rafih.socialmediaapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rafih.socialmediaapp.repository.UserRepository

class UserViewModelFactory(val app: Application, val repository:UserRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(app,repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")

    }
}