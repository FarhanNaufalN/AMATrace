package com.example.amatrace.pages.supplier.ui.pengiriman

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amatrace.R
import com.example.amatrace.databinding.FragmentPengirimanBinding
import com.example.amatrace.pages.adapter.ShippingAdapter
import com.example.amatrace.pages.supplier.ui.tambahpengiriman.TambahPengirimanActivity
import com.example.core.data.source.remote.preferences.Preference
import kotlinx.coroutines.launch

class PengirimanFragment : Fragment() {

    private var _binding: FragmentPengirimanBinding? = null
    private val binding get() = _binding!!
    private lateinit var myPreference: Preference
    private lateinit var editTextSearchShipping: EditText

    private val pengirimanViewModel: PengirimanViewModel by viewModels {
        ShippingViewModelFactory(requireContext(), getSearchQuery())
    }

    private lateinit var shippingAdapter: ShippingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPengirimanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myPreference = Preference(requireContext())

        shippingAdapter = ShippingAdapter()
        editTextSearchShipping = binding.searchViewShipping

        binding.rvShipping.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shippingAdapter
        }

        val nightMode = AppCompatDelegate.getDefaultNightMode()
        val imageView = binding.imageView2

// Ganti logo sesuai dengan mode gelap dan terang
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            imageView.setImageResource(R.drawable.logo_kalimat_white)
        }else {
            imageView.setImageResource(R.drawable.logo_kalimat)
        }

        val token = myPreference.getAccessToken()

        if (token != null) {
            pengirimanViewModel.getAllShipping(token)
        }

        pengirimanViewModel.shipping.observe(viewLifecycleOwner, Observer { pagingData ->
            viewLifecycleOwner.lifecycleScope.launch {
                shippingAdapter.submitData(pagingData)
            }
        })

        binding.buttonTambahPengiriman.setOnClickListener {
            startActivity(Intent(requireContext(), TambahPengirimanActivity::class.java))
        }


        editTextSearchShipping.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val token = myPreference.getAccessToken() ?: return
                pengirimanViewModel.searchShipping(token, s.toString())
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
        return editTextSearchShipping.text?.toString()
    }

}
