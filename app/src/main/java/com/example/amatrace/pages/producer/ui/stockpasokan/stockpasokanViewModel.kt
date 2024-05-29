package com.example.amatrace.pages.producer.ui.stockpasokan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class stockpasokanViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Stok Pasokan Fragment"
    }
    val text: LiveData<String> = _text
}