package com.myapp.skinsavvy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myapp.skinsavvy.R
import com.myapp.skinsavvy.data.pref.DataModel
import com.myapp.skinsavvy.data.response.ArticlesItem
import com.myapp.skinsavvy.databinding.CarouselItemLayoutBinding
import com.myapp.skinsavvy.databinding.CarouselPosterLayoutBinding

class ArticleAdapter(private val items: List<ArticlesItem?>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = CarouselItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            Glide.with(ivCarouselImage.context)
                .load(item?.thumbnailUrl)
                .into(ivCarouselImage)
            tvItemCategory.text = item?.category
            tvItemName.text = item?.title
            tvItemDescription.text = item?.body
        }
    }

    class ArticleViewHolder(val binding: CarouselItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return items.size
    }
}