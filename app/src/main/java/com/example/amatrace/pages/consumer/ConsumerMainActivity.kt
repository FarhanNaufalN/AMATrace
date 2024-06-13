package com.example.amatrace.pages.consumer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.amatrace.databinding.ActivityConsumerMainBinding
import com.example.amatrace.pages.landing.LandingActivity
import com.example.amatrace.pages.producer.ui.home.HomeFragment
import com.example.core.data.source.remote.preferences.Preference

class ConsumerMainActivity : AppCompatActivity() {

    private lateinit var myPreference: Preference
    private lateinit var binding: ActivityConsumerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreference = Preference(this)
        enableEdgeToEdge()

        binding.scanButton.setOnClickListener {
            val intent = Intent(this, ConsumerQRCodeScannerActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_QR_SCAN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_QR_SCAN && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra("SCAN_RESULT")
            if (result != null) {
                handleScannedResult(result)
            } else {
                Toast.makeText(this, "Scan result is null", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Scan cancelled or failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleScannedResult(contents: String) {
        Toast.makeText(this, "Hasil Scan: $contents", Toast.LENGTH_LONG).show()
        // Lakukan tindakan lain seperti mengarahkan ke aktivitas lain atau mengambil data dari server
    }

    companion object {
        private const val REQUEST_CODE_QR_SCAN = 101
    }
    fun onBackButtonClicked(view: View) {
        val intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
        finish()
    }
}
