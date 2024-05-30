package com.example.core.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.core.data.source.Paging.ProductSupplierPagingSource
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.response.Product

class SupplierProductRepository (private val apiService: API, private val context: Context) {
    fun getProduct(): LiveData<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ProductSupplierPagingSource(apiService, context)
            }
        ).liveData
    }
}