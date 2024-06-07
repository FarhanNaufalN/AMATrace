package com.example.amatrace.pages.producer.ui.detail.rawProduk

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityRawProdukBinding
import com.example.amatrace.pages.adapter.ClaimDetailAdapter
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
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
                            displayClaims(productDetailResponse?.data?.product?.claims ?: emptyList())
                            productDetailResponse?.data?.product?.let { myPreference.saveProductDetail(it) }
                        } else {
                            // Handle unsuccessful response
                        }
                    }

                    override fun onFailure(call: Call<SupplierShippingDetailProducerResponse>, t: Throwable) {
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

    private fun displayClaims(claims: List<Claim>) {
        claimAdapter.claimList = claims
        claimAdapter.notifyDataSetChanged()
    }
}