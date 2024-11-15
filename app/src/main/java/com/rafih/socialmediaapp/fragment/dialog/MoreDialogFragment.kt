package com.rafih.socialmediaapp.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.adapter.MorePostAdapter
import com.rafih.socialmediaapp.databinding.FragmentMoreDialogBinding
import com.rafih.socialmediaapp.viewmodel.PostViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoreDialogFragment : DialogFragment(R.layout.fragment_more_dialog) {

    private var _binding: FragmentMoreDialogBinding? = null
    private val binding get() = _binding!!

    private val postViewModel: PostViewModel by activityViewModels()

    private var postId: Int? = null
    private var userId: Int? = null
    private var isUserOwner: Boolean = false
    private lateinit var optionList: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        postId = args?.getInt("postId")
        userId = args?.getInt("userId")
        optionList = resources.getStringArray(R.array.more_post_action)

        lifecycleScope.launch {
            binding.recyclerViewMoreOption.visibility = View.GONE
            withContext(Dispatchers.IO){//berpindah thread ke IO
                postViewModel.getPostById(postId.toString()){
                    if (userId.toString() == it.data.UserId.toString()){
                        isUserOwner = true
                    }
                }
            }
            val morePostAdapter = MorePostAdapter(isUserOwner,optionList)
            binding.recyclerViewMoreOption.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                adapter = morePostAdapter
            }
            binding.recyclerViewMoreOption.visibility = View.VISIBLE
        }


        //bisa di if in disini list nya
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            500,
            500
//            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun newInstance(postId: Int,userId: Int): MoreDialogFragment{
            val fragment = MoreDialogFragment()
            val args = Bundle()
            args.putInt("postId",postId)
            args.putInt("userId",userId)
            fragment.arguments = args
            return fragment
        }
    }
}