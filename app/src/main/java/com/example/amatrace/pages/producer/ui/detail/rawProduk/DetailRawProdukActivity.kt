package com.example.amatrace.pages.producer.ui.detail.rawProduk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityDetailRawProdukBinding
import com.example.amatrace.pages.adapter.ClaimDetailAdapter
import com.example.amatrace.pages.consumer.ui.detail.adapter.RawClaimDetailAdapter
import com.example.amatrace.pages.producer.ProducerMainActivity
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.Claim
import com.example.core.data.source.remote.response.DetailRawProductResponse
import com.example.core.data.source.remote.response.RawClaim
import com.example.core.data.source.remote.response.RawDetailProduct
import com.example.core.data.source.remote.response.ShippingInfo
import com.example.core.data.source.remote.response.Supplier
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailRawProdukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRawProdukBinding
    private lateinit var myPreference: Preference
    private lateinit var productLocation: TextView
    private val claimAdapter = ClaimDetailAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailRawProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreference = Preference(this)
        val account = myPreference.getAccountInfo()
        productLocation = findViewById(R.id.productLocation)
        productLocation.text = account?.businessName

        binding.rvClaim.adapter = claimAdapter
        binding.rvClaim.layoutManager = LinearLayoutManager(this)

        val bundle = intent.extras
        val productId = bundle?.getString("product_id")
        if (productId != null) {
            getDetailProduct(productId)
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
            Config.getApiService().getProducerRawProductDetail(token, productId)
                .enqueue(object : Callback<DetailRawProductResponse> {
                    override fun onResponse(
                        call: Call<DetailRawProductResponse>,
                        response: Response<DetailRawProductResponse>
                    ) {
                        if (response.isSuccessful) {
                            val productDetailResponse = response.body()
                            productDetailResponse?.let { displayProductDetail(it.data.product) }
                            productDetailResponse?.data?.let { displayClaims(it.product.claims) }
                            productDetailResponse?.data?.supplier?.let {
                                displaySupplierProductDetail(
                                    it
                                )
                                displayShipProductDetail(productDetailResponse.data.shippingInfo)
                                binding.etExpiredDate.text = productDetailResponse.data.expiredAt
                                binding.etMass.text = productDetailResponse.data.remainingStock.toString()
                            }
                        } else {
                            // Handle unsuccessful response
                        }
                    }

                    override fun onFailure(call: Call<DetailRawProductResponse>, t: Throwable) {
                        // Handle failure
                    }
                })
        }
    }

    private fun displayProductDetail(productDetail: RawDetailProduct) {
        binding.productSku.text = productDetail.sku
        binding.productDescription.text = productDetail.description
    }

    private fun displaySupplierProductDetail(productDetail: Supplier) {
        binding.productLocation.text = productDetail.businessName
        binding.editTextAddress.text = productDetail.address
        binding.EmailSupplier.text = productDetail.noHp
    }

    private fun displayShipProductDetail(productDetail: ShippingInfo) {
        binding.etDeliveryDate.text = productDetail.deliveredBySupplierAt
    }


    private fun displayClaims(claims: List<Claim>?) {
        if (claims != null) {
            claimAdapter.claimList = claims
            claimAdapter.notifyDataSetChanged()
        } else {
            println("Claims list is null")
        }
    }


    fun onBackButtonClicked(view: View) {
        val intent = Intent(this, ProducerMainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
