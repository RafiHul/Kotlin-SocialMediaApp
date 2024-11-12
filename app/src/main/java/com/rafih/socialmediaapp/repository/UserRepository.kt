package com.rafih.socialmediaapp.repository

import android.content.Context
import com.rafih.socialmediaapp.Utils.clearLoginInfo
import com.rafih.socialmediaapp.Utils.getLoginToken
import com.rafih.socialmediaapp.model.databases.User
import com.rafih.socialmediaapp.retrofit.RetrofitInstance
import com.rafih.socialmediaapp.Utils.saveLoginToken
import com.rafih.socialmediaapp.model.databases.Post
import com.rafih.socialmediaapp.model.response.Msg
import com.rafih.socialmediaapp.model.response.MsgData
import com.rafih.socialmediaapp.model.response.MsgWithToken
import okhttp3.MultipartBody
import retrofit2.Response

class UserRepository {
    val userServices = RetrofitInstance.getUserService


    suspend fun setLoginData(context: Context, jwt_token: String){
        saveLoginToken(context,jwt_token)
    }

    suspend fun clearLoginData(context: Context){
        clearLoginInfo(context)
    }

    suspend fun getUserData(jwt: String): Response<User> {
        return userServices.getUserData(jwt)
    }

    suspend fun postRegisterUser(user: User): Response<Msg> {
        return userServices.postRegisterUser(user.username,user.password)
    }

    suspend fun postLoginUser(username:String,password:String): Response<MsgWithToken> {
        return userServices.postLoginUser(username,password)
    }

    suspend fun changeProfile(jwtToken: String, firstName:String, lastName:String, email:String): Response<User> {
        return userServices.changeProfile(jwtToken, firstName, lastName, email)
    }

    suspend fun changePassword(jwtToken: String, newPassword: String): Response<Msg>{
        return userServices.changePassword(jwtToken,newPassword)
    }

    suspend fun changeProfilePic(jwtToken: String, image: MultipartBody.Part): Response<Msg> {
        return userServices.changeProfilePic(jwtToken, image)
    }

    suspend fun getProfilePic(jwtToken: String): Response<MsgData> {
        return userServices.getProfilePic(jwtToken)
    }

    fun getLoginData(context: Context) = getLoginToken(context)
}