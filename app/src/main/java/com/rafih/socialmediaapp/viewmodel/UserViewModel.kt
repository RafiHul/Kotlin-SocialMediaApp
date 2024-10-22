package com.rafih.socialmediaapp.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.socialmediaapp.model.Msg
import com.rafih.socialmediaapp.model.MsgWithToken
import com.rafih.socialmediaapp.model.User
import com.rafih.socialmediaapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(val app:Application,val repository: UserRepository): AndroidViewModel(app) {
    
    private val _loadingApi: MutableLiveData<Boolean> = MutableLiveData()
    val loadingApi = _loadingApi

    private val _userData: MutableLiveData<User> = MutableLiveData()
    val userData = _userData

    private val _userJWToken: MutableLiveData<String> = MutableLiveData()
    val userJWToken = _userJWToken

    suspend fun <T> withLoading(block: suspend () -> T):T{
        _loadingApi.value = true
        return try {
            block()
        } finally {
            _loadingApi.value = false
        }
    }

    suspend fun setUserData(jwt: String,action: () -> Unit){
        withLoading {
            val get = repository.getUserData("Bearer $jwt")
            if (get.isSuccessful){
                _userData.value = get.body()
            } else {
                action()
            }
            _loadingApi.value = false
        }
    }

    suspend fun postRegisterUser(user: User,action: (Msg) -> Unit){
        withLoading {
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

    suspend fun postLoginUser(context: Context,username:String,password:String,action: (MsgWithToken) -> Unit){
        withLoading {
            try {
                _loadingApi.value = true
                val post = repository.postLoginUser(username, password)
                val postBody = post.body()!!

                if (post.isSuccessful && postBody.access_token != null) {
                    setJWToken(postBody.access_token)
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