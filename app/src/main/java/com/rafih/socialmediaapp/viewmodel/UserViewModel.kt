package com.rafih.socialmediaapp.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafih.socialmediaapp.Utils.stringToImageBitmap
import com.rafih.socialmediaapp.Utils.convertUriToMultiPart
import com.rafih.socialmediaapp.Utils.toBitMap
import com.rafih.socialmediaapp.Utils.toByteArray
import com.rafih.socialmediaapp.model.response.Msg
import com.rafih.socialmediaapp.model.response.MsgWithToken
import com.rafih.socialmediaapp.model.databases.User
import com.rafih.socialmediaapp.model.response.MsgDataUser
import com.rafih.socialmediaapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(val app:Application,val repository: UserRepository): AndroidViewModel(app) {
    
    private val _loadingApi: MutableLiveData<Boolean> = MutableLiveData()
    val loadingApi = _loadingApi

    private val _userData: MutableLiveData<User?> = MutableLiveData()
    val userData = _userData

    private val _userProfilePic: MutableLiveData<Bitmap?> = MutableLiveData()
    val userProfilePic = _userProfilePic

    private val _userJWToken: MutableLiveData<String?> = MutableLiveData()
    val userJWToken = _userJWToken

    suspend fun <T> withLoading(block: suspend () -> T):T{
        _loadingApi.value = true
        return try {
            block()
        } finally {
            _loadingApi.value = false
        }
    }

    suspend fun setUserData(action: (MsgDataUser) -> Unit){
        withLoading {
            try {
                val get = repository.getUserData(getJwtBearer())
                val body = get.body()!!
                if (get.isSuccessful) {
                    _userData.value = body.data
                    _userProfilePic.value = stringToImageBitmap(body.data?.profile_pic)
                } else {
                    action(body)
                    _userData.value = null
                    _userProfilePic.value = null
                } // TODO: Cek function ini bermasalah atau nggak karena type return nya baru di ganti
            } catch (e: Exception){
                action(MsgDataUser(null,"sesi telah habis","failed"))
            }
        }
    }

    fun getUserDataById(userId: String,action: (MsgDataUser?) -> Unit){
        viewModelScope.launch{
            val response = repository.getUserDataById(userId)
            if (response.isSuccessful){
                action(response.body())
            }
        }
    }

    fun clearUserData(){
        _userData.value = null
        _userProfilePic.value = null
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
            val jwtBearer = getJwtBearer()
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

    suspend fun changePassword(newPassword: String,action: (Msg) -> Unit){
        try{
            val jwtToken = getJwtBearer()
            val post = repository.changePassword(jwtToken,newPassword)
            val postBody = post.body()
            if(post.isSuccessful && postBody != null){
                action(postBody)
            } else {
                action(Msg("failed","gagal post"))
            }
        } catch (e:Exception){
            action(Msg("failed","Failed To Connect"))
        }
    }

    suspend fun changeProfilePic(uri: Uri, contentResolver:ContentResolver, action: (Msg) -> Unit) {

        val multipartBody = uri.convertUriToMultiPart(contentResolver)
        val byteArray = uri.toByteArray(contentResolver)

        try {
            val jwtToken = getJwtBearer()
            val post = repository.changeProfilePic(jwtToken, multipartBody)
            val postBody = post.body()
            if (post.isSuccessful && postBody != null) {
                action(postBody)
                _userProfilePic.value = byteArray.toBitMap()
            } else {
                action(Msg("failed", "gagal post"))
            }
        } catch (e: Exception) {
            action(Msg("failed", "Failed To Connect"))
        }
    }

    fun setLoadingApiTrue(){
        _loadingApi.value = false
    }

    fun setJWToken(token: String?){
        _userJWToken.value = token
    }

    fun clearLoginJWT(context: Context){
        clearUserData()
        viewModelScope.launch {
            repository.clearLoginData(context)
            setJWToken("")
        }
    }

    fun getUserLoginJWT(context: Context) = repository.getLoginData(context)

    fun getJwtBearer(): String = "Bearer ${userJWToken.value.toString()}"
}
