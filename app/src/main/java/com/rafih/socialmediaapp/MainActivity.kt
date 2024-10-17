package com.rafih.socialmediaapp

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.rafih.socialmediaapp.adapter.FragmentPagerAdapter
import com.rafih.socialmediaapp.databinding.ActivityMainBinding
import com.rafih.socialmediaapp.model.User
import com.rafih.socialmediaapp.repository.UserRepository
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import com.rafih.socialmediaapp.viewmodel.UserViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)

        setUpPager()
        userViewModel = ViewModelProvider(this,UserViewModelFactory(application,userRepository)).get(UserViewModel::class.java)
        Log.d("wdadasdwa",userViewModel.userJWToken.value.toString())

        // mengecek token jwt
        lifecycleScope.launch {
            userViewModel.getUserLoginJWT(application).collect{
                userViewModel.setJWToken(it)
                // jika ada akan disimpan ke data user
                if (it != "") {
                    userViewModel.setUserData(it){
                        Toast.makeText(application, "Gagal memuat akun, harap login ulang", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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