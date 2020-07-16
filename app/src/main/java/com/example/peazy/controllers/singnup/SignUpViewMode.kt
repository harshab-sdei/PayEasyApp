package com.example.peazy.controllers.singnup

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
    var isvalid=false

    fun checkvalidateLetters() {

        if (!AppUtility.getInstance().validateLetters(person_name.value.toString())) {
            isvalid=false
            error_prerson.value = Constants.person_name_error
        }else
            isvalid=true
    }
    fun checkEmailValid() {
        if (!AppUtility.getInstance().isEmailValid(email.value.toString())) {
            isvalid=false
            error_email.value = Constants.email_error

        }else {
            isvalid = true
        }
    }

    fun checkPasswordValid() {
        var msg = AppUtility.getInstance().isPasswordValid(pws.value.toString())
        if (!msg.isEmpty()) {
            isvalid=false
            error_pws.value = msg
        }else
            isvalid=true
    }

    fun isvalidate():Boolean
    {
       return isvalid
    }


    }


