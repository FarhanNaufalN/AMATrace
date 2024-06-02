package com.example.amatrace.pages.supplier.ui.tambahpengiriman

import android.R.attr.bitmap
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityQrcodeBinding
import com.example.amatrace.pages.supplier.MainSupplierActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class QRCodeActivity : AppCompatActivity() {

        private lateinit var binding: ActivityQrcodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val qrCodeData = intent.getStringExtra("QR_CODE_DATA") ?: return
        generateQRCode(qrCodeData)

        binding.saveButton.setOnClickListener {
            val qrValue = intent.getStringExtra("QR_CODE_DATA") ?: ""
            val qrFileName = "QR_Code"
            val savePath = "${externalCacheDir?.absolutePath}/QRCode/"
            val qrBitmap = (binding.qrImageView.drawable as BitmapDrawable).bitmap

            // Save the bitmap to the device's gallery
            val uri = saveImageToGallery(qrBitmap, qrFileName)

            if (uri != null) {
                // Notification if the image is successfully saved
                showNotification("QR Code saved", "QR Code saved successfully")
            } else {
                // Notification if there is an error saving the image
                showNotification("Error", "Failed to save QR Code")
            }
        }
    }

    private fun saveImageToGallery(bitmap: Bitmap, fileName: String): Uri? {
        val imagesDir = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/QR Codes")
        imagesDir.mkdirs()
        val imageFile = File(imagesDir, "$fileName.jpg")
        try {
            FileOutputStream(imageFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            // Notify the system that a new file has been added to the gallery
            MediaScannerConnection.scanFile(this, arrayOf(imageFile.absolutePath), null, null)
            return Uri.fromFile(imageFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1
        val channelId = "QRCodeChannel"
        val channelName = "QR Code Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(notificationId, notificationBuilder.build())
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

    fun onSaveButtonClicked(view: View) {

    }

    fun onBackButtonClicked(view: View) {
        val intent = Intent(this, MainSupplierActivity::class.java)
        startActivity(intent)
        finish()
    }
}