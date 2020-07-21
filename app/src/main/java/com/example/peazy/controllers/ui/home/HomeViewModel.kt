package com.example.peazy.controllers.ui.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.peazy.utility.Resource
import com.example.peazy.webservices.RetrofitForGoogleApi
import kotlinx.coroutines.Dispatchers

class HomeViewModel : ViewModel() {

    fun findNearByPlace(param: Map<String, Any>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitForGoogleApi.apiService.findNearByPlace()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}