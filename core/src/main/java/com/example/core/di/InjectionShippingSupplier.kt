package com.example.core.di

import android.content.Context
import com.example.core.data.repository.ShippingListRepository

import com.example.core.data.source.remote.network.Config

object InjectionShippingSupplier {
    fun provideShippingRepository(context: Context): ShippingListRepository {
        val apiService = Config.getApiService()
        return ShippingListRepository(apiService, context)
    }
}