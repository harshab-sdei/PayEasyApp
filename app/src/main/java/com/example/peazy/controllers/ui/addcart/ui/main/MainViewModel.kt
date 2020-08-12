package com.example.peazy.controllers.ui.addcart.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.peazy.utility.Resource
import com.example.peazy.webservices.RetrofitInsatance
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {
    var sub_total = MutableLiveData<Double>()
    var _total = MutableLiveData<Double>()

    fun payOrder(params: Map<String, String>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.payOrder(params)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun confirmPay(striptoken: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.verifyPay(striptoken)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}