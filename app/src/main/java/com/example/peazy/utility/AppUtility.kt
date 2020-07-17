package com.example.peazy.utility


import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.peazy.R
import com.example.peazy.controllers.HomeActivity
import com.example.peazy.models.signup.SignUP
import com.example.peazy.webservices.RerofitInsatance
import kotlinx.android.synthetic.main.forgotpws_dialog.view.*
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern

class AppUtility {

    companion object {
        const val REG = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}\$"


        @Volatile
        private var INSTANCE: AppUtility? = null
        fun getInstance(): AppUtility {
            synchronized(this) {
                var instance = AppUtility.INSTANCE
                if (instance == null) {
                    instance = AppUtility()
                }
                return instance
            }

        }

    }
        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPasswordValid(pws: String): Boolean {
            var valid = true

            // Password should contain at least one number
            var exp = ".*[0-9].*"
            var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
            var matcher = pattern.matcher(pws.toString())
            if (!matcher.matches()) {
                Constants.pws_error="Password should contain at least one number"
               return  false
            }

            // Password should contain at least one capital letter
            exp = ".*[A-Z].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(pws.toString())
            if (!matcher.matches()) {
                Constants.pws_error="Password should contain at least one capital letter"
                return false
            }

            // Password should contain at least one small letter
            exp = ".*[a-z].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(pws.toString())
            if (!matcher.matches()) {
                Constants.pws_error="Password should contain at least one small letter"
                return false
            }

          /*  // Password should contain at least one special character
            // Allowed special characters : "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
            exp = ".*[~!@#\$%\\^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(pws.toString())
            if (!matcher.matches()) {
                valid = false
            }*/
            if (pws.toString().length >=8) {
            }else
            {
                Constants.pws_error="Required Min 8 characters"

                return false
            }

            return true
        }

    var PATTERN: Pattern = Pattern.compile(Companion.REG)
    fun CharSequence.isPhoneNumber() : Boolean = PATTERN.matcher(this).find()

    fun validateLetters(txt: String?): Boolean {
        var valid = true

        val regx = "[a-zA-Z ]+\\.?"
        val pattern =
            Pattern.compile(regx, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(txt)
        if (!matcher.matches()) {
            valid= false
        }
        return valid
    }

    fun isConnectivityAvailable(context: Context): Boolean {
        var activeNetwork: NetworkInfo? = null
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            activeNetwork = connectivityManager.activeNetworkInfo
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }


    fun getDeviceToken(context: Context):String
    {
        var deviceAppUID: String=""
        try {
            deviceAppUID=
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)

        }catch (e:Exception){e.printStackTrace()}

        Log.d("Token",""+deviceAppUID)
        return deviceAppUID

    }


    fun alertDialogWithSingleButton(context: Context,title:String,msg:String)
    {
        val dialogBuilder = AlertDialog.Builder(context)

        // set message of alert dialog
        dialogBuilder.setMessage(msg)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
           /* .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                    dialog, id -> dialog.dismiss()
            })*/
            // negative button text and action
            .setNegativeButton("Ok", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle(title)
        // show alert dialog
        alert.show()

    }





   }