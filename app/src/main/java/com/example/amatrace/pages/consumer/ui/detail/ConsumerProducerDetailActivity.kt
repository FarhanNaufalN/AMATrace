package com.example.amatrace.pages.consumer.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityConsumerProducerDetailBinding
import com.example.amatrace.pages.adapter.ImageGalleryAdapter
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.response.ConsumerProducerData
import com.example.core.data.source.remote.response.ConsumerProducerDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsumerProducerDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsumerProducerDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerProducerDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val producerId = intent.getStringExtra("PRODUCER_ID")
        producerId?.let {
            getProducerDetail(it)
        }
    }

    private fun getProducerDetail(producerId: String) {
        Config.getApiService().getConsumerProducerDetail(producerId)
            .enqueue(object : Callback<ConsumerProducerDetailResponse> {
                override fun onResponse(
                    call: Call<ConsumerProducerDetailResponse>,
                    response: Response<ConsumerProducerDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        val producerDetailResponse = response.body()
                        producerDetailResponse?.let {
                            displayProducerDetail(it.data)
                        }
                    } else {
                        Toast.makeText(this@ConsumerProducerDetailActivity, "Failed to get producer details", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ConsumerProducerDetailResponse>, t: Throwable) {
                    Toast.makeText(this@ConsumerProducerDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun displayProducerDetail(producerData: ConsumerProducerData) {
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
