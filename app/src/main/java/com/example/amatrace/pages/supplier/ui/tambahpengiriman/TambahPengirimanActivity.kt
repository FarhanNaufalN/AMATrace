package com.example.amatrace.pages.supplier.ui.tambahpengiriman

import android.R
import android.app.DatePickerDialog
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
import com.example.core.data.source.remote.response.GetProducerListResponse
import com.example.core.data.source.remote.response.Producers
import com.example.core.data.source.remote.response.Product
import com.example.core.data.source.remote.response.ProductListResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TambahPengirimanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahPengirimanBinding
    private lateinit var myPreference: Preference
    private var productList: List<Product>? = null
    private var producerList: List<Producers>? = null
    private var selectedDeliveryDate: Calendar = Calendar.getInstance()
    private var selectedExpiredDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        myPreference = Preference(this)
        binding = ActivityTambahPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateDeliveryDateText()
        updateExpiredDateText()


        binding = ActivityTambahPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val token = myPreference.getAccessToken()
        if (token != null) {
            Config.getApiService().getSupplierProduct(token).enqueue(object : Callback<ProductListResponse> {
                override fun onResponse(call: Call<ProductListResponse>, response: Response<ProductListResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        productList = response.body()?.data?.products // Perbaiki di sini
                        if (!productList.isNullOrEmpty()) {
                            val productNames = mutableListOf(" Pilih Produk")
                            productNames.addAll(productList!!.map { it.name })
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

        if (token != null) {
            Config.getApiService().getProducerList(token).enqueue(object : Callback<GetProducerListResponse> {
                override fun onResponse(call: Call<GetProducerListResponse>, response: Response<GetProducerListResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        producerList = response.body()?.data?.producers // Perbaiki di sini
                        if (!producerList.isNullOrEmpty()) {
                            val productNames = mutableListOf(" Pilih Tujuan")
                            productNames.addAll(producerList!!.map { it.businessName })
                            val adapter = ArrayAdapter(this@TambahPengirimanActivity, R.layout.simple_spinner_item, productNames)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerProducer.adapter = adapter
                        }
                    } else {
                        Toast.makeText(this@TambahPengirimanActivity, "Failed to fetch product list", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<GetProducerListResponse>, t: Throwable) {
                    Toast.makeText(this@TambahPengirimanActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

// Mendapatkan ID produk yang dipilih saat pengguna mengubah item di Spinner
        binding.spinnerProduct.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        binding.spinnerProducer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    private fun updateDeliveryDateText() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.etDeliveryDate.setText(dateFormat.format(selectedDeliveryDate.time))
    }

    private fun updateExpiredDateText() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.etExpiredDate.setText(dateFormat.format(selectedExpiredDate.time))
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

    fun onBackButtonClickedDeliveryDate(view: View) {
        showDatePicker(selectedDeliveryDate) { calendar ->
            selectedDeliveryDate = calendar
            updateDeliveryDateText()
        }
    }
    fun onBackButtonClickedExpiredDate(view: View) {
        showDatePicker(selectedExpiredDate) { calendar ->
            selectedExpiredDate = calendar
            updateExpiredDateText()
        }
    }

}
