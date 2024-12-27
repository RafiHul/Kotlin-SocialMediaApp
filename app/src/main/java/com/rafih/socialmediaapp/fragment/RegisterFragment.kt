package com.rafih.socialmediaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentRegisterBinding
import com.rafih.socialmediaapp.model.databases.User
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        //progressBar Loading
        userViewModel.loadingApi.observe(viewLifecycleOwner){
            if (it){
                binding.progressBarRegister.visibility = View.VISIBLE
            }else{
                binding.progressBarRegister.visibility = View.GONE
            }
        }

        binding.buttonDaftar.setOnClickListener {
            val username = binding.editTextRegNama.text.toString()
            val password = binding.editTextRegPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()){
                lifecycleScope.launch {
                    userViewModel.postRegisterUser(User("s","p",0,"as",username,password,null)){
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.buttonToLogin.setOnClickListener {
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userViewModel.setLoadingApiTrue()
        _binding = null
    }
}