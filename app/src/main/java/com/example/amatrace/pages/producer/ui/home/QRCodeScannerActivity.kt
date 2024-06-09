package com.example.amatrace.pages.producer.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.amatrace.R
import com.example.amatrace.pages.producer.ui.detail.rawProduk.RawProdukActivity
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.SupplierShippingDetailProducerResponse
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QRCodeScannerActivity : AppCompatActivity() {
    private lateinit var myPreference: Preference
    private var lastScanResult: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreference = Preference(this)
        // Mulai pemindaian QR code
        startQRScanner()
    }

    private fun startQRScanner() {
        IntentIntegrator(this).apply {
            setPrompt("Scan a QR code")
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
            initiateScan()
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
                            productDetailResponse?.let {
                                myPreference.saveShippingScanDetail(it.data)

                                // Start RawProdukActivity with the scan result
                                val scanResultIntent = Intent(this@QRCodeScannerActivity, RawProdukActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    putExtra("SCAN_RESULT", supplierShippingQrCode)
                                }
                                startActivity(scanResultIntent)
                            }
                        } else {
                            // Handle unsuccessful response
                            Toast.makeText(this@QRCodeScannerActivity, "Failed to get product details", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<SupplierShippingDetailProducerResponse>,
                        t: Throwable
                    ) {
                        // Handle failure
                        Toast.makeText(this@QRCodeScannerActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        println("Error: ${t.message}")
                    }
                })
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                // Jika pembacaan QR code tidak berhasil
                Toast.makeText(this, "QR Code scanning cancelled", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_CANCELED)
            } else {
                // Jika pembacaan QR code berhasil
                if (result.contents != lastScanResult) {
                    // Jika hasil pemindaian QR code baru
                    lastScanResult = result.contents
                    getDetailProduct(result.contents)
                } else {
                    // Jika hasil pemindaian QR code sama dengan sebelumnya
                    Toast.makeText(this, "Scanned QR Code is the same as previous one", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
