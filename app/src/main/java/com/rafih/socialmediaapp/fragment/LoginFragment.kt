package com.rafih.socialmediaapp.fragment

import android.content.Intent
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
import com.rafih.socialmediaapp.LogRegActivity
import com.rafih.socialmediaapp.MainActivity
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentLoginBinding
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch

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

        //ProgressBar Loading
        userViewModel.loadingApi.observe(viewLifecycleOwner){
            if (it){
                binding.progressBarLogin.visibility = View.VISIBLE
            } else {
                binding.progressBarLogin.visibility = View.GONE
            }
        }

        buttonLogin()

        binding.buttonToDaftar.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun buttonLogin() {
        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextLogNama.text.toString()
            val password = binding.editTextLogPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()){
                lifecycleScope.launch {
                    userLogin(username,password)
                }
            }
        }
    }

    private suspend fun userLogin(username: String, password: String) {
        userViewModel.postLoginUser(requireContext(), username, password) {
            Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
            if (it.access_token.isNotEmpty()) {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("jwt",it.access_token)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userViewModel.setLoadingApiTrue()
        _binding = null
    }
}