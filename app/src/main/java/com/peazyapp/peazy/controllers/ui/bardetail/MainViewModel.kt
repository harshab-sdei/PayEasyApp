package com.peazyapp.peazy.controllers.ui.bardetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.peazyapp.peazy.utility.Resource
import com.peazyapp.peazy.webservices.RetrofitInsatance
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    fun bookTable(params: Map<String, String>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.bookTable(params)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}