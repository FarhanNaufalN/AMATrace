package com.example.amatrace.ui.producer.ui.ubahProfileProducer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UbahProfileProducerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is ubahprofileproducer Fragment"
    }
    val text: LiveData<String> = _text
}