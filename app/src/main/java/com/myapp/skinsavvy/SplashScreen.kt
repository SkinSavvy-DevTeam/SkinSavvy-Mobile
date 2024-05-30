package com.myapp.skinsavvy

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myapp.skinsavvy.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val splashScreen = binding.ivSplashStory
        splashScreen.alpha = 0.2f
        splashScreen.animate().setDuration(1500).alpha(1f).withEndAction {
            val splashScreenGithub = Intent(this, MainActivity::class.java)
            startActivity(splashScreenGithub)
            finish()
        }
    }
}