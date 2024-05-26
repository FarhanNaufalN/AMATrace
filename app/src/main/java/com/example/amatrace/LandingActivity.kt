package com.example.amatrace

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amatrace.databinding.ActivityLandingBinding
import com.example.amatrace.ui.customview.LoginButton

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.customerButton.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun playAnimation() {

        ObjectAnimator.ofFloat(binding.logo, View.ALPHA, -30f, 30f).apply {
            duration = 5000
            start()
        }
        ObjectAnimator.ofFloat(binding.cardView, View.TRANSLATION_Y, 100f, 0f).apply {
            duration = 5000
            start()
        }
        ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, -30f, 30f).apply {
            duration = 6000
            start()
        }
        ObjectAnimator.ofFloat(binding.customerButton, View.ALPHA, -30f, 30f).apply {
            duration = 6000
            start()
        }
        ObjectAnimator.ofFloat(binding.orText, View.ALPHA, -30f, 30f).apply {
            duration = 6000
            start()
        }
    }
}