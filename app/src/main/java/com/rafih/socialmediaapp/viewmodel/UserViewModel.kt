package com.rafih.socialmediaapp.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafih.socialmediaapp.Utils.decodeToByteArray
import com.rafih.socialmediaapp.Utils.toByteArray
import com.rafih.socialmediaapp.model.Msg
import com.rafih.socialmediaapp.model.MsgData
import com.rafih.socialmediaapp.model.MsgWithToken
import com.rafih.socialmediaapp.model.User
import com.rafih.socialmediaapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(val app:Application,val repository: UserRepository): AndroidViewModel(app) {
    
    private val _loadingApi: MutableLiveData<Boolean> = MutableLiveData()
    val loadingApi = _loadingApi

    private val _userData: MutableLiveData<User> = MutableLiveData()
    val userData = _userData

    private val _userProfilePic: MutableLiveData<Bitmap?> = MutableLiveData()
    val userProfilePic = _userProfilePic

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

    suspend fun setUserData(action: (String) -> Unit){
        withLoading {
            val get = repository.getUserData(getJwtBearer())
            if (get.isSuccessful){
                _userData.value = get.body()
                getProfilePic {
                    Log.d("asuwwww eror iki",it?.message.toString())
                }
            } else {
                action(get.raw().message)
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

        val byteArray = uri.toByteArray(contentResolver) //convert image to byte
        val mimeType = contentResolver.getType(uri) ?: "image/jpeg" //check image type
        val requestBody = byteArray.toRequestBody(mimeType.toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", "image123.jpg", requestBody)

        try {
            val jwtToken = getJwtBearer()
            val post = repository.changeProfilePic(jwtToken, multipartBody)
            val postBody = post.body()
            if (post.isSuccessful && postBody != null) {
                action(postBody)
            } else {
                action(Msg("failed", "gagal post"))
            }
        } catch (e: Exception) {
            action(Msg("failed", "Failed To Connect"))
        }
    }

    suspend fun getProfilePic(action: (MsgData?) -> Unit){
        try {
            val jwt = getJwtBearer()
            val getPic = repository.getProfilePic(jwt)
            val body = getPic.body()
            Log.d("bodyprofilepic",body.toString())
            if (getPic.isSuccessful && getPic.body()?.data != null) {
                val byteArrayImage = body?.data?.imageEncode?.decodeToByteArray()!! //decode base64 to bytearray
                val bitMapImage = BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage.size) //convert to bitmap

                _userProfilePic.value = bitMapImage
                Log.d("dummyuserdat viewmodel", bitMapImage.toString())
                action(body)
            } else {

                _userProfilePic.value = null
                action(body)
            }
        } catch (e: Exception){
            Log.d("erorgetpic viewmodel",e.message.toString())
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

    private fun getJwtBearer(): String = "Bearer ${userJWToken.value.toString()}"
}
