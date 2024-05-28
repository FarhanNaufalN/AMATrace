package com.example.amatrace.ui.producer.ui.stockpasokan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class stockpasokanViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Stok Pasokan Fragment"
    }
    val text: LiveData<String> = _text
}