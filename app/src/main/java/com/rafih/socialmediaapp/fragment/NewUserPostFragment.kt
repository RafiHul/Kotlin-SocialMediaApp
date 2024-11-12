package com.rafih.socialmediaapp.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentNewUserPostBinding
import com.rafih.socialmediaapp.viewmodel.PostViewModel
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import kotlin.getValue
import androidx.activity.addCallback

class NewUserPostFragment : Fragment(R.layout.fragment_new_user_post) {

    private var _binding: FragmentNewUserPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val userViewModel: UserViewModel by activityViewModels()
    private val postViewModel: PostViewModel by activityViewModels()
    private var imageStored: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewUserPostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.apply {
            buttonPickImageNewPost.setOnClickListener{
                pickImageLaucher.launch("image/*")
            }
            buttonSubmitNewPost.setOnClickListener{
                val title = editTextTextTitleNewPost.text
                if(title.isNotEmpty()){
                    lifecycleScope.launch {
                        postViewModel.userNewPost(
                            requireContext().contentResolver,
                            userViewModel.getJwtBearer(),
                            title.toString(),
                            imageStored
                        ) {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                            navController.navigate(R.id.action_newUserPostFragment_to_berandaFragment)
                        }
                    }
                } else {
                    Toast.makeText(context, "title tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val pickImageLaucher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        binding.imageViewImageOverviewNewPost.visibility = View.VISIBLE
        imageStored = uri
        Glide.with(this)
            .load(uri)
            .error(R.drawable.baseline_error_24)
            .placeholder(R.drawable.baseline_account_circle_24)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .centerInside()
            .into(binding.imageViewImageOverviewNewPost)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this){
            navController.navigate(R.id.action_newUserPostFragment_to_berandaFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}