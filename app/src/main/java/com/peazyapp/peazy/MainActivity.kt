package com.peazyapp.peazy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.peazyapp.peazy.controllers.login.LoginActivity
import com.peazyapp.peazy.controllers.singnup.SignUpActivity
import com.peazyapp.peazy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var databinding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        databinding.txtTerm.text = HtmlCompat.fromHtml(getString(R.string.terms_msg), HtmlCompat.FROM_HTML_MODE_LEGACY)

        databinding.btLogin.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        databinding.btSingup.setOnClickListener {
            val intent1 = Intent(this@MainActivity, SignUpActivity::class.java)
            startActivity(intent1)
        }

    }
}