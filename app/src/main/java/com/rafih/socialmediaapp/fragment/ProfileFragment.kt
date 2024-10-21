package com.rafih.socialmediaapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        userJWT = userViewModel.userJWToken.value.toString()

        if (userJWT.isEmpty() || userJWT == "null") {
            Toast.makeText(context, "harap login terlebih dahulu", Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.action_profileFragment_to_loginFragment)
        } else {
            lifecycleScope.launch {
                userViewModel.setUserData(userJWT) {
                    Toast.makeText(
                        context,
                        "Gagal memuat akun, harap login ulang",
                        Toast.LENGTH_SHORT
                    ).show()
                    userViewModel.clearLoginJWT(requireContext())
                    navController.navigate(R.id.action_profileFragment_to_loginFragment)
                }
            }
        }

        userViewModel.loadingApi.observe(viewLifecycleOwner){
            if (it){
                binding.progressBar.visibility = View.VISIBLE
                binding.cardView.visibility = View.GONE
                binding.textViewUsername.visibility = View.GONE
                binding.imageViewProfilePic.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.cardView.visibility = View.VISIBLE
                binding.textViewUsername.visibility = View.VISIBLE
                binding.imageViewProfilePic.visibility = View.VISIBLE
                binding.textViewUsername.text = userViewModel.userData.value?.username
            }
        }

        binding.buttonLogout.setOnClickListener {
            userViewModel.clearLoginJWT(requireContext())
            navController.navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userViewModel.setLoadingApiTrue()
        _binding = null
    }
}