package com.example.amatrace.pages.consumer.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.amatrace.databinding.ActivityConsumerSupplierBinding
import com.example.amatrace.pages.adapter.ImageGalleryAdapter
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.response.ConsumerProducerData
import com.example.core.data.source.remote.response.ConsumerSupplierData
import com.example.core.data.source.remote.response.ConsumerSupplierDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsumerSupplierDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsumerSupplierBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerSupplierBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val producerId = intent.getStringExtra("PRODUCT_ID")
        if (producerId != null) {
            getProducerDetail(producerId)
        } else {
            Toast.makeText(this, "No producer ID provided", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getProducerDetail(producerId: String) {
        Config.getApiService().getConsumerSupplierDetail(producerId)
            .enqueue(object : Callback<ConsumerSupplierDetailResponse> {
                override fun onResponse(
                    call: Call<ConsumerSupplierDetailResponse>,
                    response: Response<ConsumerSupplierDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        val producerDetailResponse = response.body()
                        producerDetailResponse?.let {
                            displaySupplierDetail(it.data)
                        }
                    } else {
                        Toast.makeText(this@ConsumerSupplierDetailActivity, "Failed to get producer details", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ConsumerSupplierDetailResponse>, t: Throwable) {
                    Toast.makeText(this@ConsumerSupplierDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun displaySupplierDetail(producerData: ConsumerSupplierData) {
        binding.productName.text = producerData.businessName
        binding.productSku.text = producerData.ownerName
        binding.EmailSupplier.text = producerData.email
        binding.etDeliveryDate.text = producerData.noHp
        binding.productLocation.text = producerData.address

        Glide.with(this)
            .load(producerData.heroImage)
            .into(binding.producerheroimage)

        setupImageGallery(producerData.galleryImages)
    }

    private fun setupImageGallery(imageUrls: List<String>) {
        val imageGalleryAdapter = ImageGalleryAdapter(imageUrls)
        binding.rvImagegalery.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImagegalery.adapter = imageGalleryAdapter
    }

    fun onBackButtonClicked(view: View) {
        onBackPressed()
    }
}
