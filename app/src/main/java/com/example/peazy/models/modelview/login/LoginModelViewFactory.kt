package com.example.peazy.models.modelview.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.peazy.webservices.MainRepository
import java.lang.IllegalArgumentException

class LoginModelViewFactory(private val email:String,private val pws:String) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginModelView::class.java))
        {
            return LoginModelView(email,pws) as T
        }
        throw IllegalArgumentException("Unknown model view ")
    }
}