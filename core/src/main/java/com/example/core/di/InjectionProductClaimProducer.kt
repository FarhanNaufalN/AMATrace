package com.example.core.di

import android.content.Context
import com.example.core.data.repository.ClaimProducerRepository
import com.example.core.data.source.remote.network.Config

object InjectionProductClaimProducer {
    fun provideRepository(context: Context): ClaimProducerRepository {
        val apiService = Config.getApiService()
        return ClaimProducerRepository(apiService, context)
    }
}