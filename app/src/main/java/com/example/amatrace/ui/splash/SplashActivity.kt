package com.example.amatrace.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.amatrace.ui.landing.LandingActivity
import com.example.amatrace.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val image = binding.ivSplashIcon
        val imageBg = binding.backgroundImage
        image.alpha = 0f
        imageBg.alpha = 0f

        image.animate().setDuration(2000).alpha(1f).withEndAction {
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        imageBg.animate().setDuration(2000).alpha(1f).withEndAction {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}


