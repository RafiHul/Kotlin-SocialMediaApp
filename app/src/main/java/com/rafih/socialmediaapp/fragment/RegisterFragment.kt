package com.rafih.socialmediaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentRegisterBinding
import com.rafih.socialmediaapp.model.User
import com.rafih.socialmediaapp.viewmodel.UserViewModel

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonDaftar.setOnClickListener {
            val username = binding.editTextRegNama.text.toString()
            val password = binding.editTextRegPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()){
                userViewModel.postRegisterUser(User("s","p",0,"as",username,password)){
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}