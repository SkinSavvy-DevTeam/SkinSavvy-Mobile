package com.myapp.skinsavvy.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myapp.skinsavvy.databinding.ActivityInstructionBinding

class InstructionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInstructionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstructionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}