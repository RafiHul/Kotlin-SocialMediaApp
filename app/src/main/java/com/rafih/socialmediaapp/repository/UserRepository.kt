package com.rafih.socialmediaapp.repository

import android.content.Context
import com.rafih.socialmediaapp.Utils.clearLoginInfo
import com.rafih.socialmediaapp.Utils.getLoginToken
import com.rafih.socialmediaapp.model.User
import com.rafih.socialmediaapp.model.UserList
import com.rafih.socialmediaapp.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import com.rafih.socialmediaapp.Utils.saveLoginToken
import com.rafih.socialmediaapp.model.Msg
import com.rafih.socialmediaapp.model.MsgWithToken
import retrofit2.Response

class UserRepository {
    val userServices = RetrofitInstance.getUserService


    suspend fun setLoginData(context: Context, jwt_token: String){
        saveLoginToken(context,jwt_token)
    }

    suspend fun clearLoginData(context: Context){
        clearLoginInfo(context)
    }

    suspend fun postRegisterUser(user: User): Response<Msg> {
        return userServices.postRegisterUser(user.username,user.password)
    }

    suspend fun postLoginUser(username:String,password:String): Response<MsgWithToken> {
        return userServices.postLoginUser(username,password)
    }

    fun getLoginData(context: Context) = getLoginToken(context)
}