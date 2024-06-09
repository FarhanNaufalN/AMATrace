package com.example.amatrace.pages.producer.ui.detail.rawProduk

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityRawProdukBinding
import com.example.amatrace.pages.adapter.ClaimDetailAdapter
import com.example.amatrace.pages.producer.ProducerMainActivity
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.AddProductProducerResponse
import com.example.core.data.source.remote.response.AddRawProductResponse
import com.example.core.data.source.remote.response.Claim
import com.example.core.data.source.remote.response.ProductDetailData
import com.example.core.data.source.remote.response.ProductDetailProducerResponse
import com.example.core.data.source.remote.response.ResponseDataRawProduct
import com.example.core.data.source.remote.response.SupplierShippingDetailProducerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RawProdukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRawProdukBinding
    private lateinit var myPreference: Preference
    private var productId: String? = null
    private val claimAdapter = ClaimDetailAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRawProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreference = Preference(this)
        val account = myPreference.getAccountInfo()

        val bundle = intent.extras
        val supplierShippingQrCode = bundle?.getString("SCAN_RESULT")

        binding.idShipping.setText(supplierShippingQrCode)

        if (supplierShippingQrCode != null) {
            getDetailProduct(supplierShippingQrCode)
        }
        println("Kode QR: $supplierShippingQrCode")

        binding.buttonTambahStok.setOnClickListener {
            if (supplierShippingQrCode != null) {
                addProduct(supplierShippingQrCode)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        // Lihat apakah ada kode QR yang dipindai
        val bundle = intent.extras
        val supplierShippingQrCode = bundle?.getString("SCAN_RESULT")
        if (supplierShippingQrCode != null) {
            getDetailProduct(supplierShippingQrCode)
        }
    }

    private fun getDetailProduct(supplierShippingQrCode: String) {
        val token = myPreference.getAccessToken()
        if (token != null) {
            Config.getApiService().getProducerProductDetailSupplier(token, supplierShippingQrCode)
                .enqueue(object : Callback<SupplierShippingDetailProducerResponse> {
                    override fun onResponse(
                        call: Call<SupplierShippingDetailProducerResponse>,
                        response: Response<SupplierShippingDetailProducerResponse>
                    ) {
                        if (response.isSuccessful) {
                            val productDetailResponse = response.body()
                            productDetailResponse?.let { displayProductDetail(it.data.product) }
                            displayClaims(
                                productDetailResponse?.data?.product?.claims ?: emptyList()
                            )
                            productDetailResponse?.data?.product?.let {
                                myPreference.saveProductDetail(
                                    it
                                )
                            }
                        } else {
                            // Handle unsuccessful response
                        }
                    }

                    override fun onFailure(
                        call: Call<SupplierShippingDetailProducerResponse>,
                        t: Throwable
                    ) {
                        // Handle failure
                    }
                })
        }
    }

    private fun addProduct(supplierShippingQrCode: String) {
        val token = myPreference.getAccessToken() ?: return

        val requestBodyMap = hashMapOf(
            "supplierShippingId" to supplierShippingQrCode
        )

        val call = Config.getApiService().addStokProducer(token, requestBodyMap)

        call.enqueue(object : Callback<AddRawProductResponse> {
            override fun onResponse(call: Call<AddRawProductResponse>, response: Response<AddRawProductResponse>) {
                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null) {
                        Toast.makeText(this@RawProdukActivity, "Product added successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RawProdukActivity, ProducerMainActivity::class.java))
                        finish() // Finish current activity after starting new activity
                    }
                } else {
                    // Handle unsuccessful response
                    val errorMessage = response.message() ?: "Unknown error"
                    Toast.makeText(this@RawProdukActivity, "Failed to add product: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddRawProductResponse>, t: Throwable) {
                // Handle network failure
                Toast.makeText(this@RawProdukActivity, "Failed to connect to server: ${t.message}", Toast.LENGTH_SHORT).show()
                t.printStackTrace() // Print the stack trace for debugging
            }
        })
    }



    private fun displayProductDetail(productDetail: ProductDetailData) {
        binding.productName.text = productDetail.name
        binding.productSku.text = productDetail.sku
        binding.productDescription.text = productDetail.description
        Glide.with(this)
            .load(productDetail.image)
            .into(binding.productImage)
    }


    private fun displayClaims(claims: List<Claim>) {
        claimAdapter.claimList = claims
        claimAdapter.notifyDataSetChanged()
    }
}