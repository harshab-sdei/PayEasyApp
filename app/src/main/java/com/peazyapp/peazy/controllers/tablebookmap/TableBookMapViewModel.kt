package com.peazyapp.peazy.controllers.tablebookmap

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.peazyapp.peazy.models.nearby.Bar
import com.peazyapp.peazy.utility.Resource
import com.peazyapp.peazy.webservices.RetrofitInsatance
import kotlinx.coroutines.Dispatchers

class TableBookMapViewModel : ViewModel() {
    var barlist = MutableLiveData<ArrayList<Bar>>()

    fun nearByBar(coordinate: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.nearByBar(coordinate)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun changeReserveTable(params: Map<String, String>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.changeReserveTable(params)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}