package com.rafih.socialmediaapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentEditSettingsProfileBinding
import com.rafih.socialmediaapp.model.User
import com.rafih.socialmediaapp.viewmodel.UserViewModel

class EditSettingsProfileFragment : Fragment(R.layout.fragment_edit_settings_profile) {

    private var _binding : FragmentEditSettingsProfileBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private val args: EditSettingsProfileFragmentArgs by navArgs()
    private lateinit var navController: NavController

    private lateinit var userData: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditSettingsProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userData = args.userData
        navController = findNavController()

        binding.editTextEmailProfile.setText(userData.email)
        binding.editTextFirstNameProfile.setText(userData.first_name)
        binding.editTextLastNameProfile.setText(userData.last_name)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback{
            navController.navigate(R.id.action_editSettingsProfileFragment_to_settingsProfileFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}