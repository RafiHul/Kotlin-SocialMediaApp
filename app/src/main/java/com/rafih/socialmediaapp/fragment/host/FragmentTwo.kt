package com.rafih.socialmediaapp.fragment.host

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentTwoBinding

class FragmentTwo : Fragment(R.layout.fragment_two) {

    private var _binding : FragmentTwoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTwoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}