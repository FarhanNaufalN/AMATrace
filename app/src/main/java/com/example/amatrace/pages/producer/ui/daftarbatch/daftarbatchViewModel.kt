package com.example.amatrace.pages.producer.ui.daftarbatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class daftarbatchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is daftarbatch Fragment"
    }
    val text: LiveData<String> = _text
}