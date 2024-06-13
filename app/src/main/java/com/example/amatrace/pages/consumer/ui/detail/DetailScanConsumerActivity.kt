package com.example.amatrace.pages.consumer.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.amatrace.databinding.ActivityDetailScanConsumerBinding
import com.example.amatrace.pages.adapter.ConsumerClaimDetailAdapter
import com.example.amatrace.pages.adapter.ConsumerRawProdukAdapter
import com.example.amatrace.pages.consumer.ConsumerMainActivity
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.ConsumerClaim
import com.example.core.data.source.remote.response.ConsumerGetDataResponse
import com.example.core.data.source.remote.response.ConsumerProducer
import com.example.core.data.source.remote.response.ConsumerProduct
import com.example.core.data.source.remote.response.ConsumerRawProduct
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailScanConsumerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailScanConsumerBinding
    private lateinit var myPreference: Preference
    private lateinit var claimAdapter: ConsumerClaimDetailAdapter
    private lateinit var rawproductAdapter: ConsumerRawProdukAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailScanConsumerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreference = Preference(this)
        val bundle = intent.extras
        val supplierShippingQrCode = bundle?.getString("SCAN_RESULT")


        claimAdapter = ConsumerClaimDetailAdapter(this, emptyList(),myPreference)
        binding.rvClaims.layoutManager = LinearLayoutManager(this)
        binding.rvClaims.adapter = claimAdapter

        rawproductAdapter = ConsumerRawProdukAdapter(emptyList())
        binding.rvPertanian.layoutManager = LinearLayoutManager(this)
        binding.rvPertanian.adapter = rawproductAdapter

        if (supplierShippingQrCode != null) {
            val uniqueCode = extractUniqueCode(supplierShippingQrCode)
            getDetailProduct(uniqueCode)
        }

        binding.detailProductButton.setOnClickListener {
            toggleVisibility(binding.detailProductCard)
        }

        binding.prosesProduksiButton.setOnClickListener {
            toggleVisibility(binding.prosesProduksiCard)
        }

        binding.labelProductButton.setOnClickListener {
            toggleVisibility(binding.labelProductCard)
        }

        binding.pertanianButton.setOnClickListener{
            toggleVisibility(binding.pertanianCard)
        }
    }

    private fun getDetailProduct(productBatchProductQrCode: String) {
        Config.getApiService().getConsumerProductDetail(productBatchProductQrCode)
            .enqueue(object : Callback<ConsumerGetDataResponse> {
                override fun onResponse(
                    call: Call<ConsumerGetDataResponse>,
                    response: Response<ConsumerGetDataResponse>
                ) {
                    if (response.isSuccessful) {
                        val productDetailResponse = response.body()
                        productDetailResponse?.let {
                            myPreference.saveConsumerScanDetail(it.data)
                            displayProductDetail(it.data.product)
                            displayProducerDetail(it.data.producer)
                            displayRawProduk(it.data.rawProducts)
                            displayClaims(it.data.claims)
                            displayClaims(it.data.claims)
                            val producerId = it.data.producer.id
                            println("Product ID: ${it.data.product.id}") // Extract the producer id
                            binding.lihatdetailproducer.setOnClickListener {
                                val intent = Intent(this@DetailScanConsumerActivity, ConsumerProducerDetailActivity::class.java)
                                intent.putExtra("PRODUCER_ID", producerId) // Add the producer id as an extra
                                startActivity(intent)
                            }
                        }

                    } else {
                        Toast.makeText(this@DetailScanConsumerActivity, "Failed to get product details", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ConsumerGetDataResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(this@DetailScanConsumerActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun displayProductDetail(productDetail: ConsumerProduct) {
        binding.productName.text = productDetail.name
        binding.detailProductName.text = productDetail.name
        binding.productDescription.text = productDetail.description
        binding.isiKomp.text = productDetail.ingredients
        Glide.with(this)
            .load(productDetail.image)
            .into(binding.productImage)
    }

    private fun displayProducerDetail(rawProducts: ConsumerProducer) {
        binding.asalProducer.text = rawProducts.businessName
        binding.supplierAddres.text = rawProducts.address
        Glide.with(this)
            .load(rawProducts.avatar)
            .into(binding.supplierImage)
    }

    private fun displayClaims(claims: List<ConsumerClaim>) {
        claimAdapter.claimList = claims
        claimAdapter.notifyDataSetChanged()
    }

    private fun displayRawProduk(claims: List<ConsumerRawProduct>) {
        rawproductAdapter.claimList = claims
        rawproductAdapter.notifyDataSetChanged()
    }

    private fun toggleVisibility(cardView: View) {
        cardView.visibility = if (cardView.visibility == View.GONE) View.VISIBLE else View.GONE
    }

    private fun extractUniqueCode(url: String): String {
        return url.substringAfterLast("/")
    }

    fun onBackButtonClicked(view: View) {
        val intent = Intent(this, ConsumerMainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
