package com.example.core.data.source.remote.response

data class SertifClaimLinkResponse (
    val success: Boolean,
    val message: String,
    val data: pdfURL
)

data class pdfURL(
    val image: String
)

