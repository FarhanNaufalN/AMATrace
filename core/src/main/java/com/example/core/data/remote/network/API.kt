package com.example.core.data.remote.network

import com.example.core.data.remote.response.AddProductSupplierResponse
import com.example.core.data.remote.response.LoginResponse
import com.example.core.data.remote.response.ProductListResponse
import com.example.core.data.remote.response.ProfileResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


interface API {

    @POST("stakeholder/login")
    fun loginUser(
        @Body requestBody: RequestBody
    ): Call<LoginResponse>

    @GET("supplier/profile")
    fun getProfileSupplier(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String
    ): Call<ProfileResponse>

    @PUT("supplier/profile")
    fun updateProfileSupplier(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Body requestBody: RequestBody
    ): Call<ProfileResponse>

    @POST("supplier/product")
    fun addProductSupplier(
        @Header("X-API-AUTH-SUPPLIER") token: String,
        @Body requestBody: HashMap<String, String>
    ): Call<AddProductSupplierResponse>

    @GET("supplier/products")
    fun getProductList(
        @Header("X-API-AUTH-SUPPLIER") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<ProductListResponse>

}
