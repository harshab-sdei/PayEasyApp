package com.peazyapp.peazy.controllers.singnup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.peazyapp.peazy.utility.AppUtility
import com.peazyapp.peazy.utility.Constants
import com.peazyapp.peazy.utility.Resource
import com.peazyapp.peazy.webservices.RetrofitInsatance
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
            emit(Resource.success(data = RetrofitInsatance.apiService.signupUser(name, email, pws)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}


