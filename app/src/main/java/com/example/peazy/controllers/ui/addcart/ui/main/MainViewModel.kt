package com.example.peazy.controllers.ui.addcart.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var sub_total = MutableLiveData<Double>()
    var vat = MutableLiveData<Int>()
    var servicefee = MutableLiveData<Int>()
    var _total = MutableLiveData<Double>()

}