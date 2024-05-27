package com.example.amatrace.ui.producer.ui.daftarbatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.amatrace.databinding.FragmentDaftarbacthBinding
import com.example.amatrace.databinding.FragmentStockpasokanBinding

class daftarbatchFragment : Fragment() {

private var _binding: FragmentDaftarbacthBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val daftarbatchViewModel =
            ViewModelProvider(this).get(daftarbatchViewModel::class.java)

    _binding = FragmentDaftarbacthBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textDaftarbatch
    daftarbatchViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}