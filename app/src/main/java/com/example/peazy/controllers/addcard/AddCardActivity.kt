package com.example.peazy.controllers.addcard

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.liveData
import com.example.peazy.R
import com.example.peazy.controllers.HomeActivity
import com.example.peazy.databinding.ActivityAddCardBinding
import com.example.peazy.models.addpaycard.AddPayCard
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import com.example.peazy.utility.Resource
import com.example.peazy.utility.appconfig.UserPreferenc
import com.example.peazy.webservices.RetrofitInsatance
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class AddCardActivity : AppCompatActivity() {
    lateinit var progressDialog: ProgressDialog
    lateinit var databinding: ActivityAddCardBinding
    val TAG: String = "AddCardActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_add_card)


        databinding.btSaveCard.setOnClickListener {
            val params = mapOf(
                "card_number" to "" + databinding.editcardnum.text.toString(),
                "card_holder_name" to "" + databinding.editcardholder.text.toString(),
                "expiry_date" to "" + databinding.editexdate.text.toString(),
                "card_id" to "" + databinding.editcvv.text.toString()
            )

            setAddCardObservers(params)
        }

    }

    fun addCard(params: Map<String, String>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.addCard(params)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    private fun setAddCardObservers(params: Map<String, String>) {
        try {
            addCard(params)
                .observe(this, androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.example.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<AddPayCard> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponse(it1)
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<AddPayCard> ->
                                        response.body().toString()
                                    })
                            }
                            com.example.peazy.utility.Status.ERROR -> {
                                try {
                                    progressDialog!!.dismiss()
                                    Log.e(TAG, "ERROR" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            com.example.peazy.utility.Status.LOADING -> {
                                progressDialog = ProgressDialog(this)

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

    fun sendResponse(addCard: AddPayCard) {
        try {
            progressDialog.dismiss()

            if (addCard.status == 200) {
                UserPreferenc.setStringPreference(Constants.PAYMENT_MODE, "1")
                UserPreferenc.setBooleanPreference(Constants.IS_USER_Choose_Mode, true)
                val intent = Intent(this@AddCardActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()


            } else {
                val errors = addCard.err

                AppUtility.getInstance()
                    .alertDialogWithSingleButton(
                        this,
                        "Error",
                        "" + errors.msg
                    )

            }


        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    fun clearInput() {
        databinding.editcardholder.setText("")
        databinding.editcardnum.setText("")
        databinding.editcvv.setText("")
        databinding.editexdate.setText("")
    }

}