package com.rafih.socialmediaapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.rafih.socialmediaapp.adapter.FragmentPagerAdapter
import com.rafih.socialmediaapp.databinding.ActivityMainBinding
import com.rafih.socialmediaapp.viewmodel.PostViewModel
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myPager: ViewPager2

    val userViewModel: UserViewModel by viewModels()
    val postViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)

        lifecycleScope.launch {
            postViewModel.getPost()
        }

        setUpPager()

        //get jwt token from splashscreen and login activity
        val jwttoken = intent.getStringExtra("jwt")
        userViewModel.setJWToken(jwttoken)

        if (jwttoken?.isNotBlank() == true){
            lifecycleScope.launch {
                binding.root.visibility = View.INVISIBLE
                userViewModel.setUserData {
                    if (it.status == "failed") {
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show() //disini ada error atau sesi habis
                        Log.d("eror get", it.toString())
                        userViewModel.clearLoginJWT(applicationContext) //ini ada bug kalo gagal dia memulai ulang activity nya
                    }
                }
                binding.root.visibility = View.VISIBLE
            }
        }

//        "INI MASIH ADA ERROR DAN BUG PAS LOGIN"
    }

    override fun onDestroy() {
        super.onDestroy()
        Glide.get(this).clearMemory()
    }

    private fun setUpPager() {
        myPager = binding.viewPagerMain
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

    fun backToBeranda(){
        myPager.setCurrentItem(0)
    }
}