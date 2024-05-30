package com.example.amatrace.pages.supplier.ui.tambahpengiriman

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahPengirimanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahPengirimanBinding
    private lateinit var myPreference: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        myPreference = Preference(this)

        binding = ActivityTambahPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                            generateQRCode(qrCodeData)
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

    private fun generateQRCode(data: String) {
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val smallerDimension = if (width < height) width else height

        val qrgEncoder = QRGEncoder(data, null, QRGContents.Type.TEXT, smallerDimension)
        qrgEncoder.colorBlack = Color.BLACK
        qrgEncoder.colorWhite = Color.WHITE

        try {
            val bitmap = qrgEncoder.bitmap
            binding.qrImageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.v("GenerateQRCode", e.toString())
        }
    }
}
