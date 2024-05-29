package com.example.amatrace.pages.supplier.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core.data.source.Paging.ProductPagingSource
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.response.Product
import kotlinx.coroutines.flow.Flow
import retrofit2.Call


class ProductViewModel(private val api: API, private val token: String) : ViewModel() {

    val productPagingData: Flow<PagingData<Product>> = Pager(
        config = PagingConfig(
            pageSize = 5,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { ProductPagingSource(api, token) }
    ).flow.cachedIn(viewModelScope)
}

