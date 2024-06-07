package com.example.amatrace.pages.producer.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amatrace.R
import com.example.amatrace.pages.producer.ui.detail.rawProduk.RawProdukActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class QRCodeScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mulai pemindaian QR code
        startQRScanner()
    }

    private fun startQRScanner() {
        IntentIntegrator(this).apply {
            setPrompt("Scan a QR code")
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
            initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                // Jika pembacaan QR code tidak berhasil
                Toast.makeText(this, "QR Code scanning cancelled", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_CANCELED)
            } else {
                // Jika pembacaan QR code berhasil
                Toast.makeText(this, "Scanned QR Code: ${result.contents}", Toast.LENGTH_LONG).show()
                val scanResultIntent = Intent(this, RawProdukActivity::class.java).apply {
                    putExtra("SCAN_RESULT", result.contents)
                    startActivity(this)
                }
                setResult(Activity.RESULT_OK, scanResultIntent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

        finish()
    }
}