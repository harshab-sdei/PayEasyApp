package com.example.peazy.controllers.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants


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
        var msg=AppUtility.getInstance().isPasswordValid(pws.value.toString())
        if (!msg.isEmpty()) {
            error_pws.value=msg

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