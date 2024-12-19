package com.rafih.socialmediaapp.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.adapter.CommentPostAdapter
import com.rafih.socialmediaapp.databinding.FragmentCommentBinding
import com.rafih.socialmediaapp.viewmodel.PostViewModel

class CommentDialogFragment : DialogFragment(R.layout.fragment_comment) {

    var _binding: FragmentCommentBinding? = null
    val binding get() = _binding!!

    val postViewModel : PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCommentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val postId = args?.getString("postId")

        val commentPostAdapter = CommentPostAdapter(requireContext())

        binding.recyclerViewComment.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = commentPostAdapter
        }

        postViewModel.getPostComments(postId!!){
            commentPostAdapter.differ.submitList(it)
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(postId: String): CommentDialogFragment {
            val args = Bundle()
            val fragment = CommentDialogFragment()
            args.putString("postId",postId)
            fragment.arguments = args
            return fragment
        }
    }
}