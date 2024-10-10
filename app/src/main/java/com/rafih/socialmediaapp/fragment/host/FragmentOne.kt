package com.rafih.socialmediaapp.fragment.host

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentOneBinding

class FragmentOne : Fragment(R.layout.fragment_one) {

    private var _binding : FragmentOneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOneBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}