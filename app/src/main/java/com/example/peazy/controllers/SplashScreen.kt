package com.example.peazy.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.example.peazy.MainActivity
import com.example.peazy.R
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import com.example.peazy.utility.appconfig.UserPreferenc

class SplashScreen : AppCompatActivity() {
   lateinit var homeIntent :Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //  UserPreferenc.setStringPreference(Constants.DEVICE_TOKEN,""+ AppUtility.getInstance().getDeviceToken(applicationContext))

        Log.d(
            "Device Token",
            "" + UserPreferenc.getStringPreference(
                Constants.DEVICE_TOKEN,
                ""
            ) + "   accessToken=" + UserPreferenc.getStringPreference(
                Constants.ACCESS_TOKEN,
                ""
            )
        )
        val SPLASH_TIME_OUT = 3000
        if (!UserPreferenc.getBooleanPreference(Constants.IS_USER_Login, false)) {

            homeIntent = Intent(this@SplashScreen, MainActivity::class.java)
        } else {
            homeIntent = Intent(this@SplashScreen, HomeActivity::class.java)
        }
        Handler().postDelayed({
            //Do some stuff here, like implement deep linking
            startActivity(homeIntent)
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }
    /*override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }
    private fun hideSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }*/
}