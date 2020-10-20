package com.peazyapp.peazy.controllers.paymentmethod

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.peazyapp.peazy.R
import com.peazyapp.peazy.databinding.PaymentMethodFragmentBinding
import com.peazyapp.peazy.models.addpaycard.AddPayCard
import com.peazyapp.peazy.models.verifypay.VerifyPay
import com.peazyapp.peazy.models.viewcard.ViewCard
import com.peazyapp.peazy.utility.AppUtility
import com.peazyapp.peazy.utility.Constants
import com.google.gson.GsonBuilder
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.*
import retrofit2.Response
import java.lang.ref.WeakReference

class PaymentMethodFragment : Fragment() {
    companion object {
        fun newInstance() = PaymentMethodFragment()
        private val nonDigits = Regex("[^\\d]")
        private val digits = Regex("[\\d]")

    }

    private lateinit var stripe: Stripe
    private lateinit var paymentIntentClientSecret: String
    var TAG = "PaymentMethodFragment"
    private lateinit var viewModel: PaymentMethodViewModel
    lateinit var databinding: PaymentMethodFragmentBinding
    lateinit var progressDialog: ProgressDialog

    var card = PaymentMethodCreateParams.Card.Builder()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databinding =
            DataBindingUtil.inflate(inflater, R.layout.payment_method_fragment, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        paymentIntentClientSecret = Constants.stripToken!!


        return databinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PaymentMethodViewModel::class.java)
        stripe = Stripe(
            this.requireActivity(),
            "pk_test_51H5tN2LsM2nr7hcERbIwzT4orsgMPcCVwitTE4OqhUQUjYMxUTvPTHoIsC34WW6Fc77r034TrRvoBNTbBf5sKKR900AjMbtf18"
        )

        setviewCardObservers()

        if (Build.MANUFACTURER.toString().equals("Samsung")) {
            databinding.samsungpay.visibility = View.VISIBLE
        }
        databinding.btBycash.setOnClickListener {
        }
        databinding.editcardnum.addTextChangedListener(object : TextWatcher {
            private var current = ""
            override fun afterTextChanged(s: Editable) {
                if (s.toString() != current) {
                    val userInput = s.toString().replace(nonDigits, "")
                    if (userInput.length <= 16) {
                        current = userInput.chunked(4).joinToString(" ")
                        s.filters = arrayOfNulls<InputFilter>(0)
                    }

                    s.replace(0, s.length, current, 0, current.length)

                }
                if (databinding.editcardholder.text.toString().length > 0 &&
                    databinding.editexdate.text.toString().length > 0 &&
                    databinding.editcvv.text.toString().length > 0
                ) {
                    databinding.btSaveCard.setBackgroundResource(R.drawable.button_dark_orange)
                } else {
                    databinding.btSaveCard.setBackgroundResource(R.drawable.button_dark_gray)
                }

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })

        databinding.payButton.setOnClickListener {
            progressDialog = ProgressDialog(this.requireContext())
            progressDialog.setMessage("loading...")
            progressDialog.show()
            card.setCvc(databinding.edcvv.text.toString())

            var cardparm = PaymentMethodCreateParams.create(card.build())

            val confirmParams = ConfirmPaymentIntentParams
                .createWithPaymentMethodCreateParams(cardparm, paymentIntentClientSecret)
            stripe.confirmPayment(this, confirmParams)


        }

        databinding.editexdate.addTextChangedListener(object : TextWatcher {
            private var mSeperator = false

            override fun afterTextChanged(s: Editable) {
                val input = s.toString().replace(nonDigits, "")

                if (s.length === 1) {
                    val month: Int = input.toInt()
                    if (month > 1) {
                        databinding.editexdate.setText(
                            "0" + databinding.editexdate.getText().toString() + "/"
                        )
                        databinding.editexdate.setSelection(
                            databinding.editexdate.getText().toString().length
                        )
                        mSeperator = true
                    }
                } else if (s.length === 2) {
                    val month: Int = input.toInt()
                    if (month <= 12) {
                        databinding.editexdate.setText(
                            databinding.editexdate.getText().toString() + "/"
                        )
                        databinding.editexdate.setSelection(
                            databinding.editexdate.getText().toString().length
                        )
                        mSeperator = true
                    }
                } else {
                }

                if (databinding.editcardholder.text.toString().length > 0 &&
                    databinding.editcardnum.text.toString().length > 0 &&
                    databinding.editcvv.text.toString().length > 0
                ) {
                    databinding.btSaveCard.setBackgroundResource(R.drawable.button_dark_orange)
                } else {
                    databinding.btSaveCard.setBackgroundResource(R.drawable.button_dark_gray)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })


        databinding.editcvv.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()

                if (s.toString() != current) {
                    if (userInput.length <= 3) {
                        current = userInput
                        s!!.filters = arrayOfNulls<InputFilter>(0)
                    }
                    s!!.replace(0, s.length, current, 0, current.length)

                }
                if (databinding.editcardholder.text.toString().length > 0 &&
                    databinding.editcardnum.text.toString().length > 0 &&
                    databinding.editexdate.text.toString().length > 0
                ) {
                    databinding.btSaveCard.setBackgroundResource(R.drawable.button_dark_orange)
                } else {
                    databinding.btSaveCard.setBackgroundResource(R.drawable.button_dark_gray)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {

            }
        })


        databinding.edcvv.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()/*.replace(digits,"X")*/

                if (s.toString() != current) {
                    if (userInput.length <= 3) {
                        current = userInput
                        s!!.filters = arrayOfNulls<InputFilter>(0)
                    }
                    s!!.replace(0, s.length, current, 0, current.length)
                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {

            }
        })

        databinding.btSaveCard.setOnClickListener {

            if (isCardDetailValid(
                    databinding.editcardnum.text.toString(),
                    databinding.editcardholder.text.toString(),
                    databinding.editexdate.text.toString(),
                    databinding.editcvv.text.toString()
                )
            ) {
                val params = mapOf(
                    "card_number" to "" + databinding.editcardnum.text.toString(),
                    "card_holder_name" to "" + databinding.editcardholder.text.toString(),
                    "expiry_date" to "" + databinding.editexdate.text.toString()
                )
                Log.d(TAG, "request json" + params)

                setAddCardObservers(params)
            }


        }


        try {
            var str: String? = requireArguments().getString(Constants.totalamount).toString()
            databinding.totalPrice.setText(str)
            setviewCardObservers()

        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Error:" + e.message)
        }

    }

    private fun setAddCardObservers(params: Map<String, String>) {
        try {
            viewModel.addCard(params)
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.peazyapp.peazy.utility.Status.SUCCESS -> {

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
                            com.peazyapp.peazy.utility.Status.ERROR -> {
                                try {
                                    progressDialog!!.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            com.peazyapp.peazy.utility.Status.LOADING -> {
                                progressDialog = ProgressDialog(this.requireContext())

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

    fun sendResponse(addCard: AddPayCard) {
        try {
            progressDialog.dismiss()

            if (addCard.status == 200) {
                AppUtility.getInstance()
                    .alertDialogWithSingleButton(
                        this.requireContext(),
                        "Alert",
                        "Successfully Add Your Card"
                    )
                clearInput()
                setviewCardObservers()


            } else {
                val errors = addCard.err

                AppUtility.getInstance()
                    .alertDialogWithSingleButton(
                        this.requireContext(),
                        "Error",
                        "" + errors.msg
                    )

            }


        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    fun isCardDetailValid(
        card_num: String,
        cardholder: String,
        exdate: String,
        cardcvv: String
    ): Boolean {
        var isvalid = true
        if (card_num.isEmpty() && card_num.length < 16) {
            databinding.editcardnum.setError(Constants.card_number)
            isvalid = false
        }
        if (cardholder.isEmpty() && AppUtility.getInstance().validateLetters(cardholder)) {
            databinding.editcardholder.setError(Constants.person_name_error)
            isvalid = false
        }

        if (exdate.isEmpty()) {
            databinding.editexdate.setError(Constants.valid_date)
            isvalid = false
        }

        if (!cardcvv.isEmpty() && cardcvv.length < 3) {
            databinding.editcvv.setError(Constants.valid_CVV)
            isvalid = false
        }

        return isvalid
    }

    fun clearInput() {
        databinding.editcardholder.setText("")
        databinding.editcardnum.setText("")
        databinding.editcvv.setText("")
        databinding.editexdate.setText("")
    }

    fun setcardDetail(cardnum: String, cardholder: String, cardexdaate: String) {

        viewModel.cardnum.value = cardnum
        viewModel.cardholder.value = cardholder
        viewModel.cardex.value = cardexdaate

        databinding.cardnumview.setText(cardnum.substring(14))
    }


    //webservice call for view card


    private fun setviewCardObservers() {
        try {
            viewModel.viewCard()
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.peazyapp.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<ViewCard> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponseforviewcard(it1)
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<ViewCard> ->
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
                                progressDialog = ProgressDialog(this.requireContext())

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


    fun sendResponseforviewcard(viewCard: ViewCard) {
        try {
            progressDialog.dismiss()

            if (viewCard.status == 200) {

                progressDialog.dismiss()

                databinding.layViewcard.visibility = View.VISIBLE
                val data = viewCard.res.card
                setcardDetail(data.card_number, data.card_holder_name, data.expiry_date)
                var str = data.expiry_date.toString().split("/")


                card.setNumber(data.card_number)
                card.setExpiryMonth(str.get(0).toInt())
                card.setExpiryYear(str.get(1).toInt())
                progressDialog.dismiss()


            } else {
                val errors = viewCard.err

                AppUtility.getInstance()
                    .alertDialogWithSingleButton(
                        this.requireContext(),
                        "Error",
                        "" + errors.msg
                    )

            }


        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    private fun setconfirmpayObservers(striptoken: String) {
        try {
            viewModel.confirmPay(striptoken)
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
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


                            }
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    fun sendResponseForConfirm(verifyPay: VerifyPay) {
        try {
            progressDialog.dismiss()

            if (verifyPay.status == 200) {

                findNavController().navigate(R.id.action_paymentMethodFragment_to_orderAcceptFragment)

            } else {
                val errors = verifyPay.err

                AppUtility.getInstance()
                    .alertDialogWithSingleButton(
                        this.requireContext(),
                        "Error",
                        "" + errors.msg
                    )
            }


        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val weakActivity = WeakReference<Activity>(this.requireActivity())
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                val status = paymentIntent.status
                if (status == StripeIntent.Status.Succeeded) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    weakActivity.get()?.let { activity ->

                        Log.d(TAG, "regest Id" + gson.toJson(paymentIntent))
                        setconfirmpayObservers(Constants.stripToken!!)


                    }
                } else if (status == StripeIntent.Status.RequiresPaymentMethod) {
                    progressDialog.dismiss()

                    weakActivity.get()?.let { activity ->
                        AppUtility.getInstance()
                            .alertDialogWithSingleButton(
                                activity,
                                "Payment failed",
                                "" + paymentIntent.lastPaymentError?.message.orEmpty()
                            )

                    }
                }

            }

            override fun onError(e: Exception) {
                progressDialog.dismiss()

                weakActivity.get()?.let { activity ->
                    AppUtility.getInstance()
                        .alertDialogWithSingleButton(
                            activity,
                            "Payment failed",
                            "" + e.message.toString()
                        )

                }
            }
        })
    }

}