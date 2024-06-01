package com.example.core.di

import android.content.Context
import com.example.core.data.repository.SupplierProductClaimRepository
import com.example.core.data.repository.SupplierProductRepository
import com.example.core.data.source.remote.network.Config

object InjectionProductClaimSupplier {
    fun provideRepository(context: Context): SupplierProductClaimRepository {
        val apiService = Config.getApiService()
        return SupplierProductClaimRepository(apiService, context)
    }
}