package com.rafih.socialmediaapp.fragment

import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.canhub.cropper.CropImageView
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.databinding.FragmentCropImageBinding
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
        val userPic = args.userPic
        binding.cropImageView.cropRect = Rect(100, 300, 500, 1200)
        binding.cropImageView.setImageUriAsync(userPic)
    }

    override fun onSetImageUriComplete(view: CropImageView, uri: Uri, error: Exception?) {
        return
    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {
        return
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
