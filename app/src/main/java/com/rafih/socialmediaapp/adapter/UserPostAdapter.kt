package com.rafih.socialmediaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rafih.socialmediaapp.databinding.RecyclerviewPostBinding
import com.rafih.socialmediaapp.model.databases.UserPostItem

class UserPostAdapter: RecyclerView.Adapter<UserPostAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: RecyclerviewPostBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentPost: UserPostItem){
            binding.textViewTitlePostBeranda.text = currentPost.title
            binding.textViewUsernameBeranda.text = currentPost.usernamePost
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