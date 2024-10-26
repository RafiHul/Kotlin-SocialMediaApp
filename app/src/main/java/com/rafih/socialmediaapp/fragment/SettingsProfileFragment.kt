package com.rafih.socialmediaapp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentSettingsProfileBinding
import com.rafih.socialmediaapp.model.User
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@SuppressLint("CheckResult")
class SettingsProfileFragment : Fragment(R.layout.fragment_settings_profile) {

    private var _binding: FragmentSettingsProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var navController: NavController
    private lateinit var userData: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        //try get userData from livedata
        try {
            userData = userViewModel.userData.value!!
        } catch (e:NullPointerException){
            Toast.makeText(context, "Terjadi Eror Harap Login Ulang", Toast.LENGTH_SHORT).show()
            userViewModel.clearLoginJWT(requireContext())
            navController.navigate(R.id.action_settingsProfileFragment_to_loginFragment)
        }


        userViewModel.userData.observe(viewLifecycleOwner){
            binding.apply {
                textViewFirstName.text = it.first_name
                textViewLastName.text = it.last_name
                textViewEmail.text = it.email
            }
        }

        binding.buttonEditProfile.setOnClickListener {
            val direction = SettingsProfileFragmentDirections.actionSettingsProfileFragmentToEditSettingsProfileFragment(userData)
            navController.navigate(direction)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this){
            navController.navigate(R.id.action_settingsProfileFragment_to_profileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}