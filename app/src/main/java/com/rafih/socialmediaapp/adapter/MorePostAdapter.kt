package com.rafih.socialmediaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafih.socialmediaapp.databinding.RecyclerviewMorePostBinding

class MorePostAdapter(val postId: Int, val userId: Int, val optionList: Array<String>): RecyclerView.Adapter<MorePostAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: RecyclerviewMorePostBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentOption: String){
            binding.textViewMoreOption.text = currentOption
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerviewMorePostBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(optionList[position])
    }

}