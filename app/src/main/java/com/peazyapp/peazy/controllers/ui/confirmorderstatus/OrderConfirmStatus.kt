package com.peazyapp.peazy.controllers.ui.confirmorderstatus

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.peazyapp.peazy.R
import com.peazyapp.peazy.controllers.HomeActivity
import com.peazyapp.peazy.controllers.ui.confirmorderstatus.adepter.OrderConfirmItemAdepter
import com.peazyapp.peazy.controllers.ui.menu.Menu
import com.peazyapp.peazy.databinding.ActivityOderConfirmStatusBinding
import com.peazyapp.peazy.models.verifypay.Item
import com.peazyapp.peazy.models.verifypay.VerifyPay
import com.peazyapp.peazy.utility.AppUtility
import com.peazyapp.peazy.utility.Constants
import retrofit2.Response

class OrderConfirmStatus : AppCompatActivity() {
    lateinit var databinding: ActivityOderConfirmStatusBinding
    lateinit var viewModel: OrderConfirmViewModel
    val TAG = "OrderConfirmStatus"
    lateinit var progressDialog: ProgressDialog

    private lateinit var viewAdapter: OrderConfirmItemAdepter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_oder_confirm_status)
        viewModel = ViewModelProvider(this).get(OrderConfirmViewModel::class.java)
        databinding.recleviewOrderitem.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setconfirmpayObservers(Constants.stripToken!!)

        databinding.btOrdermore.setOnClickListener {
            Constants.isredirectToMenu = true
            val intent = Intent(this@OrderConfirmStatus, HomeActivity::class.java)
            startActivity(intent)
            finish()

        }

        databinding.btThankyou.setOnClickListener {
            val intent = Intent(this@OrderConfirmStatus, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun listItemClick(selsectItem: Item) {

    }

    private fun setconfirmpayObservers(striptoken: String) {
        try {
            viewModel.confirmPay(striptoken)
                .observe(this, androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.peazyapp.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<VerifyPay> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponseForConfirm(it1)
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<VerifyPay> ->
                                        response.body().toString()
                                    })
                            }
                            com.peazyapp.peazy.utility.Status.ERROR -> {
                                try {
                                    progressDialog.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            com.peazyapp.peazy.utility.Status.LOADING -> {
                                progressDialog = ProgressDialog(this)

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


    @SuppressLint("ResourceAsColor")
    fun sendResponseForConfirm(verifyPay: VerifyPay) {
        try {
            progressDialog.dismiss()

            if (verifyPay.status == 200) {

                viewAdapter =
                    OrderConfirmItemAdepter(
                        verifyPay.res.stripe_token.item,
                        { selsectItem: Item -> listItemClick(selsectItem) })
                databinding.recleviewOrderitem.adapter = viewAdapter

                try {
                    if (verifyPay.res.stripe_token.order_status != 0) {
                        databinding.isreadylb.visibility = View.VISIBLE
                        databinding.oderstatusMsg.text = "" + getString(R.string.odernumber)
                        databinding.layOderstatus.setBackgroundResource(R.drawable.confirmorder_bg)

                    }
                    if (verifyPay.res.stripe_token.order_id.contains("-")) {
                        var str = verifyPay.res.stripe_token.order_id.split("-")
                        databinding.oderno.text = "" + str[1]

                    } else {
                        databinding.oderno.text = "" + verifyPay.res.stripe_token.order_id
                    }

                    try {
                        viewModel.sub_total.value = 0.0
                        for (addItem in verifyPay.res.stripe_token.item) {
                            viewModel.sub_total.value =
                                viewModel.sub_total.value!! + addItem.quantity.toInt() * addItem.price
                        }

                        databinding.subtotal.text =
                            Constants.currency + String.format("%.2f", viewModel.sub_total.value)
                        databinding.txtvat.text = "" + Constants.vat + "%"
                        databinding.txtservicefee.text = Constants.currency + Constants.servicefree
                        viewModel._total.value = viewModel.sub_total.value
                        databinding.total.text = "" + Constants.currency + viewModel._total.value
                    } catch (e: Exception) {

                    }
                } catch (e: Exception) {
                }

            } else {
                val errors = verifyPay.err

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

}