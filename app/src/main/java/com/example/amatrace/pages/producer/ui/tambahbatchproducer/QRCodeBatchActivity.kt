package com.example.amatrace.pages.producer.ui.tambahbatchproducer

import android.R.attr.bitmap
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.NotificationCompat
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityQrcodeBinding
import com.example.amatrace.pages.producer.ProducerMainActivity
import com.example.amatrace.pages.supplier.MainSupplierActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class QRCodeBatchActivity : AppCompatActivity() {

        private lateinit var binding: ActivityQrcodeBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val qrCodeData = intent.getStringExtra("QR_CODE_DATA") ?: return
        generateQRCode()

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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun generateQRCode() {
        val builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.pop_up, null)
        builder.setView(dialogView)

        qrcodeGenerator(dialogView)

        val saveButton = dialogView.findViewById<AppCompatButton>(R.id.saveQRCodeButton)
        val qrCodeImage = dialogView.findViewById<ImageView>(R.id.barcodeImage)
        saveButton.setOnClickListener {
            if (qrCodeImage != null) {
                saveImage(qrCodeImage)
                startActivity(Intent(this, ProducerMainActivity::class.java))
            }
        }

        val dialog = builder.create()

        dialog.show()
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImage(qrCodeImage: ImageView) {
        val drawable = qrCodeImage.drawable as BitmapDrawable
        val bitmap = drawable.bitmap

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/StockWise"
            )
        }

        val uri: Uri? =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            uri?.let {
                val outputStream: OutputStream? = contentResolver.openOutputStream(it)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                } else {
                    Toast.makeText(this, "OutputStream is null", Toast.LENGTH_SHORT).show()
                }
                outputStream?.close()
                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun qrcodeGenerator(view: View) {
        val qrCodeData = intent.getStringExtra("QR_CODE_DATA") ?: return
        var qrCodeImage = view.findViewById<ImageView>(R.id.barcodeImage)
        if (qrCodeData.isNotEmpty()) {
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap =
                    barcodeEncoder.encodeBitmap(qrCodeData, BarcodeFormat.QR_CODE, 500, 500)
                if (qrCodeImage != null) {
                    qrCodeImage.setImageBitmap(bitmap)
                } else {
                    Toast.makeText(this, "QRCODEIMAGEVIEW NULL", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun onSaveButtonClicked(view: View) {

    }

    fun onBackButtonClicked(view: View) {
        val intent = Intent(this, ProducerMainActivity::class.java)
        startActivity(intent)
        finish()
    }

}