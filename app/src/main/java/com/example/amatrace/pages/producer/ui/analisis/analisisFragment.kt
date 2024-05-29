package com.example.amatrace.pages.producer.ui.analisis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.amatrace.databinding.FragmentAnalisisBinding

class analisisFragment : Fragment() {

private var _binding: FragmentAnalisisBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val analisisViewModel =
            ViewModelProvider(this).get(analisisViewModel::class.java)

    _binding = FragmentAnalisisBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textDaftarbatch
    analisisViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}