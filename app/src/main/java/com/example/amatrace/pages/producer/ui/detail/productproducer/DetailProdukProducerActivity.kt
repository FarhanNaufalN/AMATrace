package com.example.amatrace.pages.producer.ui.detail.productproducer

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityDetailProdukProducerBinding
import com.example.amatrace.pages.adapter.ClaimDetailAdapter
import com.example.amatrace.pages.producer.ProducerMainActivity
import com.example.amatrace.pages.producer.ui.detail.tambahclaim.TambahClaimProducerActivity
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.Claim
import com.example.core.data.source.remote.response.ProductDetailData
import com.example.core.data.source.remote.response.ProductDetailProducerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProdukProducerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProdukProducerBinding
    private lateinit var myPreference: Preference
    private var productId: String? = null
    private lateinit var productLocation: TextView
    private val claimAdapter = ClaimDetailAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailProdukProducerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreference = Preference(this)
        val account = myPreference.getAccountInfo()
        productLocation = findViewById(R.id.productLocation)
        productLocation.text = account?.businessName

        val bundle = intent.extras
        val productId = bundle?.getString("product_id")
        if (productId != null) {
            getDetailProduct(productId)
        }

        binding.buttonDelete.setOnClickListener {
            if (productId != null) {
                deleteDetailProduct(productId)
            }
        }

        binding.rvClaim.adapter = claimAdapter
        binding.rvClaim.layoutManager = LinearLayoutManager(this)

        binding.buttonTambahClaim.setOnClickListener {
            val intent = Intent(this, TambahClaimProducerActivity::class.java)
            intent.putExtra("product_id", productId)
            startActivity(intent)
        }

        Glide.with(this)
            .load(intent.getStringExtra("list_image"))
            .into(binding.productImage)
        binding.productName.text = intent.getStringExtra("list_name")
        binding.productSku.text = intent.getStringExtra("list_sku")
    }

    private fun getDetailProduct(productId: String) {
        val token = myPreference.getAccessToken()
        if (token != null) {
            Config.getApiService().getProducerProductDetail(token, productId)
                .enqueue(object : Callback<ProductDetailProducerResponse> {
                    override fun onResponse(
                        call: Call<ProductDetailProducerResponse>,
                        response: Response<ProductDetailProducerResponse>
                    ) {
                        if (response.isSuccessful) {
                            val productDetailResponse = response.body()
                            productDetailResponse?.let { displayProductDetail(it.data) }
                            displayClaims(productDetailResponse?.data?.claims ?: emptyList())
                            productDetailResponse?.data?.let { myPreference.saveProductDetail(it) }
                        } else {
                            // Handle unsuccessful response
                        }
                    }

                    override fun onFailure(call: Call<ProductDetailProducerResponse>, t: Throwable) {
                        // Handle failure
                    }
                })
        }
    }

    private fun displayProductDetail(productDetail: ProductDetailData) {
        binding.productName.text = productDetail.name
        binding.productSku.text = productDetail.sku
        binding.productDescription.text = productDetail.description
        binding.productIngredients.text = productDetail.ingredients
    }

    private fun deleteDetailProduct(productId: String) {
        val token = myPreference.getAccessToken()
        if (token != null) {
            Config.getApiService().deleteProducerProductDetail(token, productId)
                .enqueue(object : Callback<ProductDetailProducerResponse> {
                    override fun onResponse(
                        call: Call<ProductDetailProducerResponse>,
                        response: Response<ProductDetailProducerResponse>
                    ) {
                        if (response.isSuccessful) {
                            val intent = Intent(this@DetailProdukProducerActivity, ProducerMainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // Handle unsuccessful response
                        }
                    }

                    override fun onFailure(call: Call<ProductDetailProducerResponse>, t: Throwable) {
                        // Handle failure
                    }
                })
        }
    }

    private fun displayClaims(claims: List<Claim>) {
        claimAdapter.claimList = claims
        claimAdapter.notifyDataSetChanged()
    }


}