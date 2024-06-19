package com.myapp.skinsavvy.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.myapp.skinsavvy.view.viewmodel.MainViewModel
import com.myapp.skinsavvy.R
import com.myapp.skinsavvy.adapter.ArticleAdapter
import com.myapp.skinsavvy.databinding.ActivityAllArticleBinding

class AllArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllArticleBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ArticleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.rvArticle.layoutManager = LinearLayoutManager(this)
        mainViewModel.listArticle.observe(this) { articleList ->
            adapter = ArticleAdapter(articleList)
            binding.rvArticle.adapter = adapter
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        setMenuItem()
    }

    private fun setMenuItem() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this@AllArticleActivity, MainActivity::class.java))
                    true
                }

                R.id.scan -> {
                    startActivity(Intent(this@AllArticleActivity, CameraXActivity::class.java))
                    true
                }

                else -> true
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}