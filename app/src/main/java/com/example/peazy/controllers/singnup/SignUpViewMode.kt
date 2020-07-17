package com.example.peazy.controllers.singnup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import com.example.peazy.utility.Resource
import com.example.peazy.webservices.ApiHelper
import com.example.peazy.webservices.RerofitInsatance
import kotlinx.coroutines.Dispatchers

class SignUpViewMode/*(private val mainRepository: MainRepository)*/:ViewModel() {
    var error_prerson=MutableLiveData<String>()
    var error_email = MutableLiveData<String>()
    var error_pws = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var pws = MutableLiveData<String>()
    var person_name = MutableLiveData<String>()
    var isvalid=MutableLiveData<Boolean>()

    fun checkvalidateLetters() {

        if (!AppUtility.getInstance().validateLetters(person_name.value.toString())) {
            isvalid.value=false
            error_prerson.value = Constants.person_name_error
        }else
            isvalid.value=true
    }
    fun checkEmailValid() {
        if (!AppUtility.getInstance().isEmailValid(email.value.toString())) {
            isvalid.value=false
            error_email.value = Constants.email_error

        }else {
            isvalid.value = true
        }
    }

    fun checkPasswordValid() {
        var msg = AppUtility.getInstance().isPasswordValid(pws.value.toString())
        if (!msg) {
            isvalid.value=false
            error_pws.value = Constants.pws_error
        }else
            isvalid.value=true
    }





    fun signupUser(name:String,email:String,pws:String ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RerofitInsatance.apiService.signupUser(name,email,pws)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}


