package com.example.amatrace.pages.supplier.ui.pengiriman

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core.data.repository.ShippingListRepository
import com.example.core.data.source.remote.response.Shipping
import com.example.core.di.InjectionProductSupplier
import com.example.core.di.InjectionShippingSupplier
import kotlinx.coroutines.launch

class PengirimanViewModel(
    private val shippingListRepository: ShippingListRepository,
    private val searchQueryState: String?
) : ViewModel() {

    private val _shipping = MutableLiveData<PagingData<Shipping>>()
    val shipping: LiveData<PagingData<Shipping>> = _shipping

    fun getAllShipping(token: String) {
        viewModelScope.launch {
            try {
                val shippingData = shippingListRepository.getShippingList()
                shippingData.observeForever {
                    _shipping.postValue(it)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting all shipping: ${e.message}")
            }
        }
    }

    fun searchShipping(token: String, query: String) {
        viewModelScope.launch {
            try {
                shippingListRepository.searchShipping(query).collect {
                    _shipping.postValue(it)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error searching shipping: ${e.message}")
            }
        }
    }

    fun performSearch() {
        val query = searchQueryState ?: return
        viewModelScope.launch {
            try {
                shippingListRepository.searchShipping(query).collect {
                    _shipping.postValue(it)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error performing search: ${e.message}")
            }
        }
    }

    companion object {
        const val TAG = "PengirimanViewModel"
    }
}

class ShippingViewModelFactory(private val context: Context, private val searchQueryState: String?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PengirimanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PengirimanViewModel(InjectionShippingSupplier.provideShippingRepository(context), searchQueryState) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
