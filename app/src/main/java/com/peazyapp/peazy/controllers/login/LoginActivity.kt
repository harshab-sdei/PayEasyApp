package com.peazyapp.peazy.controllers.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.peazyapp.peazy.R
import com.peazyapp.peazy.controllers.HomeActivity
import com.peazyapp.peazy.controllers.choosepayment.ChoosePayMethod
import com.peazyapp.peazy.controllers.singnup.SignUpActivity
import com.peazyapp.peazy.databinding.ActivityLoginBinding
import com.peazyapp.peazy.models.signup.SignUP
import com.peazyapp.peazy.utility.AppUtility
import com.peazyapp.peazy.utility.Constants
import com.peazyapp.peazy.utility.Status
import com.peazyapp.peazy.utility.appconfig.UserPreferenc
import kotlinx.android.synthetic.main.forgotpws_dialog.view.*
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    lateinit var databinding: ActivityLoginBinding
    lateinit var viewmodels: LoginModelView
    lateinit var modelViewFactory: LoginModelViewFactory
    lateinit var progressDialog: ProgressDialog
    val TAG: String = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        modelViewFactory =
            LoginModelViewFactory("", "")
        viewmodels = ViewModelProvider(this, modelViewFactory).get(LoginModelView::class.java)
        var content=SpannableString(getString(R.string.forgotpws));
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        databinding.txtforgotpassword.setText(content);

        databinding.btSingup.setOnClickListener {
            val intent1 = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent1)
        }

        databinding.txtforgotpassword.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.forgotpws_dialog, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Forget Password")
            //show dialog
            val  mAlertDialog = mBuilder.show()
            //login button click of custom layout
            mDialogView.dialogforgotBtn.setOnClickListener {
                //dismiss dialog

                val email = mDialogView.editTextTextEmail.text.toString()
                if(AppUtility.getInstance().isEmailValid(email)){


                    setForgotpasswordObservers(email)
                    mAlertDialog.dismiss()

                }
                else
                {
                    mDialogView.editTextTextEmail.setError(Constants.email_error)
                }
                //get text from EditTexts of custom layout


            }
            //cancel button click of custom layout
            mDialogView.dialogCancelBtn.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
            }
        }
        databinding.btLogin.setOnClickListener {
            if (AppUtility.getInstance().isEmailValid(viewmodels.email.value.toString()) &&
                AppUtility.getInstance().isPasswordValid(viewmodels.pws.value.toString())
            ) {

                 val params=mapOf(Constants.EMAIL to viewmodels.email.value.toString(),
                    Constants.PASSWORD to viewmodels.pws.value.toString())
                setupObservers(params)

            } else {
                if (!AppUtility.getInstance().isEmailValid(viewmodels.email.value.toString())) {
                    viewmodels.error_email.value = Constants.email_error
                } else {
                    viewmodels.error_email.value = null
                }

                if (!AppUtility.getInstance().isPasswordValid(viewmodels.pws.value.toString())) {
                    viewmodels.error_pws.value = Constants.pws_error
                } else {
                    viewmodels.error_pws.value = null

                }
            }

        }
        databinding.lifecycleOwner=this
        databinding.login = viewmodels

    }

    private fun setupObservers(params:Map<String,String>) {
        try {
            viewmodels.loginUser(params)
                .observe(this, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {

                                resource.data?.let { response: Response<SignUP> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponse(
                                                it1, true
                                            )
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<SignUP> ->
                                        response.body().toString()
                                    })
                            }
                            Status.ERROR -> {
                                try {
                                    progressDialog!!.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            Status.LOADING -> {
                                progressDialog = ProgressDialog(this@LoginActivity)

                                progressDialog.setMessage("loading...")
                                progressDialog.show()


                            }
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    private fun setForgotpasswordObservers(email:String) {
        try {
            viewmodels.forgotPassword(email)
                .observe(this, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {

                                resource.data?.let { response: Response<SignUP> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponse(
                                                it1, false
                                            )
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<SignUP> ->
                                        response.body().toString()
                                    })
                            }
                            Status.ERROR -> {
                                try {
                                    progressDialog.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            Status.LOADING -> {
                                progressDialog = ProgressDialog(this@LoginActivity)
                                progressDialog.setMessage("loading...")
                                progressDialog.show()


                            }
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    fun sendResponse(signUP: SignUP,ishome:Boolean) {
        try {
            progressDialog.dismiss()

            if (signUP.status == 200) {

                if(ishome) {
                    if (signUP.res.user != null) {
                        UserPreferenc.setStringPreference(
                            Constants.USER_NAME,
                            "" + signUP.res.user.name
                        )
                        UserPreferenc.setStringPreference(
                            Constants.USER_EMAIL,
                            "" + signUP.res.user.email
                        )
                    }
                    UserPreferenc.setStringPreference(
                        Constants.ACCESS_TOKEN,
                        "" + signUP.res.access_token
                    )
                    if (signUP.res.user.card == null) {
                        UserPreferenc.setBooleanPreference(Constants.IS_USER_Login, true)
                        val intent = Intent(this@LoginActivity, ChoosePayMethod::class.java)
                        startActivity(intent)
                    } else {
                        UserPreferenc.setBooleanPreference(Constants.IS_USER_Login, true)
                        UserPreferenc.setBooleanPreference(Constants.IS_USER_Choose_Mode, true)
                        UserPreferenc.setStringPreference(Constants.PAYMENT_MODE, "1")

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }else
                {
                    Toast.makeText(this,"Successfully email sent.",Toast.LENGTH_LONG).show()
                }

            } else {
                val errors = signUP.err
                if (errors.errCode == 5) {
                    AppUtility.getInstance().alertDialogWithSingleButton(
                        applicationContext,
                        "Error",
                        "Error in  Sign Up"
                    )

                } else {
                    AppUtility.getInstance()
                        .alertDialogWithSingleButton(
                            this@LoginActivity,
                            "Error",
                            "" + errors.msg
                        )
                }
            }


        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

}



