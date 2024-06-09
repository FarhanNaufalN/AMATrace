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

    private var lastScanResult: String? = null

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

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
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
                if (result.contents != lastScanResult) {
                    // Jika hasil pemindaian QR code baru
                    Toast.makeText(this, "Scanned QR Code: ${result.contents}", Toast.LENGTH_LONG).show()
                    lastScanResult = result.contents
                    val scanResultIntent = Intent(this, RawProdukActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        putExtra("SCAN_RESULT", result.contents)
                    }
                    startActivity(scanResultIntent)

                } else {
                    // Jika hasil pemindaian QR code sama dengan sebelumnya
                    Toast.makeText(this, "Scanned QR Code is the same as previous one", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
