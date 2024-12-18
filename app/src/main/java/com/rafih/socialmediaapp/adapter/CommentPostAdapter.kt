package com.rafih.socialmediaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rafih.socialmediaapp.R
import com.rafih.socialmediaapp.Utils.stringToImageBitmap
import com.rafih.socialmediaapp.databinding.RecyclerviewCommentPostBinding
import com.rafih.socialmediaapp.model.databases.CommentItem

class CommentPostAdapter(val context: Context): RecyclerView.Adapter<CommentPostAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: RecyclerviewCommentPostBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentComment: CommentItem){

            binding.textViewTextComments.text = currentComment.text
            binding.textViewUsernameComments.text = currentComment.comments_by_user.username
            Glide.with(context)
                .load(stringToImageBitmap(currentComment.comments_by_user.profile_pic.toString()))
                .error(R.drawable.baseline_error_24)
                .placeholder(R.drawable.baseline_account_circle_24)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .circleCrop()
                .into(binding.imageButtonProfilePicComments)
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<CommentItem>(){
        override fun areItemsTheSame(
            oldItem: CommentItem,
            newItem: CommentItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CommentItem,
            newItem: CommentItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            RecyclerviewCommentPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.bind(differ.currentList[position])
    }
}