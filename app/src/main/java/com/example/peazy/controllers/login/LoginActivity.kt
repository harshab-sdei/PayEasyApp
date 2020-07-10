package com.example.peazy.controllers.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.peazy.R
import com.example.peazy.controllers.singnup.SignUpActivity
import com.example.peazy.databinding.ActivityLoginBinding
import com.example.peazy.models.modelview.login.LoginModelView
import com.example.peazy.models.modelview.login.LoginModelViewFactory


class LoginActivity : AppCompatActivity() {
    lateinit var databinding: ActivityLoginBinding
    lateinit var viewmodels: LoginModelView
    lateinit var modelViewFactory: LoginModelViewFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        modelViewFactory = LoginModelViewFactory("", "")
        viewmodels = ViewModelProvider(this, modelViewFactory).get(LoginModelView::class.java)
        var content=SpannableString(getString(R.string.forgotpws));
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        databinding.txtforgotpassword.setText(content);

        databinding.btSingup.setOnClickListener {
            val intent1 = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent1)
        }
        databinding.lifecycleOwner=this

        databinding.login = viewmodels

    }
}



