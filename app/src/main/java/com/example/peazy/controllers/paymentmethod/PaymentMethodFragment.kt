package com.example.peazy.controllers.paymentmethod

import android.app.ProgressDialog
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
import com.example.peazy.R
import com.example.peazy.databinding.PaymentMethodFragmentBinding
import com.example.peazy.models.addpaycard.AddPayCard
import com.example.peazy.models.verifypay.VerifyPay
import com.example.peazy.models.viewcard.ViewCard
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import retrofit2.Response

class PaymentMethodFragment : Fragment() {
    companion object {
        fun newInstance() = PaymentMethodFragment()
        private val nonDigits = Regex("[^\\d]")
        private val digits = Regex("[\\d]")

    }

    var TAG = "PaymentMethodFragment"
    private lateinit var viewModel: PaymentMethodViewModel
    lateinit var databinding: PaymentMethodFragmentBinding
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databinding =
            DataBindingUtil.inflate(inflater, R.layout.payment_method_fragment, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()


        return databinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PaymentMethodViewModel::class.java)
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

        databinding.btPayNow.setOnClickListener {
            Constants.stripToken?.let { it1 -> setconfirmpayObservers(it1) }
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
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            com.example.peazy.utility.Status.LOADING -> {
                                progressDialog = ProgressDialog(this.requireContext())

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
            progressDialog!!.dismiss()

            if (addCard.status == 200) {
                AppUtility.getInstance()
                    .alertDialogWithSingleButton(
                        this.requireContext(),
                        "Alert",
                        "Successfully Add Your Card"
                    )
                clearInput()


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
                            com.example.peazy.utility.Status.SUCCESS -> {

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
                            com.example.peazy.utility.Status.ERROR -> {
                                try {
                                    progressDialog!!.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            com.example.peazy.utility.Status.LOADING -> {
                                progressDialog = ProgressDialog(this.requireContext())

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


    fun sendResponseforviewcard(viewCard: ViewCard) {
        try {
            progressDialog!!.dismiss()

            if (viewCard.status == 200) {
                val data = viewCard.res.card
                setcardDetail(data.card_number, data.card_holder_name, data.expiry_date)

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
                            com.example.peazy.utility.Status.SUCCESS -> {

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
                            com.example.peazy.utility.Status.ERROR -> {
                                try {
                                    progressDialog!!.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            com.example.peazy.utility.Status.LOADING -> {
                                progressDialog = ProgressDialog(this.requireContext())

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

    fun sendResponseForConfirm(verifyPay: VerifyPay) {
        try {
            progressDialog!!.dismiss()

            if (verifyPay.status == 200) {
                /*      var bundle=Bundle()
                      bundle.putString(Constants.totalamount,binding.totalPrice.text.toString())
                      findNavController().navigate(R.id.action_addCartFragment_to_paymentMethodFragment,bundle)
      */

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
}