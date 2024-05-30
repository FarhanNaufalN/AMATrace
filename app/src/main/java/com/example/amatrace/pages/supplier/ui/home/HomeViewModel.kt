package com.example.amatrace.pages.supplier.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.amatrace.pages.supplier.ui.home.ViewModelFactory.Companion.TAG
import com.example.core.data.repository.SupplierProductRepository
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.response.Product
import com.example.core.data.source.remote.response.ProductListResponse
import com.example.core.di.InjectionProductSupplier
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel (supplierProductRepository: SupplierProductRepository) : ViewModel() {
    private val _listProduct = MutableLiveData<List<Product>>()
    private val listProduct: LiveData<List<Product>> = _listProduct

    val product: LiveData<PagingData<Product>> by lazy {
        supplierProductRepository.getProduct().cachedIn(viewModelScope)
    }

    fun getProductsPaging() = listProduct

    fun getAllProduct(token: String){
        val client = Config.getApiService().getSupplierProduct(token)
        client.enqueue(object : Callback<ProductListResponse> {
            override fun onResponse(
                call: Call<ProductListResponse>,
                response: Response<ProductListResponse>
            ) {
                if (response.isSuccessful) {
                    _listProduct.postValue(response.body()?.data?.products)
                }
            }

            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }


}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(InjectionProductSupplier.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        const val TAG = "HomeViewModelMain"
    }
}

