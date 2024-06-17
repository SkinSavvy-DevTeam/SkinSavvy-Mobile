package com.myapp.skinsavvy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myapp.skinsavvy.data.pref.DataModel
import com.myapp.skinsavvy.databinding.CarouselPosterLayoutBinding

class PosterAdapter(private val items: List<DataModel>) : ListAdapter<DataModel, PosterAdapter.PosterViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CarouselPosterLayoutBinding.inflate(inflater, parent, false)
        return PosterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.binding.ivCarouselImage.context)
            .load(item.thumbnailUrl)
            .into(holder.binding.ivCarouselImage)
    }

    override fun getItemCount() = items.size

    class PosterViewHolder(val binding: CarouselPosterLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataModel>() {
            override fun areItemsTheSame(oldItem: DataModel, newItem: DataModel): Boolean {
                return oldItem.thumbnailUrl == newItem.thumbnailUrl
            }

            override fun areContentsTheSame(oldItem: DataModel, newItem: DataModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}