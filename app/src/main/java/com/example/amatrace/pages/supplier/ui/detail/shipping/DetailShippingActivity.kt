package com.example.amatrace.pages.supplier.ui.detail.shipping

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityDetailShippingBinding
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.ShippingDetail
import com.example.core.data.source.remote.response.SupplierProductShippingDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailShippingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailShippingBinding
    private lateinit var myPreference: Preference
    private lateinit var shippingStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailShippingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreference = Preference(this)
        val shipping = myPreference.getShippingDetail()
        shippingStatus = findViewById(R.id.statusPengiriman)
        shippingStatus.text = shipping?.status

        val bundle = intent.extras
        val shippingId = bundle?.getString("shipping_id")
        if (shippingId != null) {
            getDetailShipping(shippingId)
        }
        Glide.with(this)
            .load(intent.getStringExtra("list_image"))
            .into(binding.productImage)
        binding.productName.text = intent.getStringExtra("list_name")



    }

    private fun getDetailShipping(shippingId: String) {
        val token = myPreference.getAccessToken()
        if (token != null) {
            Config.getApiService().getSupplierShippingDetail(token, shippingId)
                .enqueue(object : Callback<SupplierProductShippingDetailResponse> {
                    override fun onResponse(
                        call: Call<SupplierProductShippingDetailResponse>,
                        response: Response<SupplierProductShippingDetailResponse>
                    ) {
                        if (response.isSuccessful) {
                            val productDetailResponse = response.body()
                            productDetailResponse?.let { displayProduct(it.data) }
                            response.body()?.data?.let { myPreference.saveShippingDetail(it) }

                        } else {
                            // Handle unsuccessful response
                        }
                    }

                    override fun onFailure(call: Call<SupplierProductShippingDetailResponse>, t: Throwable) {
                        // Handle failure
                    }
                })
        }
    }

    private fun displayProduct(product: ShippingDetail) {
        binding.productName.text = product.product.name
        binding.productSku.text = product.product.sku
    }

    fun onBackButtonClicked(view: View) {
        onBackPressed()
    }
}