package com.example.core.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.core.data.source.Paging.ProductClaimPagingSource
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.response.Claim

class SupplierProductClaimRepository (private val apiService: API, private val context: Context) {
    fun getClaimList(): LiveData<PagingData<Claim>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ProductClaimPagingSource(apiService, context)
            }
        ).liveData
    }
}