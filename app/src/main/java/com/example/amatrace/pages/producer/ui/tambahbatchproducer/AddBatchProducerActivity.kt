package com.example.amatrace.pages.producer.ui.tambahbatchproducer

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityAddBatchProducerBinding
import com.example.amatrace.databinding.ActivityTambahPengirimanBinding
import com.example.amatrace.pages.supplier.ui.tambahpengiriman.QRCodeActivity
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.BatchResponse
import com.example.core.data.source.remote.response.GetProducerListResponse
import com.example.core.data.source.remote.response.Producers
import com.example.core.data.source.remote.response.Product
import com.example.core.data.source.remote.response.ProductListResponse
import com.example.core.data.source.remote.response.ProductionResponse
import com.example.core.data.source.remote.response.RawProduct
import com.example.core.data.source.remote.response.RawProductComposition
import com.example.core.data.source.remote.response.RawProductListResponse
import com.example.core.data.source.remote.response.ShippingRequest
import com.example.core.data.source.remote.response.ShippingResponse
import com.example.core.data.source.remote.response.getRawProductResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddBatchProducerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBatchProducerBinding
    private lateinit var myPreference: Preference
    private var productList: List<RawProduct>? = null
    private var producerList: List<Product>? = null
    private var selectedDeliveryDate: Calendar = Calendar.getInstance()
    private var selectedExpiredDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        myPreference = Preference(this)
        binding = ActivityAddBatchProducerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        updateExpiredDateText()


        binding = ActivityAddBatchProducerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val token = myPreference.getAccessToken()
        if (token != null) {
            Config.getApiService().getRawProduct(token).enqueue(object :
                Callback<RawProductListResponse> {
                override fun onResponse(call: Call<RawProductListResponse>, response: Response<RawProductListResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        productList = response.body()?.data?.rawProducts // Perbaiki di sini
                        if (!productList.isNullOrEmpty()) {
                            val productNames = mutableListOf(" Pilih Stok")
                            productNames.addAll(productList!!.map { it.product.name})
                            val adapter = ArrayAdapter(this@AddBatchProducerActivity, android.R.layout.simple_spinner_item, productNames)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerKomposisi.adapter = adapter
                        }
                    } else {
                        Toast.makeText(this@AddBatchProducerActivity, "Failed to fetch product list", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RawProductListResponse>, t: Throwable) {
                    Toast.makeText(this@AddBatchProducerActivity, t.message, Toast.LENGTH_SHORT).show()
                    println("Error: ${t.message}")
                }
            })
        }

        if (token != null) {
            Config.getApiService().getProducerProduct(token).enqueue(object :
                Callback<ProductListResponse> {
                override fun onResponse(call: Call<ProductListResponse>, response: Response<ProductListResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        producerList = response.body()?.data?.products // Perbaiki di sini
                        if (!producerList.isNullOrEmpty()) {
                            val productNames = mutableListOf(" Pilih Produk")
                            productNames.addAll(producerList!!.map { it.name })
                            val adapter = ArrayAdapter(this@AddBatchProducerActivity, android.R.layout.simple_spinner_item, productNames)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerProduct.adapter = adapter
                        }
                    } else {
                        Toast.makeText(this@AddBatchProducerActivity, "Failed to fetch product list", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                    Toast.makeText(this@AddBatchProducerActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

// Mendapatkan ID produk yang dipilih saat pengguna mengubah item di Spinner
        binding.spinnerKomposisi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0 && position < (productList?.size ?: 0)) {
                    val selectedProduct = productList?.get(position - 1)
                    val productId = selectedProduct?.id ?: ""
                    binding.etProductId.setText(productId)
                    binding.etProductId.visibility = View.GONE
                } else {
                    // Handle the case where the position is out of bounds
                    Log.e("TambahPengirimanActivity", "Invalid position: $position")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No action needed
            }
        }

        binding.spinnerProduct.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0 && position < (producerList?.size ?: 0)) {
                    val selectedProducer = producerList?.get(position - 1)
                    val producerId = selectedProducer?.id ?: ""
                    binding.etProducerIdDestination.setText(producerId)
                    binding.etProducerIdDestination.visibility = View.GONE
                } else {
                    // Handle the case where the position is out of bounds
                    Log.e("TambahPengirimanActivity", "Invalid position: $position")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No action needed
            }
        }

        binding.btnSubmit.setOnClickListener {
            val chosenProductId = binding.etProducerIdDestination.text.toString() // Assuming Spinner returns the ID as a string
            val batchName = binding.etBatchname.text.toString()
            val expiredAt = binding.etExpiredDate.text.toString()
            val lotsOfProduction = binding.etMass.text.toString().toIntOrNull() ?: 0
            val defaultRawProductId = "ea48c4c9-dc90-4b63-81d6-dfe8b424aafd"

            val rawProductsComposition = listOf(
                RawProductComposition(
                    rawProductId = binding.etProductId.text.toString(),// Assuming Spinner returns the ID as a string binding.etProductId.toString(),
                    manyUsed = binding.etManyuse.text.toString().toIntOrNull() ?: 0
                )
            )

            val productionRequest = ProductionResponse(
                choosenProductId = chosenProductId,
                batchName = batchName,
                expiredAt = expiredAt,
                lotsOfProduction = lotsOfProduction,
                rawProductsComposition = rawProductsComposition
            )

            val token = myPreference.getAccessToken()
            if (token != null) {
                val requestBody = createRequestBody(productionRequest)
                val call = Config.getApiService().getQRBatch(token, requestBody)
                call.enqueue(object : Callback<BatchResponse> {
                    override fun onResponse(call: Call<BatchResponse>, response: Response<BatchResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            val qrCodeData = response.body()?.data?.qrCode ?: ""
                            val id = response.body()?.data?.id ?: ""
                            println("QR Code Data: $qrCodeData")
                            println("ID Data: $id")
                            val intent = Intent(this@AddBatchProducerActivity, QRCodeBatchActivity::class.java)
                            intent.putExtra("QR_CODE_DATA", qrCodeData)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@AddBatchProducerActivity, "Failed to create production request", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<BatchResponse>, t: Throwable) {
                        Toast.makeText(this@AddBatchProducerActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@AddBatchProducerActivity, "Token is null", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker(calendar: Calendar, onDateSetListener: (Calendar) -> Unit) {
        val datePicker = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                onDateSetListener(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
    
    private fun updateExpiredDateText() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.etExpiredDate.setText(dateFormat.format(selectedExpiredDate.time))
    }

    private fun createRequestBody(productionRequest: ProductionResponse): RequestBody {
        val json = """
        {
            "choosenProductId": "${productionRequest.choosenProductId}",
            "batchName": "${productionRequest.batchName}",
            "expiredAt": "${productionRequest.expiredAt}",
            "lotsOfProduction": ${productionRequest.lotsOfProduction},
            "rawProductsComposition": [
                {
                    "rawProductId": "${productionRequest.rawProductsComposition[0].rawProductId}",
                    "manyUsed": ${productionRequest.rawProductsComposition[0].manyUsed}
                }
            ]
        }
    """.trimIndent()
        return json.toRequestBody("application/json".toMediaTypeOrNull())
    }


    fun onBackButtonClickedExpiredDate(view: View) {
        showDatePicker(selectedExpiredDate) { calendar ->
            selectedExpiredDate = calendar
            updateExpiredDateText()
        }
    }

}
