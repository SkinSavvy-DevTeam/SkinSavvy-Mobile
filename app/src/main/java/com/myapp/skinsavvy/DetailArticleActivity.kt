package com.myapp.skinsavvy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.myapp.skinsavvy.data.response.Article
import com.myapp.skinsavvy.databinding.ActivityDetailArticleBinding
import java.util.Locale

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailArticleViewModel
    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DetailArticleViewModel::class.java]

        val id = intent.getStringExtra(DETAIL_ARTICLE)

        if (id != null) {
            viewModel.getStoryDetail(id)
        }

        viewModel.detailArticleResponse.observe(this) {
            setDetailArticle(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.responseMessage.observe(this) {data->
            if (data == null) {
                Toast.makeText(this, "Failed to fetch article", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.responseMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDetailArticle(article: Article?) {
        Log.d("DetailArticleActivity", "Setting article: $article")
        binding.apply {
            Glide.with(this@DetailArticleActivity)
                .load(article?.thumbnailUrl)
                .into(ivDetailArticle)
            tvCategory.text = article?.category?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
            tvTitle.text = article?.title
            tvDescription.text = article?.body
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val DETAIL_ARTICLE = "detail_article"
    }
}