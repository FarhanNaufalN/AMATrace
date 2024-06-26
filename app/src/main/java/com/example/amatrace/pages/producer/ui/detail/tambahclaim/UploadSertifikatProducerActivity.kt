package com.example.amatrace.pages.producer.ui.detail.tambahclaim

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.amatrace.databinding.ActivityUploadSertifikatBinding
import com.example.amatrace.databinding.ActivityUploadSertifikatProducerBinding
import com.example.amatrace.pages.producer.ProducerMainActivity
import com.example.amatrace.pages.supplier.MainSupplierActivity
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.SertifClaimLinkResponse
import com.example.core.data.source.remote.response.SertifClaimProducerLinkResponse
import com.example.core.data.source.remote.response.SertifClaimResponse
import com.example.core.data.source.remote.response.SupplierDetailClaimResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class UploadSertifikatProducerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadSertifikatProducerBinding
    private lateinit var apiService: API
    private lateinit var myPreferences: Preference
    private var productId: String? = null
    private var productClaimId: String? = null
    private var tempFile: File? = null
    private var uploadedImageUrl: String? = null

    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.also { uri ->
                val fileName = getFileName(uri)
                binding.selectedFileTextView.text = "Selected file: $fileName"

                val outputFile = saveFileFromUri(uri)
                if (outputFile != null) {
                    val uploadedFilePath = "/data/user/0/com.example.amatrace/cache/uploads/Farhan Naufal Nurdiansyah.pdf"
                    val fileName = uploadedFilePath.substringAfterLast('/')
                    println("File uploaded: $fileName")
                    uploadFile(outputFile)
                    println("File uploaded: $outputFile")
                } else {
                    Toast.makeText(this, "Failed to save file", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveFileFromUri(uri: Uri): File? {
        val contentResolver = applicationContext.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val fileName = getFileName(uri)

        try {
            val outputDir = File(cacheDir, "uploads")
            if (!outputDir.exists()) outputDir.mkdirs()

            val outputFile = File(outputDir, fileName)
            FileOutputStream(outputFile).use { output ->
                inputStream?.copyTo(output)
            }
            return outputFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadSertifikatProducerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi myPreferences sebelum digunakan
        myPreferences = Preference(this)
        apiService = Config.getApiService()

        val bundle = intent.extras
        val productDetail = myPreferences.getProductDetail()
        val account = myPreferences.getAccountInfo()
        productId = productDetail?.id
        productClaimId = bundle?.getString("productClaim_id")
        val icon = bundle?.getString("icon")
        val listName = bundle?.getString("list_name")
        binding.tvNamaclaim.text = listName

        Glide.with(this@UploadSertifikatProducerActivity)
            .load(icon)
            .into(binding.imgClaim)

        // Panggil detailClaimProducer setelah productId dan productClaimId diinisialisasi
        productId?.let { productClaimId?.let { it1 -> detailClaimProducer(it1, it) } }

        binding.uploadFileButton.setOnClickListener {
            openFilePicker()
        }

        binding.uploadButton.setOnClickListener {
            uploadLinkSertif(uploadedImageUrl)
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        pickFileLauncher.launch(intent)
    }

    private fun getFileName(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        } ?: "Unknown"
    }

    private fun uploadFile(tempFile: File?) {
        val token = myPreferences.getAccessToken() ?: return
        tempFile ?: return
        val fileName = tempFile.name
        val requestBody = tempFile.asRequestBody("application/pdf".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", fileName, requestBody)
        apiService.uploadSertifProducer(token, filePart).enqueue(object : Callback<SertifClaimProducerLinkResponse> {
            override fun onResponse(
                call: Call<SertifClaimProducerLinkResponse>,
                response: Response<SertifClaimProducerLinkResponse>
            ) {
                if (response.isSuccessful) {
                    uploadedImageUrl = response.body()?.data?.image
                    println(uploadedImageUrl)
                    Toast.makeText(
                        this@UploadSertifikatProducerActivity,
                        "Upload successful",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@UploadSertifikatProducerActivity,
                        "Upload failed: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SertifClaimProducerLinkResponse>, t: Throwable) {
                Toast.makeText(
                    this@UploadSertifikatProducerActivity,
                    "Upload error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun uploadLinkSertif(uploadedImageUrl: String?) {
        uploadedImageUrl?.let { imageUrl ->
            val token = myPreferences.getAccessToken() ?: return
            val productClaimId = this.productClaimId ?: return
            val productId = this.productId ?: return

            val json = """
            {
                "evidenceFile": "$imageUrl"
            }
            """.trimIndent()

            val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

            val call = apiService.uploadLinkSertifProducer(token, productClaimId, productId, requestBody)

            call.enqueue(object : Callback<SertifClaimResponse> {
                override fun onResponse(call: Call<SertifClaimResponse>, response: Response<SertifClaimResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(this@UploadSertifikatProducerActivity, "Link upload successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@UploadSertifikatProducerActivity, ProducerMainActivity::class.java))
                    } else {
                        val errorMessage = response.message() ?: "Unknown error"
                        Toast.makeText(this@UploadSertifikatProducerActivity, "Failed to upload link: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SertifClaimResponse>, t: Throwable) {
                    Toast.makeText(this@UploadSertifikatProducerActivity, "Upload error: ${t.message}", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
        } ?: run {
            Toast.makeText(this@UploadSertifikatProducerActivity, "Please upload a PDF first", Toast.LENGTH_SHORT).show()
        }
    }
    private fun detailClaimProducer(productClaimId: String, productId: String) {
        val token = myPreferences.getAccessToken() ?: return
        val call = apiService.detailClaimProducer(token, productClaimId, productId)

        call.enqueue(object : Callback<SupplierDetailClaimResponse> {
            override fun onResponse(call: Call<SupplierDetailClaimResponse>, response: Response<SupplierDetailClaimResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val claimResponse = response.body()
                    // Handle data from response here
                    val claimData = claimResponse?.data

                    claimData?.let { data ->
                        // Use data here
                        val claimTitle = data.claim.name
                        val claimDescription = data.claim.description

                        binding.tvDeskripsi.text = claimDescription

                        // Log the details to verify
                        println("Claim Title: $claimTitle")
                        println("Claim Description: $claimDescription")
                    } ?: run {
                        println("Claim data is null")
                    }
                } else {
                    val errorMessage = response.message() ?: "Unknown error"
                    println("Failed to get claim details: $errorMessage")
                }
            }

            override fun onFailure(call: Call<SupplierDetailClaimResponse>, t: Throwable) {
                println("API call failed: ${t.message}")
                t.printStackTrace()
            }
        })
    }

}



