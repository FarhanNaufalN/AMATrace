package com.example.amatrace.pages.supplier.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amatrace.databinding.FragmentHomeSupplierBinding
import com.example.amatrace.pages.adapter.ProductSupplierAdapter
import com.example.amatrace.pages.supplier.ui.tambahproduk.TambahProdukActivity
import com.example.core.data.source.remote.preferences.Preference
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeSupplierBinding? = null
    private val binding get() = _binding!!
    private lateinit var myPreference: Preference

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    private lateinit var productAdapter: ProductSupplierAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeSupplierBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myPreference = Preference(requireContext())

        productAdapter = ProductSupplierAdapter()

        // Load the profile image using Glide
        val account = myPreference.getAccountInfo()

        val name = account?.ownerName

        binding.rvSupplier.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }

        val token = myPreference.getAccessToken()

        if (token != null) {
            homeViewModel.getAllProduct(token)
        }

        homeViewModel.product.observe(viewLifecycleOwner, Observer { pagingData ->
            viewLifecycleOwner.lifecycleScope.launch {
                productAdapter.submitData(pagingData)
            }
        })

        binding.buttonTambahProduk.setOnClickListener {
            startActivity(Intent(requireContext(), TambahProdukActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
