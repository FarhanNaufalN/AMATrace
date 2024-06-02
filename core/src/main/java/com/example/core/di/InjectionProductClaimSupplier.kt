package com.example.core.di

import android.content.Context
import com.example.core.data.repository.ClaimSupplierRepository
import com.example.core.data.repository.SupplierProductRepository
import com.example.core.data.source.remote.network.Config

object InjectionProductClaimSupplier {
    fun provideRepository(context: Context): ClaimSupplierRepository {
        val apiService = Config.getApiService()
        return ClaimSupplierRepository(apiService, context)
    }
}