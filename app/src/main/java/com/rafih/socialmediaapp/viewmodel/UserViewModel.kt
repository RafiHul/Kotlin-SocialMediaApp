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

    suspend fun setUserData(jwt: String,action: (String) -> Unit){
        withLoading {
            val get = repository.getUserData("Bearer $jwt")
            if (get.isSuccessful){
                _userData.value = get.body()
            } else {
                action(get.message())
            }
        }
    }

    suspend fun postRegisterUser(user: User,action: (Msg) -> Unit){
        withLoading {
            try {
                val post = repository.postRegisterUser(user)
                action(post.body()!!)
            } catch (e:Exception){
                action(Msg("failed","Failed To Connect"))
            }
        }
    }

    suspend fun postLoginUser(context: Context,username:String,password:String,action: (MsgWithToken) -> Unit){
        withLoading {
            try {
                val post = repository.postLoginUser(username, password)
                val postBody = post.body()!!

                if (post.isSuccessful && postBody.access_token.isNotEmpty()) {
                    setJWToken(postBody.access_token)
                    repository.setLoginData(context, postBody.access_token)
                }
                action(postBody)
            } catch (e: Exception) {
                action(MsgWithToken("failed", "","Failed To Connect"))
            }
        }
    }

    suspend fun changeProfile(firstName:String, lastName:String ,email:String,action: (Msg) -> Unit){
        try {
            val jwtBearer = "Bearer ${userJWToken.value.toString()}"
            val post = repository.changeProfile(jwtBearer, firstName, lastName, email)
            val postBody = post.body()!!
            if (post.isSuccessful){
                _userData.value = postBody
                action(Msg("success","berhasil mengganti"))
            } else {
                Log.d("suk","gk sukses")
            }
        } catch (e:Exception){
            action(Msg("failed","Failed To Connect"))
            Log.d("change email eror",e.message.toString())
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