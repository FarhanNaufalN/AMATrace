package com.example.amatrace.pages.producer.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amatrace.R
import com.example.amatrace.databinding.FragmentDashboardBinding
import com.example.amatrace.databinding.FragmentNotificationsBinding
import com.example.amatrace.pages.adapter.ProductProducerAdapter
import com.example.amatrace.pages.adapter.RawProductProducerAdapter
import com.example.amatrace.pages.producer.ui.dashboard.DashboardViewModel
import com.example.amatrace.pages.producer.ui.tambahprodukproducer.AddProdukProducerActivity
import com.example.core.data.source.remote.preferences.Preference
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var myPreference: Preference
    private lateinit var editTextSearch: EditText


    private val dashboardViewModel: NotificationsViewModel by viewModels {
        NotificationsViewModel.ViewModelFactory(requireContext(), getSearchQuery())
    }

    private lateinit var productAdapter: RawProductProducerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
// Ikuti preferensi mode malam hari yang ditetapkan oleh sistem

        val nightMode = AppCompatDelegate.getDefaultNightMode()
        val imageView = binding.imageView2
        println("Mode gelap: $nightMode")

// Ganti logo sesuai dengan mode gelap dan terang
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            imageView.setImageResource(R.drawable.logo_kalimat_white)
        }else {
            imageView.setImageResource(R.drawable.logo_kalimat)
        }

        myPreference = Preference(requireContext())

        productAdapter = RawProductProducerAdapter()

        // Load the profile image using Glide
        val account = myPreference.getAccountInfo()


        val name = account?.ownerName
        editTextSearch = binding.searchView

        binding.rvSupplier.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }

        val token = myPreference.getAccessToken()

        if (token != null) {
            dashboardViewModel.getAllProduct(token)
        }

        dashboardViewModel.product
            .cachedIn(viewLifecycleOwner.lifecycleScope) // Menambahkan caching di sini
            .observe(viewLifecycleOwner, Observer { pagingData ->
                viewLifecycleOwner.lifecycleScope.launch {
                    productAdapter.submitData(pagingData)
                }
            })

        binding.buttonTambahProduk.setOnClickListener {
            startActivity(Intent(requireContext(), AddProdukProducerActivity::class.java))
        }
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Panggil searchProducts dari HomeViewModel saat teks berubah
                val token = myPreference.getAccessToken() ?: return
                dashboardViewModel.searchProducts(token, s.toString())
            }
            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getSearchQuery(): String? {
        return binding.searchView.text?.toString()
    }
}