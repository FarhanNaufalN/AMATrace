package com.example.core.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class DetailRawProductResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: DetailRawData
)

data class DetailRawData(
    @SerializedName("id")
    val id: String,
    @SerializedName("expiredAt")
    val expiredAt: String,
    @SerializedName("remainingStock")
    val remainingStock: Int,
    @SerializedName("product")
    val product: RawDetailProduct,
    @SerializedName("supplier")
    val supplier: Supplier,
    @SerializedName("shippingInfo")
    val shippingInfo: ShippingInfo
)

data class RawClaim(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("status")
    val status: String
)

data class RawDetailProduct(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("sku")
    val sku: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("claims")
    val claims: List<Claim>,
)


data class RawShippingInfo(
    @SerializedName("deliveredBySupplierAt")
    val deliveredBySupplierAt: String,
    @SerializedName("note")
    val note: String?,
    @SerializedName("mass")
    val mass: Int,
    @SerializedName("trackingHistory")
    val trackingHistory: RawTrackingHistory
)

data class RawTrackingHistory(
    @SerializedName("supplierLocation")
    val supplierLocation: RawLocation,
    @SerializedName("receiptViaWarehouse")
    val receiptViaWarehouse: List<Any>, // Adjust the type based on actual data structure
    @SerializedName("arrival")
    val arrival: RawArrival
)

data class RawLocation(
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lon")
    val lon: String,
    @SerializedName("address")
    val address: String
)

data class RawArrival(
    @SerializedName("arrivedAt")
    val arrivedAt: String,
    @SerializedName("lattitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("producer")
    val producer: ProducerInfo
)

data class ProducerInfo(
    @SerializedName("id")
    val id: String,
    @SerializedName("businessName")
    val businessName: String,
    @SerializedName("ownerName")
    val ownerName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("address")
    val address: String
)
