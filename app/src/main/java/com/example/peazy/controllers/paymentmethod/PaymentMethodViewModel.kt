package com.example.peazy.controllers.paymentmethod

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.peazy.utility.Resource
import com.example.peazy.webservices.RetrofitInsatance
import kotlinx.coroutines.Dispatchers

class PaymentMethodViewModel : ViewModel() {
    var cardholder = MutableLiveData<String>()
    var cardnum = MutableLiveData<String>()
    var cardex = MutableLiveData<String>()
    var cardcvv = MutableLiveData<String>()

    fun addCard(params: Map<String, String>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.addCard(params)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun viewCard() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.viewCard()))
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