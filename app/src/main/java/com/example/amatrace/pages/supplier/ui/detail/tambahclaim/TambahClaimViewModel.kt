package com.example.amatrace.pages.supplier.ui.detail.tambahclaim

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.PagingData
import com.example.amatrace.pages.supplier.ui.home.HomeViewModel
import com.example.amatrace.pages.supplier.ui.home.HomeViewModel.Companion.TAG
import com.example.core.data.repository.SupplierProductClaimRepository
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.response.Claim
import com.example.core.data.source.remote.response.ProductListResponse
import com.example.core.data.source.remote.response.SupplierProductClaimListResponse
import com.example.core.di.InjectionProductClaimSupplier
import com.example.core.di.InjectionProductSupplier
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahClaimViewModel (SupplierProductClaimRepository: SupplierProductClaimRepository) : ViewModel() {
    private val _listClaim = MutableLiveData<List<Claim>>()
    private val listClaim: LiveData<List<Claim>> = _listClaim

    val allClaim: LiveData<PagingData<Claim>> by lazy {
        SupplierProductClaimRepository.getClaimList().cachedIn(viewModelScope)
    }

    fun getClaimPaging() = listClaim

    fun getAllClaim(token: String,productId: String){
        val client = Config.getApiService().getProductClaimSupplier(token, productId)
        client.enqueue(object : Callback<SupplierProductClaimListResponse> {
            override fun onResponse(
                call: Call<SupplierProductClaimListResponse>,
                response: Response<SupplierProductClaimListResponse>
            ) {
                if (response.isSuccessful) {
                    _listClaim.postValue(response.body()?.data?.claims)
                }
            }

            override fun onFailure(call: Call<SupplierProductClaimListResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }

}

class TambahClaimViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TambahClaimViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TambahClaimViewModel(InjectionProductClaimSupplier.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        const val TAG = "TambahClaimViewModel"
    }
}
