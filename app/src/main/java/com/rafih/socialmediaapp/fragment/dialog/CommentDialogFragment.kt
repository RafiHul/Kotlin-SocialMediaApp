package com.rafih.socialmediaapp.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.adapter.CommentPostAdapter
import com.rafih.socialmediaapp.databinding.FragmentCommentDialogBinding
import com.rafih.socialmediaapp.model.response.MsgDataComment
import com.rafih.socialmediaapp.viewmodel.PostViewModel
import com.rafih.socialmediaapp.viewmodel.UserViewModel

class CommentDialogFragment : DialogFragment(R.layout.fragment_comment_dialog) {

    var _binding: FragmentCommentDialogBinding? = null
    val binding get() = _binding!!

    val postViewModel: PostViewModel by activityViewModels()
    val userViewModel: UserViewModel by activityViewModels()

    private lateinit var commentPostAdapter: CommentPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCommentDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val postId = args?.getString("postId")

        if (userViewModel.userData.value == null){
            binding.apply {
                materialButtonSendComments.visibility = View.INVISIBLE
                textInputLayout.visibility = View.INVISIBLE
                editTextCommentsField.visibility = View.INVISIBLE
            }
        }

        commentPostAdapter = CommentPostAdapter(requireContext())

        binding.recyclerViewComment.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = commentPostAdapter
        }

        postViewModel.getPostComments(postId!!){
            refreshCommentAdapter(it)
        }

        binding.materialButtonSendComments.setOnClickListener{
            val userJwt = userViewModel.getJwtBearer()
            val commentsText = binding.editTextCommentsField.text.toString()
            if (commentsText.isNotEmpty()){
                postViewModel.userComments(userJwt,postId,commentsText){
                    refreshCommentAdapter(it)
                    Toast.makeText(context, "berhasil menambahkan komentar", Toast.LENGTH_SHORT).show()
                    binding.editTextCommentsField.setText("")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun refreshCommentAdapter(msgDataComment: MsgDataComment?){
        msgDataComment?.let {dat ->
            commentPostAdapter.differ.submitList(dat.data)
        } ?: Toast.makeText(context, "gagal memuat komentar", Toast.LENGTH_SHORT).show()
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