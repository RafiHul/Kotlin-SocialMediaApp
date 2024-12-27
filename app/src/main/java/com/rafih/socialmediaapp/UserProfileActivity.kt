package com.rafih.socialmediaapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.rafih.socialmediaapp.databinding.ActivityUserProfileBinding
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView4) as NavHostFragment
        val navController = navHostFragment.navController

        val userIdData = intent.getStringExtra("userId")
        Log.d("adagk", userIdData.toString())

        val bundle = Bundle().apply {
            putString("userId", userIdData)
        }

        navController.setGraph(R.navigation.profile_nav, bundle)
    }
}
