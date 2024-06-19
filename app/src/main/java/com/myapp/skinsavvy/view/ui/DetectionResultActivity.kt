package com.myapp.skinsavvy.view.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.myapp.skinsavvy.view.viewmodel.DetectionResultViewModel
import com.myapp.skinsavvy.databinding.ActivityDetectionResultBinding

class DetectionResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectionResultBinding
    private lateinit var viewModel: DetectionResultViewModel
    private var isSolutionVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DetectionResultViewModel::class.java]

        binding.solutionClick.setOnClickListener {
            toggleSolutionVisibility()
        }

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewModel.solution.observe(this) { explanation ->
            binding.progressBar.visibility = View.GONE
            if (explanation != null) {
                binding.solution.text = explanation
                binding.solution.visibility = View.VISIBLE
            } else {
                binding.solution.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val result = intent.getStringExtra(EXTRA_RESULT)
        val data = intent.getStringExtra(EXTRA_IMAGE)
        val imageUri = Uri.parse(data)

        runOnUiThread {
            imageUri?.let {
                binding.imageView.setImageURI(it)
                binding.title.text = result

                levelVisibility(result)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun toggleSolutionVisibility() {
        if (!isSolutionVisible) {
            val result = intent.getStringExtra(EXTRA_RESULT)
            val level = when (result) {
                "Clear" -> -1
                "Level 0" -> 0
                "Level 1" -> 1
                "Level 2" -> 2
                else -> 0
            }
            viewModel.fetchSolution(level)
        }
        isSolutionVisible = !isSolutionVisible
        binding.solution.visibility = if (isSolutionVisible) View.VISIBLE else View.GONE
    }

    private fun levelVisibility(result: String?) {
        when (result) {
            "Clear" -> {
                binding.clearFalse.visibility = View.GONE
                binding.clearTrue.visibility = View.VISIBLE
            }
            "Level 0" -> {
                binding.level0False.visibility = View.GONE
                binding.level0True.visibility = View.VISIBLE
            }
            "Level 1" -> {
                binding.level1False.visibility = View.GONE
                binding.level1True.visibility = View.VISIBLE
            }
            "Level 2" -> {
                binding.level2False.visibility = View.GONE
                binding.level2True.visibility = View.VISIBLE
            }
            else -> {
                binding.clearFalse.visibility = View.VISIBLE
                binding.clearTrue.visibility = View.GONE
                binding.level0False.visibility = View.VISIBLE
                binding.level0True.visibility = View.GONE
                binding.level1False.visibility = View.VISIBLE
                binding.level1True.visibility = View.GONE
                binding.level2False.visibility = View.VISIBLE
                binding.level2True.visibility = View.GONE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_RESULT = "extra_result"
    }
}