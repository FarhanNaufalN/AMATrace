package com.example.core.data.source.remote.network

import com.example.core.data.source.remote.response.AddProductProducer
import com.example.core.data.source.remote.response.AddProductProducerResponse
import com.example.core.data.source.remote.response.AddProductSupplierResponse
import com.example.core.data.source.remote.response.AddRawProductResponse
import com.example.core.data.source.remote.response.GetProducerListResponse
import com.example.core.data.source.remote.response.GetShippingListResponse
import com.example.core.data.source.remote.response.LoginResponse
import com.example.core.data.source.remote.response.ProductDetailProducerResponse
import com.example.core.data.source.remote.response.ProductDetailSupplierResponse
import com.example.core.data.source.remote.response.ProductListResponse
import com.example.core.data.source.remote.response.ProfileProducerResponse
import com.example.core.data.source.remote.response.ProfileResponse
import com.example.core.data.source.remote.response.ResponseDataRawProduct
import com.example.core.data.source.remote.response.SertifClaimLinkResponse
import com.example.core.data.source.remote.response.SertifClaimProducerLinkResponse
import com.example.core.data.source.remote.response.SertifClaimResponse
import com.example.core.data.source.remote.response.ShippingResponse
import com.example.core.data.source.remote.response.SupplierProductClaimListResponse
import com.example.core.data.source.remote.response.SupplierProductShippingDetailResponse
import com.example.core.data.source.remote.response.SupplierShippingDetailProducerResponse
import com.example.core.data.source.remote.response.UploadImageProductProducerResponse
import com.example.core.data.source.remote.response.UploadImageProductSupplierResponse
import com.example.core.data.source.remote.response.UploadImageProfileSupplierResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
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

    @GET("producer/profile")
    fun getProfileProducer(
        @Header("X-API-AUTH-PRODUCER") accessToken: String
    ): Call<ProfileProducerResponse>

    @PUT("supplier/profile")
    fun updateProfileSupplier(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Body requestBody: RequestBody
    ): Call<ProfileResponse>

    @PUT("producer/profile")
    fun updateProfileProducer(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
        @Body requestBody: RequestBody
    ): Call<ProfileResponse>

    @Multipart
    @POST("supplier/image/profile")
    fun uploadImageProfileSupplier(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Part file: MultipartBody.Part
    ): Call<UploadImageProfileSupplierResponse>

    @Multipart
    @POST("producer/image/profile")
    fun uploadImageProfileProducer(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
        @Part file: MultipartBody.Part
    ): Call<UploadImageProfileSupplierResponse>

    @Multipart
    @POST("supplier/image/product")
    fun uploadImage(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Part file: MultipartBody.Part
    ): Call<UploadImageProductSupplierResponse>

    @Multipart
    @POST("producer/image/product")
    fun uploadImageProducer(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
        @Part file: MultipartBody.Part
    ): Call<UploadImageProductProducerResponse>

    @POST("supplier/product")
    fun addProductSupplier(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Body requestBody: Map<String, String>
    ): Call<AddProductSupplierResponse>

    @POST("producer/product")
    fun addProductProducer(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
        @Body requestBody: Map<String, String>
    ): Call<AddProductProducerResponse>

    @POST("producer/raw-product")
    fun addStokProducer(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
        @Body requestBody: Map<String, String>
    ): Call<AddRawProductResponse>

    @GET("supplier/product")
    fun getSupplierProduct(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
    ): Call<ProductListResponse>

    @GET("producer/product")
    fun getProducerProduct(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
    ): Call<ProductListResponse>

    @GET("supplier/product/{productId}")
    fun getSupplierProductDetail(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Path("productId") productId: String
    ): Call<ProductDetailSupplierResponse>

    @GET("producer/product/{productId}")
    fun getProducerProductDetail(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
        @Path("productId") productId: String
    ): Call<ProductDetailProducerResponse>

    @GET("producer/supplier-shipping/{supplierShippingQrCode}")
    fun getProducerProductDetailSupplier(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
        @Path("supplierShippingQrCode") supplierShippingQrCode: String
    ): Call<SupplierShippingDetailProducerResponse>

    @DELETE("supplier/product/{productId}")
    fun deleteSupplierProductDetail(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Path("productId") productId: String
    ): Call<ProductDetailSupplierResponse>

    @DELETE("producer/product/{productId}")
    fun deleteProducerProductDetail(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
        @Path("productId") productId: String
    ): Call<ProductDetailProducerResponse>

    @GET("supplier/product")
    suspend fun getSupplierProductList(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Query("page") totalPage: Int,
        @Query("limit") totalData: Int
    ): ProductListResponse

    @GET("producer/product")
    suspend fun getProducerProductList(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
        @Query("page") totalPage: Int,
        @Query("limit") totalData: Int
    ): ProductListResponse


    @GET("supplier/product")
    suspend fun getSearchSupplierProductList(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Query("search") search: String,
    ): ProductListResponse

    @GET("producer/product")
    suspend fun getSearchProducerProductList(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
        @Query("search") search: String,
    ): ProductListResponse

    @POST("supplier/shipping")
    fun getQRShipping(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Body requestBody: RequestBody
    ): Call<ShippingResponse>

    @GET("supplier/producer-all")
    fun getProducerList(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
    ): Call<GetProducerListResponse>


    @GET("supplier/shipping/{shippingId}")
    fun getSupplierShippingDetail(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Path("shippingId") shippingId: String
    ): Call<SupplierProductShippingDetailResponse>

    @GET("supplier/shipping")
    suspend fun getSupplierShippingList(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Query("page") totalPage: Int,
        @Query("limit") totalData: Int
    ): GetShippingListResponse

    @GET("supplier/shipping")
    suspend fun getSearchShippingProductList(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Query("search") search: String,
    ): GetShippingListResponse

    @GET("supplier/product/{productId}/product-claim")
    fun getProductClaimSupplier(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Path("productId") productId: String
    ): Call<SupplierProductClaimListResponse>

    @GET("supplier/product/{productId}/product-claim")
    suspend fun getProductClaimSupplierList(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Path("productId") productId: String,
        @Query("page") totalPage: Int,
        @Query("limit") totalData: Int
    ): SupplierProductClaimListResponse

    @GET("producer/product/{productId}/product-claim")
    suspend fun getProductClaimProducerList(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
        @Path("productId") productId: String,
        @Query("page") totalPage: Int,
        @Query("limit") totalData: Int
    ): SupplierProductClaimListResponse


    @POST("supplier/product-claim/{productClaimId}/product/{productId}")
    fun uploadLinkSertif(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Path("productClaimId") productClaimId: String,
        @Path("productId") productId: String,
        @Body requestBody: RequestBody
    ): Call<SertifClaimResponse>

    @POST("producer/product-claim/{productClaimId}/product/{productId}")
    fun uploadLinkSertifProducer(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
        @Path("productClaimId") productClaimId: String,
        @Path("productId") productId: String,
        @Body requestBody: RequestBody
    ): Call<SertifClaimResponse>

    @Multipart
    @POST("supplier/pdf")
    fun uploadSertif(
        @Header("X-API-AUTH-SUPPLIER") accessToken: String,
        @Part file: MultipartBody.Part
    ): Call<SertifClaimLinkResponse>

    @Multipart
    @POST("producer/pdf")
    fun uploadSertifProducer(
        @Header("X-API-AUTH-PRODUCER") accessToken: String,
        @Part file: MultipartBody.Part
    ): Call<SertifClaimProducerLinkResponse>


}
