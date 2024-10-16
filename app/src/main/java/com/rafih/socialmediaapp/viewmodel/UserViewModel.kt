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

    private val _userData: MutableLiveData<User> = MutableLiveData()
    val userData = _userData

    private val _userJWToken: MutableLiveData<String> = MutableLiveData()
    val userJWToken = _userJWToken

    suspend fun setUserData(jwt: String, action: (User) -> Unit){
        val get = repository.getUserData("Bearer $jwt")
        if (get.isSuccessful){
            _userData.value = get.body()
        } else {
            action(get.body()!!)
        }
    }

    fun postRegisterUser(user: User,action: (Msg) -> Unit){
        viewModelScope.launch {
            _loadingApi.value = true
            try {
                val post = repository.postRegisterUser(user)
                action(post.body()!!)
            } catch (e:Exception){
                action(Msg("Failed To Connect"))
            }
            _loadingApi.value = false
        }
    }

    fun postLoginUser(context: Context,username:String,password:String,action: (MsgWithToken) -> Unit){
        viewModelScope.launch {
            try {
                _loadingApi.value = true
                val post = repository.postLoginUser(username, password)
                val postBody = post.body() ?: MsgWithToken(null, "Terjadi Kesalahan")

                if (post.isSuccessful && postBody.access_token != null) {
                    repository.setLoginData(context, postBody.access_token)
                }
                action(postBody)
                _loadingApi.value = false
            } catch (e: Exception) {
                action(MsgWithToken(null, "Failed To Connect"))
            }
        }

    }

    fun setLoadingApiTrue(){
        _loadingApi.value = false
    }

    fun setJWToken(token: String){
        _userJWToken.value = token
    }

    fun clearLoginJWT(context: Context){
        viewModelScope.launch {
            repository.clearLoginData(context)
            setJWToken("")
        }

    }

    fun getUserLoginJWT(context: Context) = repository.getLoginData(context)
}