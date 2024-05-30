package com.example.amatrace.pages.supplier.ui.tambahpengiriman

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.amatrace.databinding.ActivityQrcodeBinding

class QRCodeActivity : AppCompatActivity() {

        private lateinit var binding: ActivityQrcodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val qrCodeData = intent.getStringExtra("QR_CODE_DATA") ?: return
        generateQRCode(qrCodeData)
    }

    private fun generateQRCode(data: String) {
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val smallerDimension = if (width < height) width else height

        val qrgEncoder = QRGEncoder(data, null, QRGContents.Type.TEXT, smallerDimension)
        qrgEncoder.colorBlack = Color.GREEN
        qrgEncoder.colorWhite = Color.WHITE

        try {
            val bitmap = qrgEncoder.bitmap
            binding.qrImageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.v("GenerateQRCode", e.toString())
        }
    }
}