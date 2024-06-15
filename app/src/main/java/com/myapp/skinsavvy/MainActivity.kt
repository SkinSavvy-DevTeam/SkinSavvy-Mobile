package com.myapp.skinsavvy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapp.skinsavvy.adapter.CarouselAdapter
import com.myapp.skinsavvy.data.pref.DataModel
import com.myapp.skinsavvy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // carousel poster
        val recyclerViewPoster: RecyclerView = binding.rvCarouselPoster
        val poster = listOf(
            DataModel(R.drawable.image_carousel_1),
            DataModel(R.drawable.image_carousel_2)
        )

        recyclerViewPoster.adapter = CarouselAdapter(poster, isTextVisible = false)
        recyclerViewPoster.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //carousel article
        val recyclerViewArticle: RecyclerView = binding.rvCarouselArticle
        val article = listOf(
            DataModel(R.drawable.image_sample, "Article 1", "deskripsi"),
            DataModel(R.drawable.image_sample, "Article 2", "deskripsi"),
            DataModel(R.drawable.image_sample, "Article 3", "deskripsi")
        )

        recyclerViewArticle.adapter = CarouselAdapter(article, isTextVisible = true)
        recyclerViewArticle.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //carousel tips and tricks
        val recyclerViewTips: RecyclerView = binding.rvCarouselTips
        val tips = listOf(
            DataModel(R.drawable.image_sample, "Tips 1", "deskripsi"),
            DataModel(R.drawable.image_sample, "Tips 2", "deskripsi"),
            DataModel(R.drawable.image_sample, "Tips 3", "deskripsi")
        )

        recyclerViewTips.adapter = CarouselAdapter(tips, isTextVisible = true)
        recyclerViewTips.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.ivFeatureScan.setOnClickListener{
            startActivity(Intent(this@MainActivity, CameraXActivity::class.java))
        }

        binding.ivFeatureInstruction.setOnClickListener{
            startActivity(Intent(this@MainActivity, InstructionActivity::class.java))
        }
    }

}