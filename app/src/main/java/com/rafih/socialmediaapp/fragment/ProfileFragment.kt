package com.rafih.socialmediaapp.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rafih.socialmediaapp.LogRegActivity
import com.rafih.socialmediaapp.MainActivity
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.Utils.toByteArray
import com.rafih.socialmediaapp.databinding.FragmentProfileBinding
import com.rafih.socialmediaapp.model.databases.User
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
            toLoginActivity()
        } else {
            lifecycleScope.launch {
                //set userData livedata
                userViewModel.setUserData {
                    if (it.status == "failed") {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show() //disini ada error
                        Log.d("eror get", it.toString())
                        userViewModel.clearLoginJWT(requireContext())
                        toLoginActivity()
                    }
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
                }
            }
        }

        userViewModel.userData.observe(viewLifecycleOwner){
            binding.textViewUsername.text = it.username
        }
        userViewModel.userProfilePic.observe(viewLifecycleOwner){
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.baseline_account_circle_24)
                .error(R.drawable.baseline_account_circle_24) //error drawable
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(binding.imageViewProfilePic)
        }
        binding.buttonSettingsProfile.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_settingsProfileFragment)
        }

        binding.buttonLogout.setOnClickListener {
            userViewModel.clearLoginJWT(requireContext())
            binding.imageViewProfilePic.setImageResource(R.drawable.baseline_account_circle_24)
            parentFragmentManager.beginTransaction().remove(ProfileFragment()).commitNow()
            (activity as MainActivity).backToBeranda()
        }

        binding.imageViewProfilePic.setOnClickListener{
            pickImageLauncher.launch("image/*")
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent() //ini akan membuka intent untuk mengambil file
    ) { uri: Uri? ->
        uri?.let {
            val fileSize = uri.toByteArray(requireActivity().contentResolver).size
            val direction = ProfileFragmentDirections.actionProfileFragmentToCropImageFragment(uri)

            if (fileSize >= 2000000){ //2MB
                Toast.makeText(context, "Ukuran File Tidak Boleh lebih dari 2MB", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(direction)
            }
        }
    }

    private fun toLoginActivity(){
        val intent = Intent(context, LogRegActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userViewModel.setLoadingApiTrue()
        _binding = null
    }
}