package com.example.peazy.controllers.ui.addcart.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebanx.swipebtn.OnActiveListener
import com.example.peazy.R
import com.example.peazy.controllers.ui.addcart.AddCartAdepter
import com.example.peazy.databinding.MainFragment3Binding
import com.example.peazy.models.addcart.Add_Item
import com.example.peazy.models.payorder.PayOrder
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import com.example.peazy.utility.appconfig.UserPreferenc
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.stripe.android.Stripe
import kotlinx.android.synthetic.main.add_instructions_dialog.view.*
import kotlinx.android.synthetic.main.add_instructions_dialog.view.in_close
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat


class AddCartFragment : Fragment() {

    companion object {
        fun newInstance() = AddCartFragment()
    }

    var isfirst: Boolean? = false
    val TAG: String = "AddCartFragment"
    var sub_total: Double = 0.0
    var sheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    var sheetBehavior1: BottomSheetBehavior<LinearLayout>? = null
    private lateinit var viewModel: MainViewModel
    lateinit var binding: MainFragment3Binding
    lateinit var progressDialog: ProgressDialog
    var instruction: String? = null
    var tip: Int? = 0
    private lateinit var stripe: Stripe
    private lateinit var paymentIntentClientSecret: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment3, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        return binding.root
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        try {

            updateOrderCalculation()

            binding.recleviewAdditem.layoutManager =
                LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)


            binding.tableNo.text = "" + Constants.tableNo
            binding.recleviewAdditem.adapter =
                AddCartAdepter(
                    Constants.addcartlist,
                    { selsectItem: Add_Item -> listItemClick(selsectItem) })

            sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
            sheetBehavior1 = BottomSheetBehavior.from(binding.bottomSheet2)
            sheetBehavior!!.setBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            binding.coordinatorLayout1.visibility = View.GONE
                            binding.coordinatorLayout2.visibility = View.VISIBLE
                            sheetBehavior1!!.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                        }
                        BottomSheetBehavior.STATE_DRAGGING, BottomSheetBehavior.STATE_SETTLING -> sheetBehavior!!.setHideable(
                            false
                        )
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
            sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED

            sheetBehavior1!!.setBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.coordinatorLayout2.visibility = View.GONE
                            binding.coordinatorLayout1.visibility = View.VISIBLE
                            binding.totalPrice.text = Constants.currency + viewModel._total.value
                            binding.totalPrice1.text = Constants.currency + viewModel._total.value
                            sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.coordinatorLayout2.visibility = View.GONE
                            binding.coordinatorLayout1.visibility = View.VISIBLE
                            binding.totalPrice.text = Constants.currency + viewModel._total.value
                            binding.totalPrice1.text = Constants.currency + viewModel._total.value
                            sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                        BottomSheetBehavior.STATE_DRAGGING, BottomSheetBehavior.STATE_SETTLING -> sheetBehavior1!!.setHideable(
                            true
                        )
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
            binding.txtaddmore.setOnClickListener {
                isfirst = false
                this.requireActivity().onBackPressed()

            }

            binding.txtaddinstraction.setOnClickListener {
                showDialog()
            }

            binding.btPlaceOrder1.setOnClickListener {
                oderConfirm()
            }
            binding.btPlaceOrder.setOnClickListener {
                oderConfirm()
            }


            binding.swipeNoState.setOnActiveListener(object : OnActiveListener {
                override fun onActive() {
                    binding.swipeNoState.setHasActivationState(false)
                    oderConfirm()
                }
            })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        viewModel._total.observe(this, Observer {
            binding.totalPrice.text = "" + String.format("%.2f", it)
            binding.totalPrice1.text = "" + String.format("%.2f", it)
            binding.btTotal.text = String.format("%.2f", it)
        })
        //for tip
        binding.layRoundup.setOnClickListener {
            try {
                binding.layRoundup.setBackgroundResource(R.drawable.button_dark_orange)
                binding.layCf2.setBackgroundResource(R.drawable.button_dark_gray)
                binding.layCf5.setBackgroundResource(R.drawable.button_dark_gray)

                val df = DecimalFormat("#")
                df.roundingMode = RoundingMode.UP
                tip = df.format(binding.roundedamount.text.toString().toDouble()).toInt()
                viewModel._total.value = (tip!! + sub_total.toInt()).toDouble()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.layCf2.setOnClickListener {
            tip = binding.cf2.text.toString().toInt()
            binding.layCf2.setBackgroundResource(R.drawable.button_dark_orange)
            binding.layRoundup.setBackgroundResource(R.drawable.button_dark_gray)
            binding.layCf5.setBackgroundResource(R.drawable.button_dark_gray)

            viewModel._total.value = sub_total + tip!!.toDouble()
        }
        binding.layCf5.setOnClickListener {
            tip = binding.cf5.text.toString().toInt()
            binding.layCf5.setBackgroundResource(R.drawable.button_dark_orange)
            binding.layRoundup.setBackgroundResource(R.drawable.button_dark_gray)
            binding.layCf2.setBackgroundResource(R.drawable.button_dark_gray)
            viewModel._total.value = sub_total + tip!!.toDouble()
        }

        binding.lifecycleOwner = this
        binding.myorder = viewModel

    }

    fun oderConfirm() {
        var jsonArray = JSONArray()
        for (items in Constants.addcartlist) {
            var jsonOb = JSONObject()
            jsonOb.put("item_id", items.item_id)
            jsonOb.put("item_name", items.name)
            jsonOb.put("quantity", items.num_of_unit)
            jsonOb.put("price", items.price.toInt())
            jsonArray.put(jsonOb)
        }
        var amount: Int = viewModel._total.value!!.toInt()
        val params: Map<String, Any?> = mapOf(
            "bar_id" to "" + Constants.bar_id,
            "mode" to "" + UserPreferenc.getStringPreference(Constants.PAYMENT_MODE, "2"),
            "amount" to "" + amount,
            "tips" to tip!!.toInt(),
            "instruction" to "" + instruction,
            "vat" to Constants.vat.toInt(),
            "p_fee" to Constants.servicefree.toInt(),
            "item" to "" + jsonArray.toString()
        )
        Log.d(TAG, "cartdata" + params.toString())

        if (!instruction.isNullOrBlank()) {
            setObservers(params)
        } else {
            showDialog()
        }
    }

    fun listItemClick(addItem: Add_Item) {
        updateOrderCalculation()
    }

    fun updateOrderCalculation() {
        viewModel.sub_total.value = 0.0
        for (addItem in Constants.addcartlist) {
            viewModel.sub_total.value =
                viewModel.sub_total.value!! + addItem.num_of_unit * addItem.price.toDouble()
        }

        sub_total = viewModel.sub_total.value!!
        binding.subtotal.text = Constants.currency + sub_total
        binding.txtvat.text = "" + Constants.vat + "%"
        binding.txtservicefee.text = Constants.currency + Constants.servicefree
        viewModel._total.value = sub_total
        binding.totalPrice.text = "" + String.format("%.2f", viewModel._total.value)
        binding.totalPrice1.text = "" + String.format("%.2f", viewModel._total.value)
        binding.roundedamount.text =
            String.format("%.2f", roundOffDecimal(viewModel.sub_total.value!!))
        if (isfirst == false) {
            val df = DecimalFormat("#")
            df.roundingMode = RoundingMode.UP
            tip = df.format((binding.roundedamount.text.toString()).toDouble()).toInt()
            viewModel._total.value = (tip!! + sub_total.toInt()).toDouble()
            isfirst = true
        }
    }

    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#")
        df.roundingMode = RoundingMode.UP
        val up: Double = df.format(number).toDouble()
        Log.d(TAG, "up=" + up)

        val down: Double = number
        Log.d(TAG, "down=" + down)

        val fractional_part: Double = (up - down)
        return fractional_part
    }

    fun serviceChargeCal() {
        var servicecharge: Double = ((sub_total * Constants.servicefree) / 100)
        if (servicecharge >= 2) {
            viewModel._total.value = (sub_total + 2).toDouble()
        } else {
            viewModel._total.value = sub_total + servicecharge

        }
    }


    private fun setObservers(params: Map<String, Any?>) {
        try {
            viewModel.payOrder(params)
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.example.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<PayOrder> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponse(it1)
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<PayOrder> ->
                                        response.body().toString()
                                    })
                            }
                            com.example.peazy.utility.Status.ERROR -> {
                                try {
                                    progressDialog.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            com.example.peazy.utility.Status.LOADING -> {
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


    fun sendResponse(payOrder: PayOrder) {
        try {
            progressDialog.dismiss()

            if (payOrder.status == 200) {
                var bundle = Bundle()
                bundle.putString(Constants.totalamount, binding.totalPrice.text.toString())
                Constants.stripToken = payOrder.res.stripe_token
                paymentIntentClientSecret = payOrder.res.stripe_token


                findNavController().navigate(
                    R.id.action_addCartFragment_to_paymentMethodFragment,
                    bundle
                )


                /* if(!payOrder.res.stripe_token.isEmpty())
                 setconfirmpayObservers(payOrder.res.stripe_token.toString())*/

            } else {
                val errors = payOrder.err

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




    fun showDialog() {
        val mDialogView =
            LayoutInflater.from(this.requireContext())
                .inflate(R.layout.add_instructions_dialog, null)
        val mBuilder = AlertDialog.Builder(this.requireContext())
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        mDialogView.bt_instruction.setOnClickListener {
            instruction = mDialogView.ed_instruction.text.toString()
            mAlertDialog.dismiss()

        }
        mDialogView.in_close.setOnClickListener {

            mAlertDialog.dismiss()
        }

    }

}