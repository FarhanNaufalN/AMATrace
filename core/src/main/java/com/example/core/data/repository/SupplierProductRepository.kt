package com.example.core.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.core.data.source.paging.ProductSupplierPagingSource
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.response.Product
import kotlinx.coroutines.flow.Flow

class SupplierProductRepository(private val apiService: API, private val context: Context) {

    fun getProduct(searchQuery: String? = null ): LiveData<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25
            ),
            pagingSourceFactory = {
                ProductSupplierPagingSource(apiService, context, searchQuery)
            }
        ).liveData
    }

    fun searchProducts(query: String): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25
            ),
            pagingSourceFactory = {
                ProductSupplierPagingSource(apiService, context, query)
            }
        ).flow
    }
}