package com.example.amatrace.pages.supplier.ui.tambahproduk
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.amatrace.databinding.ActivityTambahProdukBinding
import com.example.amatrace.pages.supplier.MainSupplierActivity
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.AddProductSupplierResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TambahProdukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahProdukBinding
    private lateinit var myPreference: Preference
    private var imageUri: Uri? = null
    private var imageFile: File? = null
    private val PICK_IMAGE_REQUEST = 1
    private val CAPTURE_IMAGE_REQUEST = 2
    private val PERMISSIONS_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        myPreference = Preference(this)
        checkPermissions()

        binding = ActivityTambahProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddProduct.setOnClickListener {
            if (imageUri == null) {
                Toast.makeText(this@TambahProdukActivity, "Please select an image", Toast.LENGTH_SHORT).show()
            } else {
                addProduct()
            }
        }

        binding.buttonSelectFromGallery.setOnClickListener {
            selectImageFromGallery()
        }

        binding.buttonCaptureFromCamera.setOnClickListener {
            captureImageFromCamera()
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun captureImageFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    imageUri = data?.data
                    imageUri?.let { uri ->
                        Glide.with(this).load(uri).into(binding.imageViewProduct)
                    }
                }
                CAPTURE_IMAGE_REQUEST -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    imageUri = getImageUri(bitmap)
                    imageUri?.let { uri ->
                        Glide.with(this).load(uri).into(binding.imageViewProduct)
                    }
                }
            }
        }
    }

    private fun getImageUri(bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun addProduct() {
        val token = myPreference.getAccessToken() ?: return
        val sku = binding.editTextSku.text.toString()
        val name = binding.editTextName.text.toString()
        val description = binding.editTextDescription.text.toString()

        if (imageUri == null) {
            Toast.makeText(this@TambahProdukActivity, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val imageUrlTemplate = "https://api-test.amatrace.com/supplier/product/%s"
        val imageUrl = String.format(imageUrlTemplate, generateImageFileName())

        println("Image URL: $imageUrl")
        println("SKU: $sku")


        val requestBodyMap = HashMap<String, String>()
        requestBodyMap["sku"] = sku
        requestBodyMap["name"] = name
        requestBodyMap["description"] = description
        requestBodyMap["image"] = imageUrl

        val call = Config.getApiService().addProductSupplier(token, requestBodyMap)

        call.enqueue(object : Callback<AddProductSupplierResponse> {
            override fun onResponse(call: Call<AddProductSupplierResponse>, response: Response<AddProductSupplierResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val product = response.body()
                    if (product != null) {
                        Toast.makeText(this@TambahProdukActivity, "Product added successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@TambahProdukActivity, MainSupplierActivity::class.java))
                    }
                } else {
                    // Handle unsuccessful response
                    val errorMessage = response.message() ?: "Unknown error"
                    Toast.makeText(this@TambahProdukActivity, "Failed to add product: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddProductSupplierResponse>, t: Throwable) {
                // Handle network failure
                Toast.makeText(this@TambahProdukActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                t.printStackTrace() // Print the stack trace for debugging
            }
        })
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
            } else {
                Toast.makeText(this, "Permissions required to access camera and storage", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateImageFileName(): String {
        // Mendapatkan timestamp saat ini
        val currentTimeMillis = System.currentTimeMillis()
        // Mengonversi timestamp menjadi format tanggal yang lebih mudah dibaca
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(currentTimeMillis))
        // Menggabungkan format nama file dengan timestamp yang telah diformat
        return "photos-$formattedDate.jpg"
    }

}