package com.example.amatrace.pages.producer.ui.notifications

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.amatrace.pages.producer.ui.dashboard.DashboardViewModel
import com.example.core.data.repository.ProducerProductRepository
import com.example.core.data.repository.RawProductRepository
import com.example.core.data.source.remote.response.Product
import com.example.core.data.source.remote.response.RawProduct
import com.example.core.di.InjectionProductProducer
import com.example.core.di.InjectionRawProductProducer
import kotlinx.coroutines.launch

class NotificationsViewModel (
    private val producerProductRepository: RawProductRepository,
    private val searchQueryState: String?


) : ViewModel() {
    private val _product = MutableLiveData<PagingData<RawProduct>>()
    val product: LiveData<PagingData<RawProduct>> = _product

    fun getAllProduct(token: String) {
        viewModelScope.launch {
            try {
                val productLiveData = producerProductRepository.getProduct()
                productLiveData.observeForever {
                    _product.postValue(it)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting all products: ${e.message}")
            }
        }
    }

    fun searchProducts(token: String, query: String) {
        viewModelScope.launch {
            try {
                producerProductRepository.searchProducts(query).collect {
                    _product.postValue(it)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error searching products: ${e.message}")
            }
        }
    }

    fun performSearch() {
        val query = searchQueryState ?: return // Mendeklarasikan query di sini
        viewModelScope.launch {
            try {
                producerProductRepository.searchProducts(query).collect {
                    _product.postValue(it)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error performing search: ${e.message}")
            }
        }
    }



    class ViewModelFactory(private val context: Context, private val searchQueryState: String?) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NotificationsViewModel(InjectionRawProductProducer.provideRepository(context), searchQueryState) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        const val TAG = "NotificationViewModelMain"
    }
}