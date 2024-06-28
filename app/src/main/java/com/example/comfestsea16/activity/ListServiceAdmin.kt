package com.example.comfestsea16.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comfestsea16.databinding.ItemRowServiceBinding
import com.example.comfestsea16.fragment.home.Service

class ListServiceAdmin(private val listService: ArrayList<Service>) : RecyclerView.Adapter<ListServiceAdmin.ListViewHolder>() {

    class ListViewHolder(var binding: ItemRowServiceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }


    override fun getItemCount(): Int = listService.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val service = listService[position]
        holder.binding.tvItemName.text = service.name
        holder.binding.tvItemDescription.text = service.description

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(service.imageUrl)
            .into(holder.binding.imgItemPhoto)

        // Remove the click listener (or make it a no-op)
        holder.itemView.setOnClickListener(null) // Or use an empty lambda: {}
    }
}
