package com.example.amatrace.pages.consumer.ui.detail.klaim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityDetailKlaimBinding
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.SupplierDetailClaimResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKlaimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKlaimBinding
    private lateinit var apiService: API
    private lateinit var myPreferences: Preference
    private var productId: String? = null
    private var productClaimId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKlaimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = Config.getApiService()
        myPreferences = Preference(this) // Inisialisasi myPreferences sesuai implementasi Anda

        productClaimId = intent.getStringExtra("PRODUCT_CLAIM_ID")
        val productId = myPreferences.getConsumerScanDetail()?.product?.id

        println("Product ID: $productId")
        productId?.let { pid ->
            productClaimId?.let { pcid ->
                detailClaimProducer(pcid, pid)
            }
        }
    }

    private fun detailClaimProducer(productClaimId: String, productId: String) {
        val token = myPreferences.getAccessToken() ?: return
        val call = apiService.detailClaimProducerConsumer(productClaimId, productId)

        call.enqueue(object : Callback<SupplierDetailClaimResponse> {
            override fun onResponse(call: Call<SupplierDetailClaimResponse>, response: Response<SupplierDetailClaimResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val claimResponse = response.body()
                    val claimData = claimResponse?.data

                    claimData?.let { data ->
                        val claimTitle = data.claim.title
                        val claimDescription = data.claim.description
                        val claimIcon = data.claim.icon

                        binding.tvNamaclaim.text = claimTitle
                        binding.tvDeskripsi.text = claimDescription
                        Glide.with(this@DetailKlaimActivity) // Specify the activity context
                            .load(claimIcon)
                            .into(binding.imgClaim)
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
                println("Error: ${t.message}")
            }
        })
    }
}
