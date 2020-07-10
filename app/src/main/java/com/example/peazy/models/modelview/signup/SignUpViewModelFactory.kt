package com.example.peazy.models.modelview.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.peazy.models.modelview.login.LoginModelView
import com.example.peazy.webservices.MainRepository
import java.lang.IllegalArgumentException

class SignUpViewModelFactory(private val mainRepository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
     /*   if(modelClass.isAssignableFrom(SignUpViewMode::class.java))
        {
            return SignUpViewMode(mainRepository) as T
        }*/
        throw IllegalArgumentException("Unknown model view ")    }
}