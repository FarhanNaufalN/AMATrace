package com.example.core.data.source.remote.response

data class UploadImageProductSupplierResponse(
    val success: Boolean,
    val message: String,
    val data: ImageUrl

)
data class ImageUrl(
    val image: String
)