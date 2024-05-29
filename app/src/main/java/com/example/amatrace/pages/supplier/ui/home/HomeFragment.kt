package com.example.amatrace.pages.supplier.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amatrace.adapter.ProductPagingAdapter
import com.example.amatrace.databinding.FragmentHomeSupplierBinding
import com.example.amatrace.pages.supplier.ui.tambahproduk.TambahProdukActivity
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeSupplierBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductPagingAdapter
    private lateinit var viewModel: ProductViewModel
    private lateinit var accessToken: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeSupplierBinding.inflate(inflater, container, false)

        binding.buttonTambahProduk.setOnClickListener {
            startActivity(Intent(requireContext(), TambahProdukActivity::class.java))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter = ProductPagingAdapter()
        binding.rvSupplier.layoutManager = LinearLayoutManager(context)
        binding.rvSupplier.adapter = productAdapter

        accessToken = Preference(requireContext()).getAccessToken() ?: ""

        if (accessToken.isEmpty()) {
            // Handle the case where the access token is not available
            return
        }

        val api = Config.getApiService()

        viewModel = ViewModelProvider(this, ViewModelFactory(api, accessToken))
            .get(ProductViewModel::class.java)

        lifecycleScope.launch {
            viewModel.productPagingData.collectLatest { pagingData ->
                productAdapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ViewModelFactory(
    private val api: API,
    private val token: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(api, token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
