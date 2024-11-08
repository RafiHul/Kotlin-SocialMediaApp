package com.rafih.socialmediaapp.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.canhub.cropper.CropImageView
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentCropImageBinding
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch

//MenuProvider,
//SampleOptionsBottomSheet.Listener,

class CropImageFragment :
    Fragment(R.layout.fragment_crop_image),
    CropImageView.OnSetImageUriCompleteListener,
    CropImageView.OnCropImageCompleteListener {

    private var _binding : FragmentCropImageBinding? = null
    private val binding get() = _binding!!
    private val args: CropImageFragmentArgs by navArgs()
    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCropImageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        val userPic = args.userPic
        binding.cropImageView.setAspectRatio(1,1)
        binding.cropImageView.setFixedAspectRatio(true)

        binding.cropImageView.setImageUriAsync(userPic)

        binding.cropImageView.setOnCropImageCompleteListener(this)
        binding.cropImageView.setOnSetImageUriCompleteListener(this)

        binding.reset.setOnClickListener{
            binding.cropImageView.croppedImageAsync()
        }
    }

    override fun onSetImageUriComplete(view: CropImageView, uri: Uri, error: Exception?) {
        if (error != null){
            Toast.makeText(context, "Gagal memuat gambar, harap coba lagi", Toast.LENGTH_SHORT).show()
            Log.d("errmsg cropimgfg",error.message.toString())
            navController.navigate(R.id.action_cropImageFragment_to_profileFragment)
        }
    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {
        val uri = result.uriContent
        uri?.let {
            lifecycleScope.launch {
                userViewModel.changeProfilePic(it,requireActivity().contentResolver){
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    navController.navigate(R.id.action_cropImageFragment_to_profileFragment)
                }
            }
        } ?: Toast.makeText(context, "Gagal menyimpan gambar :(", Toast.LENGTH_SHORT).show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.cropImageView.setOnCropImageCompleteListener(null)
        binding.cropImageView.setOnSetImageUriCompleteListener(null)
        _binding = null
    }
}
