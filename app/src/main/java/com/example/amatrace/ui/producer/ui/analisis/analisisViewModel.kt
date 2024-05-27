package com.example.amatrace.ui.producer.ui.analisis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class analisisViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is analisis Fragment"
    }
    val text: LiveData<String> = _text
}