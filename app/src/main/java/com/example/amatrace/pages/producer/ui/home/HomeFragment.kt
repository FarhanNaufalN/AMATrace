package com.example.amatrace.pages.producer.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.amatrace.databinding.FragmentHomeProducerBinding
import com.example.amatrace.pages.producer.ui.home.QRCodeScannerActivity
import com.example.core.data.source.remote.preferences.Preference

class HomeFragment : Fragment() {
    private lateinit var myPreference: Preference

    private var _binding: FragmentHomeProducerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeProducerViewModel::class.java)

        _binding = FragmentHomeProducerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val scanButton = binding.scanButton
        scanButton.setOnClickListener {
            val intent = Intent(requireContext(), QRCodeScannerActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_QR_SCAN)
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_QR_SCAN && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra("SCAN_RESULT")
            // Handle hasil scan di sini
            Toast.makeText(requireContext(), "Hasil Scan: $result", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_CODE_QR_SCAN = 101
    }
}
