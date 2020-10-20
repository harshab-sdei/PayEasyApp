package com.peazyapp.peazy.controllers.ui.confirmorderstatus

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.peazyapp.peazy.utility.Resource
import com.peazyapp.peazy.webservices.RetrofitInsatance
import kotlinx.coroutines.Dispatchers

class OrderConfirmViewModel : ViewModel() {
    var sub_total = MutableLiveData<Double>()
    var _total = MutableLiveData<Double>()

    fun confirmPay(striptoken: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.verifyPay(striptoken)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


}