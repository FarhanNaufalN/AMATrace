package com.example.amatrace.pages.supplier.ui.pengiriman

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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.amatrace.R
import com.example.amatrace.databinding.FragmentPengirimanBinding
import com.example.amatrace.pages.adapter.ShippingAdapter
import com.example.amatrace.pages.supplier.ui.tambahpengiriman.TambahPengirimanActivity
import com.example.core.data.source.remote.preferences.Preference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class PengirimanFragment : Fragment() {

    private var _binding: FragmentPengirimanBinding? = null
    private val binding get() = _binding!!
    private lateinit var myPreference: Preference

    private val pengirimanViewModel: PengirimanViewModel by viewModels {
        ShippingViewModelFactory(requireContext())
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
        val profileImageView: CircleImageView = binding.profileImage

        // Load the profile image using Glide
        val account = myPreference.getAccountInfo()
        val profile = account?.avatar
        val name = account?.ownerName
        binding.nameOwner.text = name

        Glide.with(requireContext())
            .load(profile)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(R.drawable.profil_farhan)
            .error(R.drawable.profil_farhan)
            .into(profileImageView)

        binding.rvShipping.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shippingAdapter
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
