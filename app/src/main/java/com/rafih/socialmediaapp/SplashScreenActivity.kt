package com.rafih.socialmediaapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.rafih.socialmediaapp.repository.UserRepository
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import com.rafih.socialmediaapp.viewmodel.UserViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userViewModel = ViewModelProvider(this, UserViewModelFactory(application,userRepository)).get(UserViewModel::class.java)
        Log.d("wda",userViewModel.userJWToken.value.toString())

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}