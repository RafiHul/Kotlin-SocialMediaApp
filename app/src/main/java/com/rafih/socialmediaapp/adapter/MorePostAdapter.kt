package com.rafih.socialmediaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafih.socialmediaapp.databinding.RecyclerviewMorePostBinding

class MorePostAdapter(val isUserOwner: Boolean, val optionList: Array<String>): RecyclerView.Adapter<MorePostAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: RecyclerviewMorePostBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentOption: String){
            if (!isUserOwner && currentOption in listOf("Delete")){
                binding.root.visibility = View.GONE
            }

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