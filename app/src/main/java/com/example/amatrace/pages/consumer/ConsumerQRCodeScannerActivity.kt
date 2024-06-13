package com.example.amatrace.pages.consumer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.amatrace.R
import com.example.amatrace.pages.consumer.ui.detail.DetailScanConsumerActivity
import com.example.amatrace.pages.producer.ui.detail.rawProduk.RawProdukActivity
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.ConsumerGetDataResponse
import com.example.core.data.source.remote.response.SupplierShippingDetailProducerResponse
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsumerQRCodeScannerActivity : AppCompatActivity() {
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

    private fun extractUniqueCodeFromUrl(url: String): String {
        // Mencari indeks terakhir dari karakter "/" untuk memisahkan URL dan kode unik
        val lastIndexOfSlash = url.lastIndexOf("/")
        // Memeriksa apakah "/" ditemukan
        return if (lastIndexOfSlash != -1 && lastIndexOfSlash < url.length - 1) {
            // Mengambil kode unik setelah "/"
            url.substring(lastIndexOfSlash + 1)
        } else {
            // Jika tidak ada "/", mengembalikan URL secara keseluruhan
            url
        }
    }

    private fun getDetailProduct(productBatchProductQrCode: String) {
        // Panggil endpoint untuk mendapatkan detail produk berdasarkan kode unik
        Config.getApiService().getConsumerProductDetail(productBatchProductQrCode)
            .enqueue(object : Callback<ConsumerGetDataResponse> {
                override fun onResponse(
                    call: Call<ConsumerGetDataResponse>,
                    response: Response<ConsumerGetDataResponse>
                ) {
                    if (response.isSuccessful) {
                        val productDetailResponse = response.body()
                        productDetailResponse?.let {
                            myPreference.saveConsumerScanDetail(it.data)

                            // Start RawProdukActivity with the scan result
                            val scanResultIntent = Intent(this@ConsumerQRCodeScannerActivity, DetailScanConsumerActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                putExtra("SCAN_RESULT", productBatchProductQrCode)
                            }
                            startActivity(scanResultIntent)
                        }
                    } else {
                        // Handle unsuccessful response
                        Toast.makeText(this@ConsumerQRCodeScannerActivity, "QR CODE UNVALID", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ConsumerGetDataResponse>,
                    t: Throwable
                ) {
                    // Handle failure
                    Toast.makeText(this@ConsumerQRCodeScannerActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    println("Error: ${t.message}")
                }
            })
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
                intent = Intent(this, ConsumerMainActivity::class.java)
                startActivity(intent)
            } else {
                // Jika pembacaan QR code berhasil
                if (result.contents != lastScanResult) {
                    // Jika hasil pemindaian QR code baru
                    lastScanResult = result.contents
                    // Ekstrak kode unik dari URL
                    val uniqueCode = extractUniqueCodeFromUrl(result.contents)
                    // Panggil fungsi untuk mendapatkan detail produk berdasarkan kode unik
                    getDetailProduct(uniqueCode)
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
