package com.rafih.socialmediaapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.rafih.socialmediaapp.adapter.FragmentPagerAdapter
import com.rafih.socialmediaapp.databinding.ActivityMainBinding
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)

        lifecycleScope.launch {
            userViewModel.getUserPost()
        }

        setUpPager()

        //get jwt token from splashscreen
        val jwttoken = intent.getStringExtra("jwttoken").toString()
        userViewModel.setJWToken(jwttoken)
    }

    private fun setUpPager() {
        val myPager = binding.viewPagerMain
        val myAdapter = FragmentPagerAdapter(supportFragmentManager,lifecycle)
        val myTabLayout = binding.tabLayoutMain

        myPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        myPager.adapter = myAdapter

        TabLayoutMediator(myTabLayout,myPager){tab,position ->
            when(position){
                0 -> tab.text = "Beranda"
                1 -> tab.text = "Profile"
            }
        }.attach()
    }
}