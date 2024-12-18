package com.rafih.socialmediaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rafih.socialmediaapp.databinding.RecyclerviewCommentPostBinding
import com.rafih.socialmediaapp.model.databases.CommentItem

class CommentPostAdapter: RecyclerView.Adapter<CommentPostAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: RecyclerviewCommentPostBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentComment: CommentItem){

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