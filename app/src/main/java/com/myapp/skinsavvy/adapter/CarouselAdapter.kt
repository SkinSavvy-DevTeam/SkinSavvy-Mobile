package com.myapp.skinsavvy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myapp.skinsavvy.R
import com.myapp.skinsavvy.data.pref.DataModel
import com.myapp.skinsavvy.databinding.CarouselItemLayoutBinding
import com.myapp.skinsavvy.databinding.CarouselPosterLayoutBinding


class CarouselAdapter(private val items: List<DataModel>, private val isTextVisible: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (isTextVisible) {
            val binding = CarouselItemLayoutBinding.inflate(inflater, parent, false)
            ArticleViewHolder(binding)
        } else {
            val binding = CarouselPosterLayoutBinding.inflate(inflater, parent, false)
            PosterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is ArticleViewHolder -> {
                holder.binding.apply {
                    Glide.with(ivCarouselImage.context)
                        .load(item.image)
                        .into(ivCarouselImage)
                    tvItemName.text = item.title
                    tvItemDescription.text = item.description
                }
            }
            is PosterViewHolder -> {
                Glide.with(holder.binding.ivCarouselImage.context)
                    .load(item.image)
                    .into(holder.binding.ivCarouselImage)
            }
        }
    }

    override fun getItemCount() = items.size

    class ArticleViewHolder(val binding: CarouselItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    class PosterViewHolder(val binding: CarouselPosterLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}