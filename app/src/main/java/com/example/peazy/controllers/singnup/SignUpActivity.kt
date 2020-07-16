package com.example.peazy.controllers.singnup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.peazy.MapsActivity
import com.example.peazy.controllers.HomeActivity
import com.example.peazy.R
import com.example.peazy.controllers.login.LoginActivity
import com.example.peazy.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var databinding: ActivitySignUpBinding
    lateinit var viewmodels: SignUpViewMode
    lateinit var modelViewFactory: SignUpViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        ///modelViewFactory = SignUpViewModelFactory()
        viewmodels = ViewModelProvider(this).get(SignUpViewMode::class.java)
        databinding.btLogin.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        databinding.btSignUp.setOnClickListener {
            if(viewmodels.isvalidate())
            {
                val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                startActivity(intent)
            }
            else{

            }
        }
        databinding.lifecycleOwner=this
        databinding.signup = viewmodels

    }
}