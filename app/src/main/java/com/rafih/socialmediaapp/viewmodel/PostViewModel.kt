package com.rafih.socialmediaapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rafih.socialmediaapp.model.databases.Post
import com.rafih.socialmediaapp.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
}