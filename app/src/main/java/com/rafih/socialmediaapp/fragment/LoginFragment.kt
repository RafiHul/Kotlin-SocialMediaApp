package com.rafih.socialmediaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentLoginBinding
import com.rafih.socialmediaapp.viewmodel.UserViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        userViewModel.loadingApi.observe(viewLifecycleOwner){
            if (it){
                binding.progressBarLogin.visibility = View.VISIBLE
            }else{
                binding.progressBarLogin.visibility = View.GONE
            }
        }

        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextLogNama.text.toString()
            val password = binding.editTextLogPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()){
                userViewModel.postLoginUser(requireContext(),username,password){
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.buttonToDaftar.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userViewModel.seuLoadingApiTrue()
        _binding = null

    }
}