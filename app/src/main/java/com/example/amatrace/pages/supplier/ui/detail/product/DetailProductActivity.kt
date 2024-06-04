package com.example.amatrace.pages.supplier.ui.detail.product

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityDetailProductBinding
import com.example.amatrace.pages.adapter.ClaimDetailAdapter
import com.example.amatrace.pages.supplier.MainSupplierActivity
import com.example.amatrace.pages.supplier.ui.detail.tambahclaim.TambahClaimActivity
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.Claim
import com.example.core.data.source.remote.response.ClaimList
import com.example.core.data.source.remote.response.ProductDetailData
import com.example.core.data.source.remote.response.ProductDetailSupplierResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProductBinding
    private lateinit var myPreference: Preference
    private var productId: String? = null
    private lateinit var productLocation: TextView
    private val claimAdapter = ClaimDetailAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
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
            val intent = Intent(this, TambahClaimActivity::class.java)
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
            Config.getApiService().getSupplierProductDetail(token, productId)
                .enqueue(object : Callback<ProductDetailSupplierResponse> {
                    override fun onResponse(
                        call: Call<ProductDetailSupplierResponse>,
                        response: Response<ProductDetailSupplierResponse>
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

                    override fun onFailure(call: Call<ProductDetailSupplierResponse>, t: Throwable) {
                        // Handle failure
                    }
                })
        }
    }

    private fun displayProductDetail(productDetail: ProductDetailData) {
        binding.productName.text = productDetail.name
        binding.productSku.text = productDetail.sku
        binding.productDescription.text = productDetail.description
    }

    private fun deleteDetailProduct(productId: String) {
        val token = myPreference.getAccessToken()
        if (token != null) {
            Config.getApiService().deleteSupplierProductDetail(token, productId)
                .enqueue(object : Callback<ProductDetailSupplierResponse> {
                    override fun onResponse(
                        call: Call<ProductDetailSupplierResponse>,
                        response: Response<ProductDetailSupplierResponse>
                    ) {
                        if (response.isSuccessful) {
                            val intent = Intent(this@DetailProductActivity, MainSupplierActivity::class.java)
                            startActivity(intent)
                        } else {
                            // Handle unsuccessful response
                        }
                    }

                    override fun onFailure(call: Call<ProductDetailSupplierResponse>, t: Throwable) {
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