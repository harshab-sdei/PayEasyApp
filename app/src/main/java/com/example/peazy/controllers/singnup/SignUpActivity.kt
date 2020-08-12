package com.example.peazy.controllers.singnup

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.peazy.R
import com.example.peazy.controllers.HomeActivity
import com.example.peazy.controllers.login.LoginActivity
import com.example.peazy.databinding.ActivitySignUpBinding
import com.example.peazy.models.signup.SignUP
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import com.example.peazy.utility.Status
import com.example.peazy.utility.appconfig.UserPreferenc
import retrofit2.Response


class SignUpActivity : AppCompatActivity() {
    val TAG: String = "SignUpActivity"
    lateinit var databinding: ActivitySignUpBinding
    lateinit var viewmodels: SignUpViewMode
    lateinit var modelViewFactory: SignUpViewModelFactory
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        viewmodels = ViewModelProvider(this).get(SignUpViewMode::class.java)
        databinding.btLogin.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }


        databinding.btSignUp.setOnClickListener {


            if (AppUtility.getInstance().isEmailValid(viewmodels.email.value.toString()) &&
                AppUtility.getInstance().validateLetters(viewmodels.person_name.value.toString()) &&
                AppUtility.getInstance().isPasswordValid(viewmodels.pws.value.toString())
            ) {

                /* val params=mapOf(Constants.NAME to viewmodels.person_name.value.toString(),
                    Constants.EMAIL to viewmodels.email.value.toString(),
                    Constants.PASSWORD to viewmodels.pws.value.toString(),
                    Constants.LANGUAGE_SEL to ""+1,
                    Constants.DEVICE_TOKEN to ""+AppUtility.getInstance().getDeviceToken(applicationContext),
                    Constants.PLATFORM to ""+1)*/
                setupObservers(
                    viewmodels.person_name.value.toString(),
                    viewmodels.email.value.toString(),
                    viewmodels.pws.value.toString()
                )

            } else {
                if (!AppUtility.getInstance().isEmailValid(viewmodels.email.value.toString())) {
                    viewmodels.error_email.value = Constants.email_error
                } else {
                    viewmodels.error_email.value = null
                }
                if (!AppUtility.getInstance()
                        .validateLetters(viewmodels.person_name.value.toString())
                ) {
                    viewmodels.error_prerson.value = Constants.person_name_error
                } else {
                    viewmodels.error_prerson.value = null
                }
                if (!AppUtility.getInstance().isPasswordValid(viewmodels.pws.value.toString())) {
                    viewmodels.error_pws.value = Constants.pws_error
                } else {
                    viewmodels.error_pws.value = null

                }
            }

            /*  val responseLiveDataivedata: LiveData<Response<SignUP>> = liveData {
                  val response: Response<SignUP> =RerofitInsatance.apiService.signupUser(params)
                  emit(response)
              }


              responseLiveDataivedata.observe(this, Observer {
                  val status: Int? =it.body()?.status
                  Toast.makeText(this@SignUpActivity,"Error in  Sign Up"+status,Toast.LENGTH_LONG).show()

              })
  */

        }


        databinding.lifecycleOwner = this
        databinding.signup = viewmodels

    }

    private fun setupObservers(name: String, email: String, pws: String) {
        try {
            viewmodels.signupUser(name, email, pws)
                .observe(this, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {

                                resource.data?.let { response: Response<SignUP> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            successmessage(
                                                it1
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
                                progressDialog = ProgressDialog(this@SignUpActivity)

                                progressDialog!!.setMessage("loading...")
                                progressDialog!!.show()


                            }
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    fun successmessage(signUP: SignUP) {
        try {
            progressDialog!!.dismiss()

            if (signUP.status == 200) {

                if (signUP.res.access_token.isEmpty()) {
                    AppUtility.getInstance().alertDialogWithSingleButton(
                        applicationContext,
                        "Alert",
                        "Please verify user and Login app"
                    )
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    UserPreferenc.setBooleanPreference(Constants.IS_USER_Login, true)
                    val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            } else {
                val errors = signUP.err
                if (errors.errCode == 5) {
                    AppUtility.getInstance().alertDialogWithSingleButton(
                        applicationContext,
                        "Sign Up Error",
                        "Error in  Sign Up"
                    )

                }else {

                        AppUtility.getInstance()
                            .alertDialogWithSingleButton(
                                this@SignUpActivity,
                                "Sign Up Error",
                                "" + errors.msg
                            )

                }
            }


        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

}