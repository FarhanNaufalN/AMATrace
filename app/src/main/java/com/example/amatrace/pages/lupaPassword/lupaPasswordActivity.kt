package com.example.amatrace.pages.lupaPassword

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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