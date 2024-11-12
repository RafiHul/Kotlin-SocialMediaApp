package com.rafih.socialmediaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.adapter.UserPostAdapter
import com.rafih.socialmediaapp.databinding.FragmentBerandaBinding
import com.rafih.socialmediaapp.viewmodel.PostViewModel
import com.rafih.socialmediaapp.viewmodel.UserViewModel

class BerandaFragment : Fragment(R.layout.fragment_beranda) {

    private var _binding : FragmentBerandaBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private val postViewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBerandaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userPostAdapter = UserPostAdapter(requireContext())
        binding.recyclerViewUserPost.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = userPostAdapter
        }
        activity?.let {
            postViewModel.Post.observe(viewLifecycleOwner){
                userPostAdapter.differ.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}