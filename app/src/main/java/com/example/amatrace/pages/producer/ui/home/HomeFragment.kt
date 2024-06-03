package com.example.amatrace.pages.producer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.amatrace.databinding.FragmentHomeProducerBinding
import com.example.core.data.source.remote.preferences.Preference

class HomeFragment : Fragment() {
    private lateinit var myPreference: Preference


private var _binding: FragmentHomeProducerBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel =
            ViewModelProvider(this).get(HomeProducerViewModel::class.java)

    _binding = FragmentHomeProducerBinding.inflate(inflater, container, false)
    val root: View = binding.root




    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}