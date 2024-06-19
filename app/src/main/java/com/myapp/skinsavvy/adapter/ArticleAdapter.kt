package com.myapp.skinsavvy.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myapp.skinsavvy.view.ui.DetailArticleActivity
import com.myapp.skinsavvy.data.response.ArticlesItem
import com.myapp.skinsavvy.databinding.ArticleItemLayoutBinding
import java.util.Locale

class ArticleAdapter(private val items: List<ArticlesItem?>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ArticleItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)
        }
    }

    class ArticleViewHolder(private val binding: ArticleItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) {
            binding.apply {
                article.thumbnailUrl?.let {
                    Glide.with(itemView.context)
                        .load(it)
                        .into(ivCarouselImage)
                }
                tvItemCategory.text = article.category?.capitalize(Locale.ROOT)
                tvItemName.text = article.title

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailArticleActivity::class.java)
                    intent.putExtra(DetailArticleActivity.DETAIL_ARTICLE, article.id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun getItem(position: Int): ArticlesItem? {
        return items[position]
    }
}