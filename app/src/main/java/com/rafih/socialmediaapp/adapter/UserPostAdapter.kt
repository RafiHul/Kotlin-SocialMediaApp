package com.rafih.socialmediaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.Utils.decodeToByteArray
import com.rafih.socialmediaapp.Utils.toBitMap
import com.rafih.socialmediaapp.databinding.RecyclerviewPostBinding
import com.rafih.socialmediaapp.model.databases.UserPostItem

class UserPostAdapter(val context: Context): RecyclerView.Adapter<UserPostAdapter.MyViewHolder>() {

//    init {
//        // Basic Pre-fetch
//        profiles.forEach { profile ->
//            Glide.with(context)
//                .load(profile.photoUrl)
//                .preload()
//        }
//    }

    inner class MyViewHolder(val binding: RecyclerviewPostBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentPost: UserPostItem){
            val currImage = currentPost.image

            binding.textViewTitlePostBeranda.text = currentPost.title
            binding.textViewUsernameBeranda.text = currentPost.usernamePost

            Glide.with(context)
                .load(currentPost.userProfilePicturePost?.decodeToByteArray())
                .error(R.drawable.baseline_error_24)
                .placeholder(R.drawable.baseline_account_circle_24)
                .diskCacheStrategy(DiskCacheStrategy.NONE) //tidak memberikan cache
                .circleCrop()
                .into(binding.imageViewProfilePicBeranda)

//            binding.imageViewProfilePicBeranda.setImageBitmap(currentPost.userProfilePicturePost?.decodeToByteArray()?.toBitMap())

            if (currImage == null){
                binding.imageViewUserPost.visibility = View.GONE
            } else {
                Glide.with(context)
                    .load(currImage.decodeToByteArray())
                    .error(R.drawable.baseline_error_24)
                    .placeholder(R.drawable.baseline_downloading_24)
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //memberikan cache
                    .into(binding.imageViewUserPost)
            }
        }
    }
    private val differCallback = object : DiffUtil.ItemCallback<UserPostItem>(){
        override fun areItemsTheSame(oldItem: UserPostItem, newItem: UserPostItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserPostItem, newItem: UserPostItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RecyclerviewPostBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPost = differ.currentList[position]
        holder.bind(currentPost)
    }

}