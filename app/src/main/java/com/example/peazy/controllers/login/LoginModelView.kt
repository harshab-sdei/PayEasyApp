package com.example.peazy.controllers.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import com.example.peazy.utility.Resource
import com.example.peazy.webservices.RetrofitInsatance
import kotlinx.coroutines.Dispatchers


class LoginModelView(email: String,pws: String): ViewModel() {
    var error_email = MutableLiveData<String>()
    var error_pws = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var pws=MutableLiveData<String>()
    var isvalid=false

    init {
     //   error_email.value="Required"
        this.email.value=email
        this.pws.value=pws
    }

    fun checkValidation() {

        if (!AppUtility.getInstance().isEmailValid(email.value.toString())) {
            error_email.value=Constants.email_error

        }


    }

    fun checkEmailValid() {
        if (!AppUtility.getInstance().isEmailValid(email.value.toString())) {
            isvalid=false
            error_email.value = Constants.email_error

        }else
            isvalid=true
    }

    fun checkPasswordValid() {
        var msg = AppUtility.getInstance().isPasswordValid(pws.value.toString())
        if (!msg) {
            isvalid=false
            error_pws.value = Constants.pws_error
        }else
            isvalid=true
    }

    fun isvalidate():Boolean
    {
        return isvalid
    }

    fun loginUser(params:Map<String,String>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.loginUser(params)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun forgotPassword(email: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.fogotPassword(email)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}