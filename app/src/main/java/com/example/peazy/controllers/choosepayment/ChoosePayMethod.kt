package com.example.peazy.controllers.choosepayment

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.peazy.R
import com.example.peazy.controllers.HomeActivity
import com.example.peazy.controllers.addcard.AddCardActivity
import com.example.peazy.controllers.login.LoginActivity
import com.example.peazy.databinding.ActivityChoosePayMethodBinding
import com.example.peazy.utility.Constants
import com.example.peazy.utility.appconfig.UserPreferenc

class ChoosePayMethod : AppCompatActivity() {
    lateinit var databinding: ActivityChoosePayMethodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(
            this@ChoosePayMethod,
            R.layout.activity_choose_pay_method
        )

        if (Build.MANUFACTURER.toString().equals("Samsung")) {
            databinding.samsungpay.visibility = View.VISIBLE
        }
        databinding.cardpay.setOnClickListener {
            val intent = Intent(this@ChoosePayMethod, AddCardActivity::class.java)
            startActivity(intent)
        }

        databinding.btBycash.setOnClickListener {
            UserPreferenc.setStringPreference(Constants.PAYMENT_MODE, "2")
            UserPreferenc.setBooleanPreference(Constants.IS_USER_Choose_Mode, true)

            val intent = Intent(this@ChoosePayMethod, HomeActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
}