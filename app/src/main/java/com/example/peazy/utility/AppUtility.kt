package com.example.peazy.utility


import android.content.Context
import android.content.DialogInterface
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.peazy.R
import com.example.peazy.utility.appconfig.UserPreferenc
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

            /*  // Password should contain at least one number
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
              }*/

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

        Log.d(
            "Token",
            "" + deviceAppUID + "accessToken=" + UserPreferenc.getStringPreference(
                Constants.ACCESS_TOKEN,
                ""
            )
        )
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


    fun showToast(ctx: Context?, Message: String?) {

        /* Toast toast = Toast.makeText(ctx, Message, Toast.LENGTH_LONG);
        toast.show();
*/
        if (ctx != null) {
//            Typeface fontLight = getFontRegular(ctx);
            val customToastroot: View =
                LayoutInflater.from(ctx).inflate(R.layout.custom_toast, null)
            val text =
                customToastroot.findViewById<View>(R.id.tv_toast_message) as TextView

            val toastView =
                customToastroot.findViewById<View>(R.id.layout_toast_background) as LinearLayout

//            text.setTypeface(fontLight);
            text.text = Message
            val customtoast = Toast(ctx)
            val bgDrawable = toastView.background as GradientDrawable
            bgDrawable.cornerRadius = 5f

            //icon.setImageResource(R.drawable.ic_action_success);
            bgDrawable.setColorFilter(
                ctx.resources.getColor(R.color.black),
                PorterDuff.Mode.SRC_IN
            )
            //icon.setImageResource(R.drawable.ic_action_warning);
            customtoast.view = customToastroot
            customtoast.setGravity(
                Gravity.BOTTOM or Gravity.CENTER_VERTICAL, 0
                , 150
            )
            customtoast.duration = Toast.LENGTH_LONG
            customtoast.show()
        } else {
            Log.e("showCustomToastAlert: ", "context null")
        }
    }


}