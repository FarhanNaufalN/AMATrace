package com.example.amatrace.pages.producer.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "List Stok Pasokan"
    }
    val text: LiveData<String> = _text
}