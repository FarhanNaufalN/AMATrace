package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RawProductListResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: RawProductListData
)

data class RawProductListData(
    @SerializedName("rawProducts") val rawProducts: List<RawProduct>,
    @SerializedName("totalData") val totalData: Int,
    @SerializedName("totalPage") val totalPage: Int
)

data class RawProduct(
    @SerializedName("id") val id: String,
    @SerializedName("stock") val stock: Int,
    @SerializedName("receivedAt") val receivedAt: String,
    @SerializedName("expiredAt") val expiredAt: String,
    @SerializedName("product") val product: Product,
    @SerializedName("supplier") val supplier: Supplier
)


