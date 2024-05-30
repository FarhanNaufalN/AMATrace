package com.example.amatrace.pages.supplier.ui.pengiriman

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.amatrace.databinding.FragmentPengirimanBinding
import com.example.amatrace.pages.supplier.ui.tambahpengiriman.TambahPengirimanActivity


class PengirimanFragment : Fragment() {

    private var _binding: FragmentPengirimanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val pengirimanViewModel =
            ViewModelProvider(this).get(PengirimanViewModel::class.java)

        _binding = FragmentPengirimanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPengiriman
        pengirimanViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.buttonTambahPengiriman.setOnClickListener{
            startActivity(Intent(requireContext(), TambahPengirimanActivity::class.java))
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}