package com.example.peazy.models.modelview.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants

class SignUpViewMode/*(private val mainRepository: MainRepository)*/:ViewModel() {
    var error_prerson=MutableLiveData<String>()
    var error_email = MutableLiveData<String>()
    var error_pws = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var pws = MutableLiveData<String>()
    var person_name = MutableLiveData<String>()


    fun checkValidation() {

        if (!AppUtility.getInstance().validateLetters(person_name.value.toString())) {
            error_prerson.value = Constants.person_name_error
        }
        if (!AppUtility.getInstance().isEmailValid(email.value.toString())) {
            error_email.value = Constants.email_error

        }

        var msg=AppUtility.getInstance().isPasswordValid(pws.value.toString())
        if (!msg.isEmpty()) {
            error_pws.value=msg

        }

    }


}