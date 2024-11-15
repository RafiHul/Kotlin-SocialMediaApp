package com.rafih.socialmediaapp.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rafih.socialmediaapp.Utils.convertUriToMultiPart
import com.rafih.socialmediaapp.model.databases.Post
import com.rafih.socialmediaapp.model.response.Msg
import com.rafih.socialmediaapp.model.response.MsgDataPost
import com.rafih.socialmediaapp.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(val repo: PostRepository): ViewModel() {

    private val _Post: MutableLiveData<Post?> = MutableLiveData()
    val Post = _Post

    suspend fun getPost(){
        try {
            val response = repo.getPost()
            val body = response.body()
            if(response.isSuccessful){
                _Post.value = body
            }
        } catch (e: Exception){
            Log.d("failed","gagal memuat beranda")
        }
    }

    suspend fun userNewPost(contentResolver: ContentResolver,jwtToken: String,title: String,image: Uri?, action: (Msg) -> Unit){

        val multipartBody = image?.convertUriToMultiPart(contentResolver)
//        val titleRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), title) deprecated
        val titleRequestBody = title.toRequestBody("text/plain".toMediaTypeOrNull())

        try {
            val response = repo.userNewPost(jwtToken,titleRequestBody, multipartBody)
            if (response.isSuccessful){
                   action(response.body()!!)
            } else {
                action(response.body()!!)
            }
        } catch (e: Exception){
            Log.e("erorororoororor",e.message.toString())
        }
    }

    suspend fun getPostById(postId: String,action: (MsgDataPost) -> Unit){
        try {
            val response = repo.getPostById(postId)
            if (response.isSuccessful){
                action(response.body()!!)
            }
        } catch (e: Exception){
            Log.e("eror di postviewmodel",e.message.toString())
        }
    }
}