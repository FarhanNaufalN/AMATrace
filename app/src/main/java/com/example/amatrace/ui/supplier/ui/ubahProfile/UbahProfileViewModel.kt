package com.example.amatrace.ui.supplier.ui.ubahProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UbahProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Ini Fragment Untuk Ubah Profile"
    }
    val text: LiveData<String> = _text
}