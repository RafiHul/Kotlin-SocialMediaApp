package com.rafih.socialmediaapp.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.socialmediaapp.UserProfileActivity
import com.rafih.socialmediaapp.Utils.convertUriToMultiPart
import com.rafih.socialmediaapp.fragment.dialog.CommentDialogFragment
import com.rafih.socialmediaapp.fragment.dialog.MoreDialogFragment
import com.rafih.socialmediaapp.model.databases.Post
import com.rafih.socialmediaapp.model.databases.User
import com.rafih.socialmediaapp.model.response.Msg
import com.rafih.socialmediaapp.model.response.MsgDataComment
import com.rafih.socialmediaapp.model.response.MsgDataPost
import com.rafih.socialmediaapp.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    suspend fun deletePost(jwtToken: String,postId: String, action: (String) -> Unit){
        try {
            val response = repo.deletePost(jwtToken,postId)
            if (response.isSuccessful){
                action(response.body()?.message.toString())
            }
        } catch (e: Exception){
            action(e.message.toString())
        }
    }

    fun deletePostAdapter(jwtToken: String,postId: String,action: (String) -> Unit){
        viewModelScope.launch{
            deletePost(jwtToken,postId,action)
            getPost() //refresh beranda
        }
    }

    fun getPostComments(postId: String,action: (MsgDataComment?) -> Unit){
        viewModelScope.launch{
            val response = repo.getPostComments(postId)
            if (response.isSuccessful){
                action(response.body())
            }
        }
    }

    fun getPostUser(userId: String, action: (Post?) -> Unit){
        viewModelScope.launch{
            val response = repo.getPostUser(userId)
            if (response.isSuccessful){
                action(response.body())
            }
        }
    }

    fun userComments(jwtToken: String, postId: String, text: String, action: (MsgDataComment?) -> Unit){
        viewModelScope.launch{
            val response = repo.userComment(jwtToken,postId,text)
            if (response.isSuccessful){
                action(response.body())
            }
        }
    }

    fun handleActionMore(context: Context, userData: User?, fm: FragmentManager, postId: Int){
        userData?.let {
            MoreDialogFragment.newInstance(postId).show(fm, "tes")
        } ?: Toast.makeText(context, "Harap Login Terlebih dahulu", Toast.LENGTH_SHORT).show()
    }

    fun handleActionComment(postId: String, fm: FragmentManager){
        CommentDialogFragment.newInstance(postId).show(fm,"show comments")
    }

    fun handleOtherUserProfile(userId: String, context: Context) {
        val intent = Intent(context, UserProfileActivity::class.java).putExtra("userId", userId)
        context.startActivity(intent)
    }

}