package com.example.peazy.controllers.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.peazy.utility.Resource
import com.example.peazy.webservices.RetrofitInsatance
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {
    fun getCategoryList(params: Map<String, String>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.getCategoryList(params)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getSubCategory(path: String, params: Map<String, String>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.getSubCategory(path, params)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getMenuItem(params: Map<String, String>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.getMenuItem(params)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}