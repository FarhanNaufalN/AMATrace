package com.example.amatrace.pages.supplier.ui.tambahpengiriman

import android.R
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.amatrace.databinding.ActivityTambahPengirimanBinding
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.ShippingRequest
import com.example.core.data.source.remote.response.ShippingResponse
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.example.core.data.source.remote.response.Product
import com.example.core.data.source.remote.response.ProductListResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahPengirimanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahPengirimanBinding
    private lateinit var myPreference: Preference
    private var productList: List<Product>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        myPreference = Preference(this)

        binding = ActivityTambahPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val token = myPreference.getAccessToken()
        if (token != null) {
            Config.getApiService().getSupplierProduct(token).enqueue(object : Callback<ProductListResponse> {
                override fun onResponse(call: Call<ProductListResponse>, response: Response<ProductListResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        val productList = response.body()?.data?.products
                        if (!productList.isNullOrEmpty()) {
                            val productNames = productList.map { it.name }
                            val adapter = ArrayAdapter(this@TambahPengirimanActivity, R.layout.simple_spinner_item, productNames)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerProduct.adapter = adapter
                        }
                    } else {
                        Toast.makeText(this@TambahPengirimanActivity, "Failed to fetch product list", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                    Toast.makeText(this@TambahPengirimanActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

// Mendapatkan ID produk yang dipilih saat pengguna mengubah item di Spinner
        binding.spinnerProduct.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedProduct = productList?.get(position)
                val productId = selectedProduct?.id ?: ""
                binding.etProductId.setText(productId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tidak melakukan apa-apa saat tidak ada item yang dipilih
            }
        }

        binding.btnSubmit.setOnClickListener {
            val productId = binding.etProductId.text.toString()
            val deliveryDate = binding.etDeliveryDate.text.toString()
            val expiredDate = binding.etExpiredDate.text.toString()
            val mass = binding.etMass.text.toString().toIntOrNull() ?: 0
            val producerIdDestination = binding.etProducerIdDestination.text.toString()
            val notes = binding.etNotes.text.toString()
            val serialNumber = binding.etSerialNumber.text.toString()

            val shippingRequest = ShippingRequest(
                productId = productId,
                deliveryDate = deliveryDate,
                expiredDate = expiredDate,
                mass = mass,
                producerIdDestination = producerIdDestination,
                notes = notes,
                serialNumber = serialNumber
            )

            val token = myPreference.getAccessToken()
            if (token != null) {
                val requestBody = createRequestBody(shippingRequest)
                val call = Config.getApiService().getQRShipping(token, requestBody)
                call.enqueue(object : Callback<ShippingResponse> {
                    override fun onResponse(call: Call<ShippingResponse>, response: Response<ShippingResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            val qrCodeData = response.body()?.data?.id ?: ""
                            val intent = Intent(this@TambahPengirimanActivity, QRCodeActivity::class.java)
                            intent.putExtra("QR_CODE_DATA", qrCodeData)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@TambahPengirimanActivity, "Failed to create shipping", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ShippingResponse>, t: Throwable) {
                        Toast.makeText(this@TambahPengirimanActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@TambahPengirimanActivity, "Token is null", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createRequestBody(shippingRequest: ShippingRequest): RequestBody {
        val json = """
            {
                "productId": "${shippingRequest.productId}",
                "deliveryDate": "${shippingRequest.deliveryDate}",
                "expiredDate": "${shippingRequest.expiredDate}",
                "mass": ${shippingRequest.mass},
                "producerIdDestination": "${shippingRequest.producerIdDestination}",
                "notes": "${shippingRequest.notes}",
                "serialNumber": "${shippingRequest.serialNumber}"
            }
        """.trimIndent()
        return json.toRequestBody("application/json".toMediaTypeOrNull())
    }

}
