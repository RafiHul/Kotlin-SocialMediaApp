package com.rafih.socialmediaapp.fragment.dialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.adapter.MorePostAdapter
import com.rafih.socialmediaapp.databinding.FragmentMoreDialogBinding
import com.rafih.socialmediaapp.model.databases.PostItem
import com.rafih.socialmediaapp.viewmodel.PostViewModel
import com.rafih.socialmediaapp.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

class MoreDialogFragment : DialogFragment(R.layout.fragment_more_dialog) {

    private var _binding: FragmentMoreDialogBinding? = null
    private val binding get() = _binding!!

    private val postViewModel: PostViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private val writeExternalStoragePermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val downloadDirectory = Environment.DIRECTORY_DOWNLOADS

    private var userId: Int? = null
    private var isUserOwner: Boolean = false
    private var userJwt: String? = null

    private lateinit var postItem: PostItem//post id ini gk ada null handling ketika download
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
        userId = userViewModel.userData.value?.id
        userJwt = userViewModel.getJwtBearer()
        optionList = resources.getStringArray(R.array.more_post_action)

        lifecycleScope.launch {
            withContext(Dispatchers.IO){//berpindah thread ke IO
                postViewModel.getPostById(args?.getInt("postId").toString()){
                    postItem = it.data
                    if (userId.toString() == postItem.UserId.toString()){
                        isUserOwner = true
                    }
                }
            }

            val morePostAdapter = MorePostAdapter(isUserOwner,optionList,
                deletePostAction = {
                    postViewModel.deletePostAdapter(userJwt!!,postItem.id){
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        dismiss()
                }},
                savePostAction = {
                    if (postItem.image == null){
                        // save title to clipboard
                        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Text Berhasil Di salin",postItem.title)
                        clipboard.setPrimaryClip(clip)
                    }
                    dismiss()
                })

            binding.recyclerViewMoreOption.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                adapter = morePostAdapter
            }
            binding.recyclerViewMoreOption.visibility = View.VISIBLE
            binding.progressBar4.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            500,
            500
//            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


    private fun saveBitMapDownloadAndroidPie(bitMap: Bitmap, fileName: String): Boolean{
        return try {
            val downloadDir = Environment.getExternalStoragePublicDirectory(downloadDirectory) // mengambil public directory
            val file = File(downloadDir,fileName) //membuat file baru
            val outputStream = FileOutputStream(file) //
            saveBitMapToStream(bitMap,outputStream)
            outputStream.close()
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    private fun saveBitMapToStream(bitmap: Bitmap, stream: FileOutputStream?) {
        stream?.let {
            bitmap.compress(Bitmap.CompressFormat.PNG,100,it)
        }
    }

    private fun fileNamesByDateTime(mimeType: String): String {
        return "${LocalDateTime.now()}.$mimeType"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun newInstance(postId: Int): MoreDialogFragment{
            val fragment = MoreDialogFragment()
            val args = Bundle()
            args.putInt("postId",postId)
            fragment.arguments = args
            return fragment
        }
    }
}