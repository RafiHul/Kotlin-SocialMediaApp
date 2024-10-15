package com.rafih.socialmediaapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.socialmediaapp.model.Msg
import com.rafih.socialmediaapp.model.MsgWithToken
import com.rafih.socialmediaapp.model.User
import com.rafih.socialmediaapp.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(val repository: UserRepository): ViewModel() {
    
    private val _loadingApi: MutableLiveData<Boolean> = MutableLiveData()
    val loadingApi = _loadingApi
    
    fun postRegisterUser(user: User,action: (Msg) -> Unit){
        viewModelScope.launch {
            _loadingApi.value = true
            try {
                val post = repository.postRegisterUser(user)
                if (post.isSuccessful) {
                    action(post.body()!!)
                } else {
                    action(post.body()!!)
                }
            } catch (e:Exception){
                Log.e("Exeption", "Exception: ${e.message}")
            }
            _loadingApi.value = false
        }
    }

    fun postLoginUser(context: Context,username:String,password:String,action: (MsgWithToken) -> Unit){
        viewModelScope.launch {
            try {
                _loadingApi.value = true
                val post = repository.postLoginUser(username, password)
                val postBody = post.body() ?: MsgWithToken(null, "")

                if (post.isSuccessful && postBody.access_token != null) {
                    repository.setLoginData(context, postBody.access_token)
                    action(postBody)
                } else {
                    action(postBody)
                }
                _loadingApi.value = false
            } catch (e: Exception) {
                Log.e("Exeption", "Exception: ${e.message}")
            }
        }

    }

    fun seuLoadingApiTrue(){
        _loadingApi.value = false
    }

    fun getUserLoginData(context: Context) = repository.getLoginData(context)
}