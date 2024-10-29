package com.rafih.socialmediaapp.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentProfileBinding
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var navController: NavController
    private lateinit var userJWT: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        //get jwt token
        userJWT = userViewModel.userJWToken.value.toString()

        if (userJWT.isEmpty() || userJWT == "null") {
            Toast.makeText(context, "harap login terlebih dahulu", Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.action_profileFragment_to_loginFragment)
        } else {
            lifecycleScope.launch {
                //set userData livedata
                userViewModel.setUserData {
                    Toast.makeText(
                        context,
                        it,
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("eror get",it)
                    userViewModel.clearLoginJWT(requireContext())
                    navController.navigate(R.id.action_profileFragment_to_loginFragment)
                }
            }
        }

        // ProgressBar Loading
        userViewModel.loadingApi.observe(viewLifecycleOwner){
            if (it){
                binding.apply {
                    progressBar.visibility = View.VISIBLE
                    cardView.visibility = View.GONE
                    textViewUsername.visibility = View.GONE
                    imageViewProfilePic.visibility = View.GONE
                }
            } else {
                binding.apply {
                    progressBar.visibility = View.GONE
                    cardView.visibility = View.VISIBLE
                    textViewUsername.visibility = View.VISIBLE
                    imageViewProfilePic.visibility = View.VISIBLE
                    textViewUsername.text = userViewModel.userData.value?.username
                }
            }
        }

        binding.buttonSettingsProfile.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_settingsProfileFragment)
        }

        binding.buttonLogout.setOnClickListener {
            userViewModel.clearLoginJWT(requireContext())
            navController.navigate(R.id.action_profileFragment_to_loginFragment)
        }

        binding.imageViewProfilePic.setOnClickListener{
            pickImageLauncher.launch("image/*")
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent() //ini akan membuka intent untuk mengambil file
    ) { uri: Uri? ->
        uri?.let {
         Log.d("imageup",uri.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userViewModel.setLoadingApiTrue()
        _binding = null
    }
}