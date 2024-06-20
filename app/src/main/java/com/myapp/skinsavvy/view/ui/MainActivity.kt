package com.myapp.skinsavvy.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.myapp.skinsavvy.view.viewmodel.MainViewModel
import com.myapp.skinsavvy.R
import com.myapp.skinsavvy.adapter.ArticleAdapter
import com.myapp.skinsavvy.adapter.PosterAdapter
import com.myapp.skinsavvy.data.pref.DataModel
import com.myapp.skinsavvy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ArticleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val recyclerViewPoster: RecyclerView = binding.rvCarouselPoster
        val poster = listOf(
            DataModel(R.drawable.image_poster_1),
            DataModel(R.drawable.image_poster_2)
        )

        recyclerViewPoster.adapter = PosterAdapter(poster)
        recyclerViewPoster.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.rvArticle.layoutManager = LinearLayoutManager(this)
        mainViewModel.listArticle.observe(this) { articleList ->
            val articleCount = articleList.take(3)

            if (articleCount.isEmpty()) {
                binding.rvArticle.visibility = View.GONE
                binding.buttonAllArticle.visibility = View.GONE
                binding.tvNoArticle.visibility = View.VISIBLE
            } else {
                binding.rvArticle.visibility = View.VISIBLE
                binding.buttonAllArticle.visibility = View.VISIBLE
                binding.tvNoArticle.visibility = View.GONE
                adapter = ArticleAdapter(articleCount)
                binding.rvArticle.adapter = adapter
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.ivFeatureScan.setOnClickListener{
            startActivity(Intent(this@MainActivity, CameraXActivity::class.java))
        }

        binding.ivFeatureInstruction.setOnClickListener{
            startActivity(Intent(this@MainActivity, InstructionActivity::class.java))
        }

        binding.ivFeatureHistory.setOnClickListener{
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
        }

        binding.buttonAllArticle.setOnClickListener{
            startActivity(Intent(this@MainActivity, AllArticleActivity::class.java))
        }

        setMenuItem()
    }
    private fun setMenuItem() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    true
                }

                R.id.scan -> {
                    startActivity(Intent(this@MainActivity, CameraXActivity::class.java))
                    true
                }

                else -> true
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}