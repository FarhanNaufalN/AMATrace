package com.example.core.data.repository

import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.response.ProductDetailSupplierResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailRepository(private val api: API) {

    // Function to fetch product detail from the API
    fun fetchProductDetail(
        accessToken: String,
        productId: String,
        onSuccess: (ProductDetailSupplierResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        // Make an API call to fetch product detail
        api.getSupplierProductDetail(accessToken, productId).enqueue(object : Callback<ProductDetailSupplierResponse> {
            override fun onResponse(call: Call<ProductDetailSupplierResponse>, response: Response<ProductDetailSupplierResponse>) {
                if (response.isSuccessful) {
                    onSuccess(response.body()!!)
                } else {
                    onError("Failed to fetch product detail: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProductDetailSupplierResponse>, t: Throwable) {
                onError("Request failed with exception: ${t.message}")
            }
        })
    }
}
