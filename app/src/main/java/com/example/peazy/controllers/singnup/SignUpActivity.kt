package com.example.peazy.controllers.singnup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.peazy.R
import com.example.peazy.databinding.ActivitySignUpBinding
import com.example.peazy.models.modelview.signup.SignUpViewMode
import com.example.peazy.models.modelview.signup.SignUpViewModelFactory

class SignUpActivity : AppCompatActivity() {
    lateinit var databinding: ActivitySignUpBinding
    lateinit var viewmodels: SignUpViewMode
    lateinit var modelViewFactory: SignUpViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        ///modelViewFactory = SignUpViewModelFactory()
        viewmodels = ViewModelProvider(this).get(SignUpViewMode::class.java)
        databinding.lifecycleOwner=this

        databinding.signup = viewmodels

    }
}