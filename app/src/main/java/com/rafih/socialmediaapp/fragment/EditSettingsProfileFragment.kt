package com.rafih.socialmediaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentEditSettingsProfileBinding

class EditSettingsProfileFragment : Fragment(R.layout.fragment_edit_settings_profile) {

    private var _binding : FragmentEditSettingsProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditSettingsProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}