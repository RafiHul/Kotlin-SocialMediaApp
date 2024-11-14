package com.rafih.socialmediaapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rafih.socialmediaapp.databinding.ActivityLogRegBinding
import com.rafih.socialmediaapp.databinding.ActivityMainBinding
import com.rafih.socialmediaapp.viewmodel.PostViewModel
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class LogRegActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogRegBinding

    val userViewModel: UserViewModel by viewModels()
    val postViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLogRegBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}