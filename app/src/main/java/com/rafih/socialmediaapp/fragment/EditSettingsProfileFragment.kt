package com.rafih.socialmediaapp.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentEditSettingsProfileBinding
import com.rafih.socialmediaapp.model.User
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch

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

        setUpTextView()

        submitButton()
    }

    private fun submitButton() {
        binding.buttonSubmitSettingsProfile.setOnClickListener {
            val firstName = binding.editTextFirstNameProfile.text.toString()
            val lastName = binding.editTextLastNameProfile.text.toString()
            val email = binding.editTextEmailProfile.text.toString()


            if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty()){
                lifecycleScope.launch {
                    saveUserChange(firstName,lastName,email)
                }
            } else {
                Toast.makeText(context, "Tidak Boleh Ada yang kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun saveUserChange(firstName:String, lastName:String ,email:String) {
        userViewModel.changeProfile(firstName,lastName,email){
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.action_editSettingsProfileFragment_to_settingsProfileFragment)
        }
    }

    private fun setUpTextView() {
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