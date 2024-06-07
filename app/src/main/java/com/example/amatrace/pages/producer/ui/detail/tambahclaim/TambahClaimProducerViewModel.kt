package com.example.amatrace.pages.producer.ui.detail.tambahclaim

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.core.data.repository.ClaimProducerRepository
import com.example.core.data.repository.ClaimSupplierRepository
import com.example.core.data.source.remote.response.ClaimList
import com.example.core.di.InjectionProductClaimProducer
import com.example.core.di.InjectionProductClaimSupplier
import kotlinx.coroutines.launch

class TambahClaimProducerViewModel (
    private val claimRepository: ClaimProducerRepository,
) : ViewModel() {
    private val _claim = MutableLiveData<PagingData<ClaimList>>()
    val claim: LiveData<PagingData<ClaimList>> = _claim

    fun getAllClaim(token: String, productId: String) {
        viewModelScope.launch {
            try {
                val claimLiveData = claimRepository.getClaim(productId)
                claimLiveData.observeForever {
                    _claim.postValue(it)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting all products: ${e.message}")
            }
        }
    }

    class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TambahClaimProducerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TambahClaimProducerViewModel(InjectionProductClaimProducer.provideRepository(context)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        const val TAG = "HomeViewModelMain"
    }
}