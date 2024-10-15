package com.rafih.socialmediaapp.viewmodel

import android.content.Context
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

    fun postRegisterUser(user: User){
        viewModelScope.launch {
            try {
                val post = repository.postRegisterUser(user)
                if (post.isSuccessful) {
                    Log.d("Berhasil", "Success Membuat Akun")
                } else {
                    Log.e("Gagal", "Error: ${post.message()}")
                }
            } catch (e:Exception){
                Log.e("Exeption", "Exception: ${e.message}")
            }
        }
    }

    fun postLoginUser(context: Context,username:String,password:String){
        viewModelScope.launch {
            val post = repository.postLoginUser(username,password)
            if (post.isSuccessful && post.body() != null){
                repository.setLoginData(context, post.body()!!.access_token)
                Log.d("Berhasil", "Success Login")
            } else {
                Log.e("Gagal", "Error: ${post.message()}")
            }
        }
    }

    fun getUserLoginData(context: Context) = repository.getLoginData(context)
}