package com.example.amatrace.ui.lupaPassword

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityLupaPasswordBinding

class lupaPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLupaPasswordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLupaPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun onBackButtonClicked(view: View) {
        onBackPressed()
    }
}