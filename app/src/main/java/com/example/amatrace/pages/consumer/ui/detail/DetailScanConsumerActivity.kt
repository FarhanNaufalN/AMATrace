package com.example.amatrace.pages.consumer.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityDetailScanConsumerBinding

class DetailScanConsumerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailScanConsumerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailScanConsumerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.detailProductButton.setOnClickListener {
            toggleVisibility(binding.detailProductCard)
        }

        binding.prosesProduksiButton.setOnClickListener {
            toggleVisibility(binding.prosesProduksiCard)
        }

        binding.labelProductButton.setOnClickListener {
            toggleVisibility(binding.labelProductCard)
        }

        binding.pertanianButton.setOnClickListener{
            toggleVisibility(binding.pertanianCard)
        }

    }

    private fun toggleVisibility(cardView: View) {
        cardView.visibility = if (cardView.visibility == View.GONE) View.VISIBLE else View.GONE
    }
}