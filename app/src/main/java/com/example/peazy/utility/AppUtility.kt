package com.example.peazy.utility


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
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

        fun isPasswordValid(pws: String): String {
            var valid = true

            // Password should contain at least one number
            var exp = ".*[0-9].*"
            var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
            var matcher = pattern.matcher(pws.toString())
            if (!matcher.matches()) {
                Constants.email_error="Password should contain at least one number"
               return  "Password should contain at least one number"
            }

            // Password should contain at least one capital letter
            exp = ".*[A-Z].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(pws.toString())
            if (!matcher.matches()) {
                Constants.email_error="Password should contain at least one capital letter"
                return "Password should contain at least one capital letter"
            }

            // Password should contain at least one small letter
            exp = ".*[a-z].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(pws.toString())
            if (!matcher.matches()) {
                Constants.pws_error="Password should contain at least one small letter"
                return "Password should contain at least one small letter"
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
                valid = true
            }else
            {
                return "Required Min 8 characters"
            }

            return ""
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


}