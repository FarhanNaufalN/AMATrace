package com.example.amatrace.pages.producer.ui.tambahprodukproducer

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityAddProdukProducerBinding
import com.example.amatrace.databinding.ActivityTambahProdukBinding
import com.example.amatrace.pages.producer.ProducerMainActivity
import com.example.amatrace.pages.supplier.MainSupplierActivity
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.AddProductProducerResponse
import com.example.core.data.source.remote.response.AddProductSupplierResponse
import com.example.core.data.source.remote.response.UploadImageProductProducerResponse
import com.example.core.data.source.remote.response.UploadImageProductSupplierResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddProdukProducerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProdukProducerBinding
    private lateinit var myPreference: Preference
    private var imageUri: Uri? = null
    private var imageFile: File? = null
    private val PICK_IMAGE_REQUEST = 1
    private val CAPTURE_IMAGE_REQUEST = 2
    private val PERMISSIONS_REQUEST_CODE = 123
    private var uploadedImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        myPreference = Preference(this)
        checkPermissions()

        binding = ActivityAddProdukProducerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddProduct.setOnClickListener {
            if (uploadedImageUrl == null) {
                Toast.makeText(this@AddProdukProducerActivity, "Please select an image", Toast.LENGTH_SHORT).show()
            } else {
                addProduct(uploadedImageUrl)
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
        if (intent.resolveActivity(packageManager) != null) {
            val imageFile = createImageFile()
            imageUri = FileProvider.getUriForFile(
                this,
                "com.example.amatrace.fileprovider",
                imageFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        } else {

        }
    }


    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    imageUri = data?.data
                    imageUri?.let { uri ->
                        Glide.with(this).load(uri).into(binding.imageViewProduct)
                        // Kita panggil uploadImageToServer setelah pengguna memilih gambar
                        uploadImageToServer(imageUri!!)
                    }
                }
                CAPTURE_IMAGE_REQUEST -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    imageUri = getImageUri(bitmap)
                    imageUri?.let { uri ->
                        Glide.with(this).load(uri).into(binding.imageViewProduct)
                        // Kita panggil uploadImageToServer setelah pengguna mengambil foto
                        uploadImageToServer(imageUri!!)
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

    private fun uploadImageToServer(uri: Uri) {
        val token = myPreference.getAccessToken() ?: return
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val file = bitmapToFile(bitmap)
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val client = Config.getApiService().uploadImageProducer(token, body)
        client.enqueue(object : Callback<UploadImageProductProducerResponse> {
            override fun onResponse(
                call: Call<UploadImageProductProducerResponse>,
                response: Response<UploadImageProductProducerResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.success) {
                        uploadedImageUrl = responseBody.data.image
                    } else {
                        // Gagal mengunggah gambar
                        Toast.makeText(
                            this@AddProdukProducerActivity,
                            "Failed to upload image",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // Gagal terhubung ke server
                    Toast.makeText(
                        this@AddProdukProducerActivity,
                        "Failed to connect to server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UploadImageProductProducerResponse>, t: Throwable) {
                // Gagal terhubung ke server
                Toast.makeText(
                    this@AddProdukProducerActivity,
                    "Failed to connect to server: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun addProduct(uploadedImageUrl: String?) {
        // Pastikan uploadedImageUrl sudah diisi sebelumnya
        this.uploadedImageUrl?.let { imageUrl ->
            val token = myPreference.getAccessToken() ?: return
            val sku = binding.editTextSku.text.toString()
            val name = binding.editTextName.text.toString()
            val description = binding.editTextDescription.text.toString()
            val ingredients = binding.editTextKomposisi.text.toString()

            val requestBodyMap = HashMap<String, String>()
            requestBodyMap["sku"] = sku
            requestBodyMap["name"] = name
            requestBodyMap["description"] = description
            requestBodyMap["ingredients"] = ingredients
            requestBodyMap["image"] = imageUrl

            val call = Config.getApiService().addProductProducer(token, requestBodyMap)

            call.enqueue(object : Callback<AddProductProducerResponse> {
                override fun onResponse(call: Call<AddProductProducerResponse>, response: Response<AddProductProducerResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val product = response.body()
                        if (product != null) {
                            Toast.makeText(this@AddProdukProducerActivity, "Product added successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@AddProdukProducerActivity, ProducerMainActivity::class.java))
                        }
                    } else {
                        // Handle unsuccessful response
                        val errorMessage = response.message() ?: "Unknown error"
                        Toast.makeText(this@AddProdukProducerActivity, "Failed to add product: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddProductProducerResponse>, t: Throwable) {
                    // Handle network failure
                    Toast.makeText(this@AddProdukProducerActivity, "Failed to connect to server: ${t.message}", Toast.LENGTH_SHORT).show()
                    t.printStackTrace() // Print the stack trace for debugging
                }
            })
        } ?: run {
            // Jika uploadedImageUrl masih null
            Toast.makeText(this@AddProdukProducerActivity, "Please upload an image first", Toast.LENGTH_SHORT).show()
        }
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

    private fun bitmapToFile(bitmap: Bitmap): File {
        val filesDir = applicationContext.filesDir
        val file = File(filesDir, "temp_image.jpg") // Ubah ekstensi file menjadi .jpg
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // Simpan dalam format JPEG
        outputStream.flush()
        outputStream.close()
        return file
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