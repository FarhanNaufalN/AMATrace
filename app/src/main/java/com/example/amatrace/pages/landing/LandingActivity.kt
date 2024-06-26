package com.example.amatrace.pages.landing

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.amatrace.databinding.ActivityLandingBinding
import com.example.amatrace.pages.consumer.ConsumerMainActivity
import com.example.amatrace.pages.login.LoginActivity
import com.example.amatrace.pages.supplier.MainSupplierActivity


class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding
    private lateinit var animatorSet: AnimatorSet

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
            startActivity(Intent(this, ConsumerMainActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        if (::animatorSet.isInitialized && animatorSet.isRunning) {
            animatorSet.cancel()
        }
    }

    private fun playAnimation() {
        val logoAnimator = ObjectAnimator.ofFloat(binding.logo, View.ALPHA, 0f, 1f).apply {
            duration = 2000
        }

        val cardViewAnimator = ObjectAnimator.ofFloat(binding.cardView, View.TRANSLATION_Y, 100f, 0f).apply {
            duration = 3000
            interpolator = OvershootInterpolator() // Apply overshoot interpolator for a bouncy effect
        }

        val loginButtonAnimator = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).apply {
            duration = 3000
            interpolator = AccelerateDecelerateInterpolator() // Apply accelerate-decelerate interpolator
        }

        val customerButtonAnimator = ObjectAnimator.ofFloat(binding.customerButton, View.ALPHA, 0f, 1f).apply {
            duration = 3000
            interpolator = AccelerateDecelerateInterpolator() // Apply accelerate-decelerate interpolator
        }

        val orTextAnimator = ObjectAnimator.ofFloat(binding.orText, View.ALPHA, 0f, 1f).apply {
            duration = 3000
            interpolator = AccelerateDecelerateInterpolator() // Apply accelerate-decelerate interpolator
        }

        animatorSet = AnimatorSet().apply {
            playTogether(logoAnimator, cardViewAnimator, loginButtonAnimator, customerButtonAnimator, orTextAnimator)
            start()
        }
    }
}
