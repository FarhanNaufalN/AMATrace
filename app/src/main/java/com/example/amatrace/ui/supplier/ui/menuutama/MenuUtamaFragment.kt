package com.example.amatrace.ui.supplier.ui.menuutama

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.amatrace.databinding.FragmentMenuUtamaBinding

class MenuUtamaFragment : Fragment() {

    private var _binding: FragmentMenuUtamaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(MenuUtamaViewModel::class.java)

        _binding = FragmentMenuUtamaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMenuUtama
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}